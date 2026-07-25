package com.darami.wagateway.client.meta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Root payload Meta POSTs to our webhook endpoint. One notification can
 * bundle multiple entries/changes, though in practice it's usually one.
 */
public record WebhookNotification(
        String object,
        List<Entry> entry
) {
    public record Entry(String id, List<Change> changes) {
    }

    public record Change(Value value, String field) {
    }

    public record Value(
            @JsonProperty("messaging_product") String messagingProduct,
            Metadata metadata,
            List<Contact> contacts,
            List<Status> statuses,
            List<IncomingMessage> messages
    ) {
    }

    public record Metadata(
            @JsonProperty("display_phone_number") String displayPhoneNumber,
            @JsonProperty("phone_number_id") String phoneNumberId
    ) {
    }

    public record Contact(@JsonProperty("wa_id") String waId) {
    }

    // Delivery/read status updates for messages WE sent
    public record Status(
            String id,
            String status,
            String timestamp,
            @JsonProperty("recipient_id") String recipientId
    ) {
    }

    // Messages sent TO us by an end user (not present in your payload,
    // but this is the shape when someone texts your test number)
    public record IncomingMessage(String from, String id, String timestamp, String type) {
    }
}