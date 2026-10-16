
package edu.ucaldas.cinereserve.controllers;

import edu.ucaldas.cinereserve.model.Asiento;
import edu.ucaldas.cinereserve.service.AsientoService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/funciones")
public class AsientoController {

    private final AsientoService asientoService;

    public AsientoController(AsientoService asientoService) {
        this.asientoService = asientoService;
    }

    @GetMapping("/{funcionId}/asientos")
    public List<Asiento> listarAsientos(
            @PathVariable Long funcionId) {

        
        if (asientos == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La función no existe"
            );
        }

        return asientos;
    }
}
