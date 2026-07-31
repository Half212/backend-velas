package com.velassj.backend.api.security;

import com.velassj.backend.domain.entity.User;
import com.velassj.backend.domain.repositories.UserRepository;
import com.velassj.backend.domain.service.AuditService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AdminAuditFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        // Apenas para métodos que alteram dados
        if (method.equals("POST") || method.equals("PUT") || method.equals("PATCH") || method.equals("DELETE")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                
                boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                if (isAdmin) {
                    String confirmPassword = request.getHeader("X-Admin-Password");

                    if (confirmPassword == null || confirmPassword.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Administrador: É necessário informar o header X-Admin-Password para realizar alterações.");
                        return;
                    }

                    User adminUser = userRepository.findByEmail(auth.getName()).orElse(null);

                    if (adminUser == null || !passwordEncoder.matches(confirmPassword, adminUser.getPassword())) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Administrador: Senha de confirmação inválida.");
                        return;
                    }

                    // Se passou, registra a auditoria e continua
                    auditService.logAdminAction(adminUser, "Método " + method + " em " + request.getRequestURI(), request.getRequestURI());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
