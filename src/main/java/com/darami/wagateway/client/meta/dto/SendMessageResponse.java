// src/main/java/com/darami/wagateway/client/meta/dto/SendMessageResponse.java
package com.darami.wagateway.client.meta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Meta's response to a successful send. Contains the message ID we need
 * to correlate later webhook status updates back to this send.
 */
public record SendMessageResponse(
        @JsonProperty("messaging_product") String messagingProduct,
        List<Contact> contacts,
        List<MessageId> messages
) {
    public record Contact(String input, @JsonProperty("wa_id") String waId) {
    }

    public record MessageId(String id) {
    }
}