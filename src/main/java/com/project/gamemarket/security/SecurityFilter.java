package com.project.gamemarket.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.HeaderBearerTokenResolver;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.project.gamemarket.util.SecurityHeader.API_KEY_HEADER;

    public class SecurityFilter extends OncePerRequestFilter{

        private final BearerTokenResolver tokenResolver = new HeaderBearerTokenResolver(API_KEY_HEADER);

        private final BearerTokenAuthenticationEntryPoint entryPoint = new BearerTokenAuthenticationEntryPoint();
        private final AuthenticationFailureHandler failureHandler = new AuthenticationEntryPointFailureHandler(entryPoint);
        private final AuthenticationProvider provider;

        public SecurityFilter(JwtDecoder jwtDecoder) {
            this.provider = new JwtAuthenticationProvider(jwtDecoder);
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String token;

            try {
                token = this.tokenResolver.resolve(request);
                if (token == null) {
                    handleFailure(request, response, "No bearer token present", null);
                    return;
                } else if (token.isBlank()) {
                    handleFailure(request, response, "Bearer token is empty", null);
                    return;
                }

                Authentication authenticationRequest = new BearerTokenAuthenticationToken(token);
                Authentication authenticationResult = provider.authenticate(authenticationRequest);

                if (!authenticationResult.isAuthenticated()) {
                    handleFailure(request, response, "Unauthenticated", null);
                } else {
                    this.logger.trace("Successfully authenticated: " + authenticationResult);
                    filterChain.doFilter(request, response);
                }
            } catch (OAuth2AuthenticationException ex) {
                handleFailure(request, response, "Failed to resolve or validate bearer token", ex);
            } catch (AuthenticationException ex) {
                handleFailure(request, response, "Failed to process authentication", ex);
            }
        }

        private void handleFailure(HttpServletRequest request, HttpServletResponse response, String message, Exception ex)
                throws IOException, ServletException {
            if (ex != null) {
                this.logger.trace(message, ex);
                if (ex instanceof OAuth2AuthenticationException) {
                    this.entryPoint.commence(request, response, (OAuth2AuthenticationException) ex);
                } else if (ex instanceof AuthenticationException) {
                    this.failureHandler.onAuthenticationFailure(request, response, (AuthenticationException) ex);
                }
            } else {
                this.logger.trace(message);
                this.entryPoint.commence(request, response, null);
            }
        }
}
