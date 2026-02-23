package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Reno;
import org.example.apilogin.domain.service.RenoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<List<Reno>> listarMisRenos(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(renoService.findByUserId(userId.intValue()));
    }

    @GetMapping(Constantes.API_BY_ID)
    public ResponseEntity<Reno> obtenerReno(@PathVariable int id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Reno reno = renoService.findById(id);

        if (reno.userId() != userId.intValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(reno);
    }

    @GetMapping(Constantes.API_RENO_FILTRAR)
    public ResponseEntity<List<Reno>> filtrarMisRenos(
            @RequestParam String nombre,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        List<Reno> renos = renoService.findNameLike(nombre);

        List<Reno> renosFiltrados = renos.stream()
                .filter(reno -> reno.userId() == userId.intValue())
                .toList();

        if (renosFiltrados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(renosFiltrados);
    }

    @DeleteMapping(Constantes.API_BY_ID)
    public ResponseEntity<Void> eliminarReno(@PathVariable int id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Reno reno = renoService.findById(id);

        if (reno.userId() != userId.intValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        renoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

