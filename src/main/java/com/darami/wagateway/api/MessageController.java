package com.darami.wagateway.api;

import com.darami.wagateway.domain.model.OutboundMessage;
import com.darami.wagateway.domain.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/messages")

public class MessageController {
 
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<SendResponseBody> send(@RequestBody SendRequestBody body) {
        var message = new OutboundMessage(body.to(), body.templateName(), body.languageCode());
        String messageId = messageService.sendTemplateMessage(message);
        return ResponseEntity.ok(new SendResponseBody(messageId));
    }

    public record SendRequestBody(String to, String templateName, String languageCode) {
    }

    public record SendResponseBody(String messageId) {
    }
    
}
