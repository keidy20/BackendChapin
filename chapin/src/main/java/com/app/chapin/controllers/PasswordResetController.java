package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.PasswordResetDto;
import com.app.chapin.persistence.models.PasswordResetToken;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.persistence.respository.PasswordResetTokenRepository;
import com.app.chapin.persistence.respository.UsuariosRepository;
import com.app.chapin.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        // Generar token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario.get());
        resetToken.setExpiracion(new Date(System.currentTimeMillis() + 3600000)); // 1 hour expiry

        tokenRepository.save(resetToken);

        // Envio de email
        emailService.sendPasswordResetEmail(email, token);

        return ResponseEntity.ok("El siguiente token se ha enviado a tu correo " + token);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto dto) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(dto.getToken());
        if (!resetToken.isPresent() || resetToken.get().getExpiracion().before(new Date())) {
            return ResponseEntity.badRequest().body("Invalido o el token expiro");
        }

        Usuario user = resetToken.get().getUsuario();
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getNewPassword()));
        usuariosRepository.save(user);

        // Opcional eliminar el token
        tokenRepository.delete(resetToken.get());

        return ResponseEntity.ok("La contraseña se cambio correctamente");
    }
}
