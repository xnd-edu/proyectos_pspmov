package org.example.apilogin.ui.security.jwt;

import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UsuarioService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioService.findByUsername(username);

        return User.builder()
                .username(user.id().toString())
                .password(user.password())
                .roles(user.rol().toString())
                .build();
    }
}
