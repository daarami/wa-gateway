package com.darami.wagateway.domain.model;

/**
 * Domain representation of a message to send. Deliberately simpler
 * than Meta's payload shape — the translation to Meta's format happens
 * only inside client/meta.
 */
public record OutboundMessage(
        String to,
        String templateName,
        String languageCode
) {
}