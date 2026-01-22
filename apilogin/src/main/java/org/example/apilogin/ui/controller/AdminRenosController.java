package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Reno;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.domain.service.RenoService;
import org.example.apilogin.ui.interceptor.RequiresAuth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.API_ADMIN_RENOS)
public class AdminRenosController {
    private final RenoService renoService;

    public AdminRenosController(RenoService renoService) {
        this.renoService = renoService;
    }

    @GetMapping
    @RequiresAuth(rol = Rol.ADMIN)
    public ResponseEntity<List<Reno>> listarTodosLosRenos() {
        return ResponseEntity.ok(renoService.findAll());
    }

    @GetMapping(Constantes.API_RENO_BY_ID)
    @RequiresAuth(rol = Rol.ADMIN)
    public ResponseEntity<Reno> obtenerReno(@PathVariable int id) {
        Reno reno = renoService.findById(id);
        return ResponseEntity.ok(reno);
    }

    @GetMapping(Constantes.API_RENO_FILTRAR)
    @RequiresAuth(rol = Rol.ADMIN)
    public ResponseEntity<List<Reno>> filtrarRenos(@RequestParam String nombre) {
        List<Reno> renos = renoService.findNameLike(nombre);
        if (renos.isEmpty()) {
            return ResponseEntity.notFound().build();
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
    @RequiresAuth(rol = Rol.ADMIN)
    public ResponseEntity<Void> eliminarReno(@PathVariable int id) {
        renoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

