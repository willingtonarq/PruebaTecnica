
package edu.ucaldas.cinereserve;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CineReserveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void debeConsultarFunciones() throws Exception {
        mockMvc.perform(get("/api/funciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].pelicula").exists())
                .andExpect(jsonPath("$[0].horario").exists())
                .andExpect(jsonPath("$[0].precio").exists());
    }

    @Test
    void debeConsultarAsientosDeFuncion() throws Exception {
        mockMvc.perform(get("/api/funciones/1/asientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].codigo").exists())
                .andExpect(jsonPath("$[0].ocupado").exists());
    }

    @Test
    void debeCrearReserva() throws Exception {
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "funcionId": 1,
                              "asientos": ["B1"],
                              "nombreCliente": "Prueba de integración"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.funcionId").value(1))
                .andExpect(jsonPath("$.nombreCliente")
                        .value("Prueba de integración"))
                .andExpect(jsonPath("$.asientos[0]").value("B1"));
    }

    @Test
    void debeRechazarReservaSinNombre() throws Exception {
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "funcionId": 1,
                              "asientos": ["B2"],
                              "nombreCliente": " "
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRechazarReservaSinAsientos() throws Exception {
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "funcionId": 1,
                              "asientos": [],
                              "nombreCliente": "Cliente"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRechazarAsientoInvalido() throws Exception {
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "funcionId": 1,
                              "asientos": ["Z99"],
                              "nombreCliente": "Cliente"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRechazarAsientoOcupado() throws Exception {
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "funcionId": 1,
                              "asientos": ["A3"],
                              "nombreCliente": "Cliente"
                            }
                            """))
                .andExpect(status().isConflict());
    }

    @Test
    void debeRechazarAsientosRepetidos() throws Exception {
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "funcionId": 1,
                              "asientos": ["B3", "B3"],
                              "nombreCliente": "Cliente"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeConsultarReservaInexistente() throws Exception {
        mockMvc.perform(get("/api/reservas/999999"))
                .andExpect(status().isNotFound());
    }
}