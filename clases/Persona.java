package clases;

import java.io.Serializable;

public abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dni;
    private String nombre;
    private String apellido;

    public Persona(String dni, String nombre, String apellido) {
        validarDni(dni);
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    private void validarDni(String dni) {
        if (!dni.matches("\\d{8}[A-Za-z]")) {
            throw new DNIException("DNI incorrecto: " + dni);
        }
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        validarDni(dni);
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "Persona -> DNI: " + dni +
               " , Nombre: " + nombre +
               " , Apellido: " + apellido;
    }
}
