
package edu.ucaldas.cinereserve.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.ucaldas.cinereserve.model.Funcion;

import java.util.List;

@RestController
@RequestMapping("/api/funciones")
public class FuncionController {

    private final List<Funcion> funciones = List.of(
        new Funcion(1L, "Interestelar", "15:00", 18000),
        new Funcion(2L, "Dune: Parte Dos", "18:30", 22000),
        new Funcion(3L, "Intensamente 2", "20:00", 16000)
    );

    
}
