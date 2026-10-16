
package edu.ucaldas.cinereserve.controller;

import edu.ucaldas.cinereserve.model.Reserva;
import edu.ucaldas.cinereserve.service.ReservaService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva crear(@RequestBody Reserva solicitud) {
        return reservaService.crear(solicitud);
    }

    @GetMapping("/{id}")
    public Reserva consultar(@PathVariable Long id) {
        return reservaService.buscar(id);
    }
}
