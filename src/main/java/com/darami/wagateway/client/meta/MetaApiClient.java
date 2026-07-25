package com.darami.wagateway.client.meta;

import com.darami.wagateway.client.meta.dto.SendMessageResponse;
import com.darami.wagateway.client.meta.dto.TemplateMessageRequest;

/**
 * Contract for talking to the WhatsApp Cloud API. The interface is the
 * seam that lets us swap the real HTTP implementation for a test double.
 */
public interface MetaApiClient {

    SendMessageResponse sendTemplateMessage(TemplateMessageRequest request);
}