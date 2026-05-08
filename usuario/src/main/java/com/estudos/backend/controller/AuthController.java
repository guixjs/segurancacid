package com.estudos.backend.controller;

import com.estudos.backend.config.security.TokenService;
import com.estudos.backend.dto.LoginUsuarioRequest;
import com.estudos.backend.dto.RegistrarUSuarioRequest;
import com.estudos.backend.dto.TokenResponseDTO;
import com.estudos.backend.dto.UsuarioResponseDTO;
import com.estudos.backend.model.User;
import com.estudos.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthService authenticationService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginUsuarioRequest dto) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(),dto.senha()));
        var usuario = (User) auth.getPrincipal();
        var token = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody RegistrarUSuarioRequest dto){
        var usuario = authService.registrarUsuario(dto);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario.getId(), usuario.getName(), usuario.getUsername(), usuario.getPassword()));
    }
}
