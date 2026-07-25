# wa-gateway

Gateway for the WhatsApp Cloud API (Meta). Wraps message sending and webhook
reception with retries, structured logs, and a simplified format. First
infrastructure piece of the Darami ecosystem — but scoped, today, to a
single-tenant service consumed by my own bot and dashboard.

## Scope guardrails (read before adding ANYTHING)

- Single tenant. ONE `WhatsAppAccount` config instance. Do NOT build
  multi-tenant tables, tenant resolution, or per-tenant auth.
- No queues, no plugin system, no generic "messaging provider" abstraction,
  no SaaS features. These are explicitly out of scope until 2-3 real client
  integrations exist.
- If a feature is not needed by the current version's "done" criteria,
  do not build it. Flag it as a suggestion instead.
- YAGNI wins every argument in this repo.

## Stack

- Java 21, Spring Boot 3, Maven
- Testing: JUnit 5 + Mockito, WireMock for stubbing the Meta API
- Logging: SLF4J/Logback, structured, with a `messageId` correlation field
- Config: environment variables via `application.yml` placeholders
- No additional frameworks or starters without discussing first

## Package structure

```
com.darami.wagateway
├── api/            REST controllers (MessageController, WebhookController)
├── domain/
│   ├── model/      OutboundMessage, TextMessage, TemplateMessage,
│   │               Recipient, MessageStatus, WhatsAppAccount
│   └── service/    MessageService, WebhookService
├── client/meta/    MetaApiClient (interface), MetaApiHttpClient (impl),
│                   dto/ (exact Meta payloads, isolated here)
├── infra/          RetryExecutor, RateLimitGuard, WebhookSignatureVerifier
├── config/         GatewayProperties, WhatsAppAccountConfig
└── exception/      GatewayException, MetaApiException,
                    RateLimitedException, InvalidWebhookException
```

## Architecture rules

- Domain models NEVER equal Meta DTOs. Translation happens only inside
  `client/meta`. Nothing outside that package imports Meta DTOs.
- `MetaApiClient` is an interface. Services depend on the interface.
- Every service method that touches Meta receives a `WhatsAppAccount`
  parameter explicitly. Never read tokens from a global/static context.
- Webhook events are normalized to our own event format before leaving
  `WebhookService`. Consumers depend on our contract, not Meta's.
- Public REST routes are versioned: `/v1/...` from day one.
- Message builders for anything nested (templates especially).

## Retries and rate limits

- Exponential backoff with jitter, max 3 attempts.
- Retry ONLY on: network errors, timeouts, 5xx, Meta's rate-limit error.
  Never retry validation 4xx.
- Sends are NOT idempotent (Cloud API has no idempotency keys). Retry
  aggressively only on clear connection failures (request never left);
  be conservative on response timeouts. Log every retry with its reason.
- RateLimitGuard: local configurable messages-per-second semaphore +
  treat Meta's rate-limit error as a backoff signal, not a failure.
- Do NOT hardcode Meta's limit numbers. Verify current values in Meta's
  Cloud API rate limits docs before implementing.

## Security

- Tokens only via env vars. `.env` is gitignored; keep `.env.example` updated.
- Use a Meta System User token (non-expiring), not the 24h panel token.
- Verify `X-Hub-Signature-256` (HMAC SHA-256 with app secret) on EVERY
  incoming webhook POST. The GET verify-token challenge is separate.
- Never log tokens. Never log full message content at INFO level;
  content only at DEBUG, truncated. Gateway logs contain end users'
  personal data — treat them as such.

## Testing expectations

- WireMock-based tests for `MetaApiHttpClient` (success, 5xx, rate limit,
  timeout paths).
- Unit tests for RetryExecutor (backoff math, retry/no-retry decisions)
  and WebhookSignatureVerifier (valid, invalid, missing signature).
- A couple of integration tests for controllers. No coverage percentage
  targets — cover the risky seams listed above.

## Conventions

- Code, comments, commits, and docs in English.
- Conventional Commits (`feat:`, `fix:`, `docs:`, `test:`, `refactor:`).
- GitHub Flow: `main` + short-lived feature branches.
- SemVer with git tags starting at `v0.1.0`.

## Version roadmap (do not skip ahead)

- **v0.1.0 (current target):** send text + template messages, receive and
  verify webhooks, retries, structured logs, env config, README.
  Done = a real message sent to my own WhatsApp via `curl` against the
  gateway, with its delivery status arriving through the webhook.
- **v0.1.x:** media / interactive messages, only as my own bot needs them.
- **v0.2.0:** integration with first real client bot. Persistence, gateway
  API-key auth, and client-specific message types get designed THEN.

## When in doubt

Propose the simplest version that satisfies the current milestone and
explain the tradeoff. Do not silently expand scope.