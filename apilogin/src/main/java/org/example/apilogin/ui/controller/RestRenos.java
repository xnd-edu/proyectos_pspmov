package org.example.apilogin.ui.controller;

import jakarta.servlet.http.HttpSession;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Reno;
import org.example.apilogin.domain.service.RenoService;
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
    public ResponseEntity<List<Reno>> listarRenos(HttpSession session) {
        if (authService.isAuthenticated(session)) {
           if (authService.isAdmin(session)) {
               return ResponseEntity.ok(renoService.findAll());
           } else {
               int userId = authService.getUsuarioIdFromSession(session).intValue();
               return ResponseEntity.ok(renoService.findByUserId(userId));
           }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(Constantes.API_RENO_BY_ID)
    public ResponseEntity<Reno> obtenerReno(@PathVariable int id, HttpSession session) {
        if (authService.isAuthenticated(session)) {
            Reno reno = renoService.findById(id);
            if (authService.isAdmin(session)) {
                return ResponseEntity.ok(reno);
            } else {
                if (reno == null) {
                    return ResponseEntity.notFound().build();
                }
                int userId = authService.getUsuarioIdFromSession(session).intValue();
                if (reno.userId() == userId) {
                    return ResponseEntity.ok(reno);
                } else  {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(Constantes.API_RENO_FILTRAR)
    public ResponseEntity<List<Reno>> filtroReno(@RequestParam String nombre, HttpSession session) {
        if (authService.isAuthenticated(session)) {
            List<Reno> renos = renoService.findNameLike(nombre);
            if (authService.isAdmin(session)) {
                return ResponseEntity.ok(renos);
            } else {
                int userId = authService.getUsuarioIdFromSession(session).intValue();
                List<Reno> renosFiltrados = renos.stream()
                        .filter(reno -> reno.userId() == userId)
                        .toList();
                return ResponseEntity.ok(renosFiltrados);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PostMapping
    public ResponseEntity<Reno> crearReno(@RequestBody Reno reno, HttpSession session) {
        if (authService.isAuthenticated(session)) {
            if (authService.isAdmin(session)) {
                Reno newReno = renoService.save(reno);
                return ResponseEntity.status(HttpStatus.CREATED).body(newReno);
            }
            else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping(Constantes.API_RENO_UPDATE)
    public ResponseEntity<Reno> actualizarReno(@PathVariable int id, @RequestBody Reno reno, HttpSession session) {
        if (authService.isAuthenticated(session)) {
            if (authService.isAdmin(session)) {
                Reno updatedReno = renoService.update(id, reno);
                if (updatedReno != null) {
                    return ResponseEntity.ok(updatedReno);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping(Constantes.API_RENO_DELETE)
    public ResponseEntity<Void> eliminarReno(@PathVariable int id, HttpSession session) {
        if (authService.isAuthenticated(session)) {
            if (authService.isAdmin(session)) {
                if (renoService.delete(id)) {
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                int userId = authService.getUsuarioIdFromSession(session).intValue();
                Reno reno = renoService.findById(id);
                if (reno == null) {
                    return ResponseEntity.notFound().build();
                }
                if (reno.userId() == userId) {
                    if (renoService.delete(id)) {
                        return ResponseEntity.noContent().build();
                    }
                } else  {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
