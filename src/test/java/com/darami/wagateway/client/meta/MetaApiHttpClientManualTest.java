package com.darami.wagateway.client.meta;

import com.darami.wagateway.client.meta.dto.SendMessageResponse;
import com.darami.wagateway.client.meta.dto.TemplateMessageRequest;
import com.darami.wagateway.config.WhatsAppAccountConfig;
import org.junit.jupiter.api.Test;

/**
 * Manual smoke test: hits the REAL Meta API, no mocks. Run this once by
 * hand to confirm the client works end to end. This is NOT a real unit
 * test (it depends on external state and your env vars) — WireMock-based
 * tests replace this once the client is proven to work.
 *
 * Reads credentials from environment variables, same ones as your .env.
 * Set them in your terminal session before running:
 *   $env:META_ACCESS_TOKEN="..."
 *   $env:META_PHONE_NUMBER_ID="..."
 */
class MetaApiHttpClientManualTest {

    @Test
    void sendsHelloWorldTemplateToRealMetaApi() {
        // We build the objects by hand — no Spring context involved.
        var account = new WhatsAppAccountConfig(
                System.getenv("META_ACCESS_TOKEN"),
                System.getenv("META_PHONE_NUMBER_ID"),
                System.getenv("META_APP_SECRET")
        );

        MetaApiClient client = new MetaApiHttpClient(account);

        var request = TemplateMessageRequest.of(
                "527151363503", // tu número, el que registraste como test recipient
                "hello_world",
                "en_US"
        );

        SendMessageResponse response = client.sendTemplateMessage(request);

        System.out.println("Message ID: " + response.messages().get(0).id());
    }
}