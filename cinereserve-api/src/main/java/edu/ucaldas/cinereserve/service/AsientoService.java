package edu.ucaldas.cinereserve.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.ucaldas.cinereserve.model.Asiento;

@Service
public class AsientoService {

    private final Map<Long, List<Asiento>> asientosPorFuncion =
            new HashMap<>();

    public AsientoService() {
        for (long funcionId = 1; funcionId <= 3; funcionId++) {
            List<Asiento> asientos = new ArrayList<>();

            for (char fila = 'A'; fila <= 'C'; fila++) {
                for (int numero = 1; numero <= 5; numero++) {
                    boolean ocupado = fila == 'A' && numero == 3;
                    asientos.add(
                        new Asiento("" + fila + numero, ocupado)
                    );
                }
            }

            asientosPorFuncion.put(funcionId, asientos);
        }
    }

    public List<Asiento> listarPorFuncion(Long funcionId) {
        return asientosPorFuncion.get(funcionId);
    }
}
