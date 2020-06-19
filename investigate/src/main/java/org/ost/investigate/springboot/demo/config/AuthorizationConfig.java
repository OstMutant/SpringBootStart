package org.ost.investigate.springboot.demo.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "authentication")
public class AuthorizationConfig {
    private String endpoint;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
}
