package clases;

import java.util.TreeMap;

public class Cliente extends Persona {

    private String telefono;
    private TreeMap<Integer, Reserva> reservas = new TreeMap<>();

    public Cliente(String dni, String nombre, String apellido, String telefono) {
        super(dni, nombre, apellido); // Persona ya valida el DNI
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void aniadirReserva(int id, Reserva reserva) {
        reservas.put(id, reserva);
    }

    public void eliminarReserva(int id) {
        if (reservas.remove(id) != null) {
            System.out.println("Reserva eliminada correctamente");
        } else {
            System.out.println("No existe una reserva con ese ID");
        }
    }
    public TreeMap<Integer, Reserva> getReservas() {
        return reservas;
    }

    public void listarReservas() {
        for (Reserva r : reservas.values()) {
            System.out.println(r);
        }
    }

    @Override
    public String toString() {
        return "Cliente -> " + super.toString() +
               ", Teléfono: " + telefono +
               ", Nº Reservas: " + reservas.size();
    }
}
