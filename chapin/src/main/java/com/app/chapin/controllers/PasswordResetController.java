package com.app.chapin.controllers;

import com.app.chapin.persistence.models.PasswordResetToken;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.persistence.respository.PasswordResetTokenRepository;
import com.app.chapin.persistence.respository.UsuariosRepository;
import com.app.chapin.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    private UsuariosRepository usuariosRepository;

    private PasswordResetTokenRepository tokenRepository;

    private EmailService emailService;

    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        Optional<Usuario> usuario = usuariosRepository.findByEmail(email);
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Generate token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario.get());
        resetToken.setExpiracion(new Date(System.currentTimeMillis() + 3600000)); // 1 hour expiry

        tokenRepository.save(resetToken);

        // Send email
        // emailService.sendPasswordResetEmail(email, token);

        return ResponseEntity.ok("Password reset link has been sent to your email " + token);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
        if (!resetToken.isPresent() || resetToken.get().getExpiracion().before(new Date())) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        Usuario user = resetToken.get().getUsuario();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        usuariosRepository.save(user);

        // Optionally, delete the token after use
        tokenRepository.delete(resetToken.get());

        return ResponseEntity.ok("Password has been reset successfully");
    }
}
