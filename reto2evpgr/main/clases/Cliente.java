package clases;

import java.util.ArrayList;

public class Cliente extends Persona {

	private int telefono;
	ArrayList<Reserva> reservas=new ArrayList<Reserva>();
	public Cliente(String dni, String nombre, String apellido, int telefono) {
		super(dni, nombre, apellido);
		 if (!dni.matches("\\d{8}[A-Za-z]")) {
	            throw new DNIException("DNI incorrecto: " + dni);
	        }
		this.telefono=telefono;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	public void aniadirReserva(Reserva a) {
		reservas.add(a);
	}
	public void eliminarReserva(Reserva a) {
		reservas.remove(a);
	}
	public void listarReservas() {
		for(Reserva a : reservas) {
			System.out.println(a.toString());
		}
	}
	@Override
	public String toString() {
		return super.toString()+".Cliente [telefono=" + telefono + "]";
	}

}