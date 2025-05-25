package com.presto.server.infra.redis.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
        @NotBlank String host,
        @NotNull int port,
        @NotBlank String password,
        @NotNull Ssl ssl
) {

    public record Ssl(@NotNull boolean enabled) {
    }
}
