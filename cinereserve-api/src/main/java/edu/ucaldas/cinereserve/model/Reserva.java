
package edu.ucaldas.cinereserve.model;

import java.util.List;

public class Reserva {

    private Long id;
    private Long funcionId;
    private List<String> asientos;
    private String nombreCliente;


    public Reserva(Long id, Long funcionId,
                   List<String> asientos,
                   String nombreCliente) {
        this.id = id;
        this.funcionId = funcionId;
        
        this.nombreCliente = nombreCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFuncionId() {
        return funcionId;
    }

    public void setFuncionId(Long funcionId) {
        this.funcionId = funcionId;
    }

    public List<String> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<String> asientos) {
        this.asientos = asientos;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
