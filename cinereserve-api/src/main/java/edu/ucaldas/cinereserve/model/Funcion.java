package edu.ucaldas.cinereserve.model;

public class Funcion {
    private Long id;
    private String pelicula;
    private String horario;
    private double precio;

    public Funcion(Long id, String pelicula, String horario, double precio) {
        this.id = id;
        this.pelicula = pelicula;
        
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public String getPelicula() {
        return pelicula;
    }

    public String getHorario() {
        return horario;
    }

    public double getPrecio() {
        return precio;
    }
}
