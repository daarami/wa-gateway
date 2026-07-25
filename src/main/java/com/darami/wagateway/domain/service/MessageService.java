package com.darami.wagateway.domain.service;

import com.darami.wagateway.client.meta.MetaApiClient;
import com.darami.wagateway.client.meta.dto.SendMessageResponse;
import com.darami.wagateway.client.meta.dto.TemplateMessageRequest;
import com.darami.wagateway.domain.model.OutboundMessage;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MetaApiClient metaApiClient;

    public MessageService(MetaApiClient metaApiClient) {
        this.metaApiClient = metaApiClient;
    }

    public String sendTemplateMessage(OutboundMessage message) {
        var request = TemplateMessageRequest.of(
                message.to(),
                message.templateName(),
                message.languageCode()
        );

        SendMessageResponse response = metaApiClient.sendTemplateMessage(request);
        return response.messages().get(0).id();
    }
}