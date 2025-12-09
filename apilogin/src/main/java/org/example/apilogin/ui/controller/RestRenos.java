package org.example.apilogin.ui.controller;

import jakarta.servlet.http.HttpSession;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Reno;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.domain.service.RenoService;
import org.example.apilogin.ui.interceptor.RequiresAuth;
import org.example.apilogin.ui.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.API_BASE_RENOS)
public class RestRenos {
    private final RenoService renoService;
    private final AuthService authService;

    public RestRenos(RenoService renoService, AuthService authService) {
        this.renoService = renoService;
        this.authService = authService;
    }

    @GetMapping
    @RequiresAuth
    public ResponseEntity<List<Reno>> listarRenos(HttpSession session) {
       if (authService.isUser(session)) {
           int userId = authService.getUsuarioIdFromSession(session).intValue();
           return ResponseEntity.ok(renoService.findByUserId(userId));
       }
        return ResponseEntity.ok(renoService.findAll());
    }

    @GetMapping(Constantes.API_RENO_BY_ID)
    @RequiresAuth
    public ResponseEntity<Reno> obtenerReno(@PathVariable int id, HttpSession session) {
        Reno reno = renoService.findById(id);

        if (authService.isUser(session)) {
            int userId = authService.getUsuarioIdFromSession(session).intValue();
            if (reno.userId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.ok(reno);
    }

    @GetMapping(Constantes.API_RENO_FILTRAR)
    @RequiresAuth
    public ResponseEntity<List<Reno>> filtroReno(@RequestParam String nombre, HttpSession session) {
        List<Reno> renos = renoService.findNameLike(nombre);
        if (renos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (authService.isUser(session)) {
            int userId = authService.getUsuarioIdFromSession(session).intValue();
            List<Reno> renosFiltrados = renos.stream()
                    .filter(reno -> reno.userId() == userId)
                    .toList();
            return ResponseEntity.ok(renosFiltrados);
        }
        return ResponseEntity.ok(renos);
    }


    @PostMapping
    @RequiresAuth(rol = Rol.ADMIN)
    public ResponseEntity<Reno> crearReno(@RequestBody Reno reno) {
        Reno newReno = renoService.save(reno);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReno);
    }

    @PutMapping(Constantes.API_RENO_UPDATE)
    @RequiresAuth(rol = Rol.ADMIN)
    public ResponseEntity<Reno> actualizarReno(@PathVariable int id, @RequestBody Reno reno) {
        Reno updatedReno = renoService.update(id, reno);
        return ResponseEntity.ok(updatedReno);
    }

    @DeleteMapping(Constantes.API_RENO_DELETE)
    @RequiresAuth
    public ResponseEntity<Void> eliminarReno(@PathVariable int id, HttpSession session) {
        Reno reno = renoService.findById(id);

        if (authService.isUser(session)) {
            int userId = authService.getUsuarioIdFromSession(session).intValue();
            if (reno.userId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        renoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
