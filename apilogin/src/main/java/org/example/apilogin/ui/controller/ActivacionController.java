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
    private final UsuarioService usuarioService;

    public ActivacionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(Constantes.API_ACTIVAR_CUENTA)
    public String activarCuenta(@RequestParam String codigo, Model model) {
        try {
            Usuario usuario = usuarioService.activarCuenta(codigo);
            model.addAttribute(Constantes.PARAM_EXITOSO, true);
            model.addAttribute(Constantes.PARAM_MENSAJE, Constantes.MSG_CUENTA_ACTIVADA_EXITOSAMENTE);
            model.addAttribute(Constantes.PARAM_USUARIO, usuario.nombre());
        } catch (Exception e) {
            model.addAttribute(Constantes.PARAM_EXITOSO, false);
            model.addAttribute(Constantes.PARAM_MENSAJE, Constantes.MSG_ERROR_ACTIVACION);
        }
        return Constantes.TEMPLATE_ACTIVACION_RESULTADO;
    }
}
