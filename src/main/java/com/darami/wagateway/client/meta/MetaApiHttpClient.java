package com.darami.wagateway.client.meta;

import com.darami.wagateway.client.meta.dto.SendMessageResponse;
import com.darami.wagateway.client.meta.dto.TemplateMessageRequest;
import com.darami.wagateway.config.WhatsAppAccountConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MetaApiHttpClient implements MetaApiClient {

    private static final String GRAPH_API_VERSION = "v21.0";

    private final RestClient restClient;
    private final WhatsAppAccountConfig account;

    public MetaApiHttpClient(WhatsAppAccountConfig account) {
        this.account = account;
        this.restClient = RestClient.builder()
                .baseUrl("https://graph.facebook.com/" + GRAPH_API_VERSION)
                .build();
    }

    @Override
    public SendMessageResponse sendTemplateMessage(TemplateMessageRequest request) {
        return restClient.post()
                .uri("/{phoneNumberId}/messages", account.phoneNumberId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + account.accessToken())
                .body(request)
                .retrieve()
                .body(SendMessageResponse.class);
    }
}