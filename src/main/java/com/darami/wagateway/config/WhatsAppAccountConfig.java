package com.darami.wagateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Single-tenant account config. When multi-account support becomes real
 * (post 2-3 client integrations), this becomes a lookup instead of a
 * single injected bean — call sites that already take an account
 * parameter won't need to change.
 */
@Component
public record WhatsAppAccountConfig(
        @Value("${meta.access-token}") String accessToken,
        @Value("${meta.phone-number-id}") String phoneNumberId,
        @Value("${meta.app-secret}") String appSecret
) {
}