package com.mindhub.homeBanking.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests( ant ->
                ant.requestMatchers("/web/index.html", "/web/pages/login.html","/web/scripts/login.js",
                                "/web/scripts/index.js","/web/styles/**","/web/images/**", "/api/clients",
                                "/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clients","/api/login","/api/cards/pay").permitAll()
                .requestMatchers("/rest/**","/web/pages/manager.html",
                        "/web/scripts/manager.js").hasAuthority("ADMIN")
                        .requestMatchers("/web/pages/accounts.html","/web/pages/account.html/**",
                                "/web/pages/cards.html","/web/pages/create-cards.html"
                                , "/api/clients/current/accounts","/web/pages/transfers.html",
                                "/web/pages/loan-application.html","/web/pages/pay-loan.html",
                                "/web/scripts/loan-application.js", "/web/scripts/pay-loan.js",
                                "/web/scripts/account.js", "/api/accounts/{id}",
                                "/web/scripts/accounts.js","/web/scripts/cards.js",
                                "/web/scripts/transfers.js","/web/scripts/create-cards.js").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.GET,"/api/clients/current","/api/clients/current/accounts",
                                "/api/clients/current/cards", "/api/loans").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.POST,"/api/clients/current/cards",
                                "/api/clients/current/accounts", "/api/transactions",
                                "/api/loans", "/api/loans/pay").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/clients/current/cards/deactivate",
                                "/api/clients/current/accounts/deactivate").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.POST,"/api/loans/create").hasAuthority("ADMIN")
                        .requestMatchers("/web/pages/create-loan.html","/web/scripts/create-loan.js").hasAuthority("ADMIN")
                ).csrf(csrf ->
                        csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .exceptionHandling(excep -> excep.authenticationEntryPoint((req, res, exc) -> res.sendError(401)));
        http.formLogin(formLogin ->
                        formLogin.loginPage("/web/pages/login.html")
                                .loginProcessingUrl("/api/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .successHandler((req, res, auth) -> clearAuthenticationAttributes(req))
                                .failureHandler((req, res, exc) -> res.sendError(401)))
                        .logout(logout ->
                                logout.logoutUrl("/api/logout")
                                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                                        .deleteCookies("JSESSIONID"))
                .rememberMe(Customizer.withDefaults());
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOrigin("http://127.0.0.1:5500");
            configuration.addAllowedMethod("POST");
            configuration.addAllowedHeader("*");
            configuration.setAllowCredentials(true);

            return configuration;
        }));

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null){
            session.removeAttribute((WebAttributes.AUTHENTICATION_EXCEPTION));
        }
    }

}
