package com.serethewind.NeoClantech.securityConfig;

//import com.serethewind.Arkticles.entity.TokenEntity;
//import com.serethewind.Arkticles.exceptions.ResourceNotFoundException;
//import com.serethewind.Arkticles.repository.TokenRepository;
import com.serethewind.NeoClantech.entity.TokenEntity;
import com.serethewind.NeoClantech.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LogoutService implements LogoutHandler {

    private TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        String jwt = authHeader.substring(7);
        TokenEntity token  = tokenRepository.findByToken(jwt).orElseThrow();
//        TokenEntity token = tokenRepository.findByToken(jwt).orElseThrow(() -> new ResourceFoundException("Token not found"));
        if (token != null){
            token.setExpired(true);
            token.setRevoked(true);
            tokenRepository.save(token);
        }

    }
}
