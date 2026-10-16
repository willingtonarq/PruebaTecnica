
package edu.ucaldas.cinereserve.service;

import edu.ucaldas.cinereserve.model.Asiento;
import edu.ucaldas.cinereserve.model.Reserva;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ReservaService {

    private final AsientoService asientoService;
    private final Map<Long, Reserva> reservas = new HashMap<>();
    private long siguienteId = 1;

    public ReservaService(AsientoService asientoService) {
        this.asientoService = asientoService;
    }

    public synchronized Reserva crear(Reserva solicitud) {
        if (solicitud == null
                || solicitud.getFuncionId() == null
                || solicitud.getAsientos() == null
                || solicitud.getAsientos().isEmpty()
                || solicitud.getNombreCliente() == null
                || solicitud.getNombreCliente().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La solicitud está incompleta"
            );
        }

        List<Asiento> disponibles =
                asientoService.listarPorFuncion(
                        solicitud.getFuncionId());

        if (disponibles == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La función no existe"
            );
        }

        Set<String> solicitados =
                new HashSet<>(solicitud.getAsientos());

        if (solicitados.size() != solicitud.getAsientos().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede repetir un asiento"
            );
        }

        Map<String, Asiento> porCodigo = new HashMap<>();
        for (Asiento asiento : disponibles) {
            porCodigo.put(asiento.getCodigo(), asiento);
        }

        // Validar todos los asientos antes de modificar su estado.
        

        // Solo después de validar todo, confirmar la reserva.
        
        Reserva reserva = new Reserva(
                siguienteId++,
                solicitud.getFuncionId(),
                new ArrayList<>(solicitados),
                solicitud.getNombreCliente().trim()
        );

        reservas.put(reserva.getId(), reserva);
        return reserva;
    }

    public Reserva buscar(Long id) {
        Reserva reserva = reservas.get(id);

        if (reserva == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Reserva no encontrada"
            );
        }

        return reserva;
    }
}
