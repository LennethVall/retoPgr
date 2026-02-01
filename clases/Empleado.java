package clases;

public class Empleado extends Persona {

    private Cargo cargo;

    public Empleado(String dni, String nombre, String apellido, Cargo cargo) {
        super(dni, nombre, apellido);
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "Empleado -> " +
               "DNI: " + getDni() +
               ", Nombre: " + getNombre() +
               ", Apellido: " + getApellido() +
               ", Cargo: " + cargo;
    }
}
