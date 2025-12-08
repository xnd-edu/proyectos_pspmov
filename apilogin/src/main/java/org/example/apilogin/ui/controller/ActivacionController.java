package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivacionController {
    private UsuarioService usuarioService;

    public ActivacionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(Constantes.API_ACTIVAR_CUENTA)
    public String activarCuenta(@RequestParam String codigo, Model model) {
        try {
            Usuario usuario = usuarioService.activarCuenta(codigo);
            model.addAttribute("exitoso", true);
            model.addAttribute("mensaje", "¡Cuenta activada exitosamente!");
            model.addAttribute("usuario", usuario.nombre());
        } catch (Exception e) {
            model.addAttribute("exitoso", false);
            model.addAttribute("mensaje", "Error al activar la cuenta. El código puede ser inválido o ya ha expirado.");
        }
        return "activacion-resultado";
    }
}
