package com.velassj.backend.api.security;

import com.velassj.backend.domain.entity.User;
import com.velassj.backend.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Value("${app.cors.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()))
            );
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            .csrf(AbstractHttpConfigurer::disable) // Desabilitado para APIs REST simples
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll() // Comum pode ver
                .requestMatchers(HttpMethod.POST, "/api/products/**").permitAll() // Cadastro de produtos pelo portal
                .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll() // Alteração de produtos pelo portal
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").permitAll() // Exclusão de produtos pelo portal
                .requestMatchers("/api/categories/**").permitAll() // Consulta e criação de categorias
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Cadastro
                .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll() // Login
                .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_SUPERVISOR", "ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults()); // Utilizando Basic Auth para simplificar a autenticação no momento

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedPatterns = new ArrayList<>();
        
        // Padrões padrão para desenvolvimento local e deploys (Vercel e Render)
        allowedPatterns.add("http://localhost:*");
        allowedPatterns.add("http://127.0.0.1:*");
        allowedPatterns.add("https://*.vercel.app");
        allowedPatterns.add("https://*.onrender.com");
        allowedPatterns.add("https://frontend-veleas.vercel.app");
        allowedPatterns.add("https://frontend-velas.vercel.app");

        // Suporta FRONTEND_URL individual ou lista separada por vírgulas
        if (frontendUrl != null && !frontendUrl.isBlank()) {
            String[] origins = frontendUrl.split(",");
            for (String origin : origins) {
                String trimmed = origin.trim();
                if (!trimmed.isEmpty() && !allowedPatterns.contains(trimmed)) {
                    allowedPatterns.add(trimmed);
                }
            }
        }

        configuration.setAllowedOriginPatterns(allowedPatterns);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "X-Total-Count"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
