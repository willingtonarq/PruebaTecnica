
package edu.ucaldas.cinereserve.service;

import edu.ucaldas.cinereserve.model.Asiento;
import edu.ucaldas.cinereserve.model.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReservaServiceTest {

    @Mock
    private AsientoService asientoService;

    private ReservaService reservaService;

    private Asiento a1;
    private Asiento a2;
    private Asiento a3;

    @BeforeEach
    void configurar() {
        reservaService = new ReservaService(asientoService);

        a1 = mock(Asiento.class);
        when(a1.getCodigo()).thenReturn("A1");
        when(a1.isOcupado()).thenReturn(false);

        a2 = mock(Asiento.class);
        when(a2.getCodigo()).thenReturn("A2");
        when(a2.isOcupado()).thenReturn(false);

        a3 = mock(Asiento.class);
        when(a3.getCodigo()).thenReturn("A3");
        when(a3.isOcupado()).thenReturn(true);

        when(asientoService.listarPorFuncion(1L))
                .thenReturn(List.of(a1, a2, a3));
    }

    private Reserva solicitud(List<String> codigos, String nombre) {
        return new Reserva(null, 1L, codigos, nombre);
    }

    private void esperarError(HttpStatus estado, Reserva solicitud) {
        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> reservaService.crear(solicitud)
        );

        assertEquals(estado, error.getStatusCode());
    }

    @Test
    void creaReservaValida() {
        Reserva resultado = reservaService.crear(
                solicitud(List.of("A1", "A2"), "Ana Pérez")
        );

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getFuncionId());
        assertEquals("Ana Pérez", resultado.getNombreCliente());

        verify(a1).setOcupado(true);
        verify(a2).setOcupado(true);
        verify(a3, never()).setOcupado(true);
    }

    @Test
    void eliminaEspaciosDelNombreCliente() {
        Reserva resultado = reservaService.crear(
                solicitud(List.of("A1"), "  Ana Pérez  ")
        );

        assertEquals("Ana Pérez", resultado.getNombreCliente());
    }

    @Test
    void rechazaSolicitudNula() {
        esperarError(HttpStatus.BAD_REQUEST, null);
    }

    @Test
    void rechazaNombreVacio() {
        esperarError(
                HttpStatus.BAD_REQUEST,
                solicitud(List.of("A1"), "   ")
        );
    }

    @Test
    void rechazaListaDeAsientosVacia() {
        esperarError(
                HttpStatus.BAD_REQUEST,
                solicitud(List.of(), "Ana")
        );

        verify(asientoService, never()).listarPorFuncion(anyLong());
    }

    @Test
    void rechazaFuncionInexistente() {
        when(asientoService.listarPorFuncion(99L)).thenReturn(null);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> reservaService.crear(
                        new Reserva(null, 99L, List.of("A1"), "Ana")
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(a1, never()).setOcupado(true);
    }

    @Test
    void rechazaAsientoInvalido() {
        esperarError(
                HttpStatus.BAD_REQUEST,
                solicitud(List.of("Z99"), "Ana")
        );

        verify(a1, never()).setOcupado(true);
        verify(a2, never()).setOcupado(true);
    }

    @Test
    void rechazaAsientoOcupado() {
        esperarError(
                HttpStatus.CONFLICT,
                solicitud(List.of("A3"), "Ana")
        );

        verify(a3, never()).setOcupado(true);
    }

    @Test
    void rechazaCodigosRepetidos() {
        esperarError(
                HttpStatus.BAD_REQUEST,
                solicitud(List.of("A1", "A1"), "Ana")
        );

        verify(a1, never()).setOcupado(true);
    }

    @Test
    void reservaFallidaNoOcupaParcialmenteLosAsientos() {
        // A1 está disponible, pero A3 ya está ocupado.
        esperarError(
                HttpStatus.CONFLICT,
                solicitud(List.of("A1", "A3"), "Ana")
        );

        verify(a1, never()).setOcupado(true);
        verify(a2, never()).setOcupado(true);
    }

    @Test
    void permiteConsultarUnaReservaCreada() {
        Reserva creada = reservaService.crear(
                solicitud(List.of("A1"), "Ana")
        );

        Reserva consultada = reservaService.buscar(creada.getId());

        assertSame(creada, consultada);
    }

    @Test
    void rechazaConsultaDeReservaInexistente() {
        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> reservaService.buscar(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    
  @Test
  void dosSolicitudesConcurrentesNoReservanElMismoAsiento()
          throws Exception {

      // Un asiento real conserva el estado ocupado entre solicitudes.
      Asiento asientoReal = new Asiento("A1", false);

      when(asientoService.listarPorFuncion(1L))
              .thenReturn(List.of(asientoReal));

      ExecutorService executor = Executors.newFixedThreadPool(2);
      CountDownLatch inicio = new CountDownLatch(1);

      try {
          Callable<Boolean> intento = () -> {
              inicio.await();

              try {
                  reservaService.crear(
                          solicitud(List.of("A1"), "Cliente")
                  );
                  return true;
              } catch (ResponseStatusException ex) {
                  return false;
              }
          };

          Future<Boolean> primero = executor.submit(intento);
          Future<Boolean> segundo = executor.submit(intento);

          inicio.countDown();

          boolean resultado1 = primero.get(5, TimeUnit.SECONDS);
          boolean resultado2 = segundo.get(5, TimeUnit.SECONDS);

          // Exactamente una solicitud debe ganar.
          assertNotEquals(resultado1, resultado2);
          assertTrue(asientoReal.isOcupado());

      } finally {
          executor.shutdownNow();
      }
  }

}
