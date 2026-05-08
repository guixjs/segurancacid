package com.estudos.backend.service;

import com.estudos.backend.dto.RegistrarUSuarioRequest;
import com.estudos.backend.exception.UsernameNotFoundException;
import com.estudos.backend.exception.UsuarioJaExisteException;
import com.estudos.backend.model.User;
import com.estudos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public User registrarUsuario(RegistrarUSuarioRequest dto){
        if(repository.findByUsername(dto.username())!= null){
            throw new UsuarioJaExisteException("Username já existe");
        }

        String senha = passwordEncoder.encode(dto.senha());
        var usuario = new User(dto.nome(), dto.username(), senha);

        return repository.save(usuario);
    }
}
