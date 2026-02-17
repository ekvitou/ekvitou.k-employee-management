//package com.ekvitou.employee_management.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtConverterConfigure implements Converter<Jwt, AbstractAuthenticationToken> {
//
//    private static final Logger log = LoggerFactory.getLogger(JwtConverterConfigure.class);
//
//    @Override
//    public AbstractAuthenticationToken convert(Jwt jwt) {
//
//        Collection<GrantedAuthority> authorities = extractRoles(jwt);
//
//        String username = jwt.getClaimAsString("preferred_username");
//        if (username == null || username.isBlank()) {
//            username = jwt.getSubject();
//        }
//        return new JwtAuthenticationToken(jwt, authorities, username);
//    }
//
//    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
//        try {
//            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
//            if (resourceAccess == null) return Collections.emptySet();
//
//            Map<String, Object> appAccess =
//                    (Map<String, Object>) resourceAccess.get("spring-boot");
//            if (appAccess == null) return Collections.emptySet();
//
//            List<String> roles = (List<String>) appAccess.get("roles");
//            if (roles == null) return Collections.emptySet();
//
//            return roles.stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                    .collect(Collectors.toSet());
//
//        } catch (Exception e) {
//            log.warn("JWT role extraction failed: {}", e.getMessage());
//            return Collections.emptySet();
//        }
//    }
//}

package com.ekvitou.employee_management.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtConverterConfigure implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger log = LoggerFactory.getLogger(JwtConverterConfigure.class);

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractRoles(jwt);

        String username = jwt.getClaimAsString("preferred_username");
        if (username == null || username.isBlank()) {
            username = jwt.getSubject();
        }

        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        try {
            // Get resource_access map
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess == null) return Collections.emptySet();

            // Get roles for "spring-boot" client
            Map<String, Object> appAccess = (Map<String, Object>) resourceAccess.get("spring-boot");
            if (appAccess == null) return Collections.emptySet();

            List<String> roles = (List<String>) appAccess.get("roles");
            if (roles == null) return Collections.emptySet();

            // Add ROLE_ prefix to each role (required for @PreAuthorize)
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toSet());

        } catch (Exception e) {
            log.warn("JWT role extraction failed: {}", e.getMessage());
            return Collections.emptySet();
        }
    }
}
