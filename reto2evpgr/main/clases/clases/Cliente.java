package clases;

import java.util.ArrayList;
import java.util.TreeMap;

public class Cliente extends Persona {

	private String telefono;
	TreeMap<Integer,Reserva> reservas=new TreeMap<Integer,Reserva>();
	public Cliente(String dni, String nombre, String apellido, String telefono2) {
		super(dni, nombre, apellido);
		 if (!dni.matches("\\d{8}[A-Za-z]")) {
	            throw new DNIException("DNI incorrecto: " + dni);
	        }
		this.telefono=telefono2;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String string) {
		this.telefono = string;
	}
	public void aniadirReserva(int i,Reserva a) {
		reservas.put(i,a);
	}
	public void eliminarReserva(int n) {
			reservas.remove(n);
			System.out.println("Reserva eliminada correctamente");
	}
	public void listarReservas() {
		for(Reserva a : reservas.values()) {
			System.out.println(a.toString());
		}
	}
	@Override
	public String toString() {
		return super.toString()+".Cliente [telefono=" + telefono + "]";
	}

}