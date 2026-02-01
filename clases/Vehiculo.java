package clases;

import java.io.Serializable;

public abstract class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String matricula;
    private String marca;
    private String modelo;

    public Vehiculo(String matricula, String marca, String modelo) {

        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("La matrícula no puede estar vacía");
        }

        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("La matrícula no puede estar vacía");
        }
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "Vehículo -> Matrícula: " + matricula +
               ", Marca: " + marca +
               ", Modelo: " + modelo;
    }
}

