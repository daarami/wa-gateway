package com.darami.wagateway.client.meta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Exact payload Meta expects at POST /{phone-number-id}/messages
 * when sending a template message. Mirrors Meta's JSON field names
 * on purpose — this is the ONE place that's allowed to look like Meta's API.
 */
public record TemplateMessageRequest(
        @JsonProperty("messaging_product") String messagingProduct,
        String to,
        String type,
        TemplatePayload template
) {
    public static TemplateMessageRequest of(String to, String templateName, String languageCode) {
        return new TemplateMessageRequest(
                "whatsapp",
                to,
                "template",
                new TemplatePayload(templateName, new Language(languageCode))
        );
    }

    public record TemplatePayload(String name, Language language) {
    }

    public record Language(String code) {
    }
}
