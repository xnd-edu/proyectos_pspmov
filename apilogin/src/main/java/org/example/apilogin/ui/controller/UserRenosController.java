package org.example.apilogin.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Reno;
import org.example.apilogin.domain.service.RenoService;
import org.example.apilogin.ui.interceptor.RequiresAuth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.API_USER_RENOS)
public class UserRenosController {
    private final RenoService renoService;

    public UserRenosController(RenoService renoService) {
        this.renoService = renoService;
    }

    @GetMapping
    @RequiresAuth
    public ResponseEntity<List<Reno>> listarMisRenos(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constantes.ATTR_USER_ID);
        return ResponseEntity.ok(renoService.findByUserId(userId.intValue()));
    }

    @GetMapping(Constantes.API_RENO_BY_ID)
    @RequiresAuth
    public ResponseEntity<Reno> obtenerReno(@PathVariable int id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constantes.ATTR_USER_ID);
        Reno reno = renoService.findById(id);

        if (reno.userId() != userId.intValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(reno);
    }

    @GetMapping(Constantes.API_RENO_FILTRAR)
    @RequiresAuth
    public ResponseEntity<List<Reno>> filtrarMisRenos(
            @RequestParam String nombre,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute(Constantes.ATTR_USER_ID);
        List<Reno> renos = renoService.findNameLike(nombre);

        List<Reno> renosFiltrados = renos.stream()
                .filter(reno -> reno.userId() == userId.intValue())
                .toList();

        if (renosFiltrados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(renosFiltrados);
    }

    @DeleteMapping(Constantes.API_RENO_DELETE)
    @RequiresAuth
    public ResponseEntity<Void> eliminarReno(@PathVariable int id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constantes.ATTR_USER_ID);
        Reno reno = renoService.findById(id);

        if (reno.userId() != userId.intValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        renoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

