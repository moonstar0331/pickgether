package com.capstone.pick.config;

import com.capstone.pick.security.VotePrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication);
        if(authentication.isEmpty() || !authentication.get().isAuthenticated()) return null;
        return () -> authentication
                .map(Authentication::getPrincipal)
                .map(VotePrincipal.class::cast)
                .map(VotePrincipal::getUsername);
    }
}
