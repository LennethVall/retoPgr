package clases;

import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numeroReserva;
	private String dniCliente;
	private String modeloVehiculo;
	private LocalDate fechaInicio;
	private int dias;

	public Reserva(int numeroReserva, String dniCliente, String modeloVehiculo, LocalDate fechaInicio, int dias) {
		this.numeroReserva = numeroReserva;
		this.dniCliente = dniCliente;
		this.modeloVehiculo = modeloVehiculo;
		this.fechaInicio = fechaInicio;
		this.dias = dias;
	}

	// (metodo) Sumar inicio + dias para conseguir la fecha final

	public LocalDate getFechaFin() {
		return fechaInicio.plusDays(dias);
	}
	// (metod) calcular si la reserva esta en curso, finalizada o activa
	// ____________________________________________________________________
	// Como funciona: Si la fecha actual es ANTERIOR a la fecha del INICIO de la
	// reserva -> En reserva
	// Si la fecha actual es POSTERIOR a la fecha de FIN de la reserva -> Finalizada
	// Si no cumple ninguna de esas dos condiciones, POR DEFECTO será -> Activa

	public String getEstado() {
		LocalDate hoy = LocalDate.now();
		if (hoy.isBefore(fechaInicio))
			return "En reserva";
		if (hoy.isAfter(getFechaFin()))
			return "Finalizada";
		return "Activa";
	}

	public int getNumeroReserva() {
		return numeroReserva;
	}

	public void setNumeroReserva(int numeroReserva) {
		this.numeroReserva = numeroReserva;
	}

	public String getDniCliente() {
		return dniCliente;
	}

	public void setDniCliente(String dniCliente) {
		this.dniCliente = dniCliente;
	}

	public String getModeloVehiculo() {
		return modeloVehiculo;
	}

	public void setModeloVehiculo(String modeloVehiculo) {
		this.modeloVehiculo = modeloVehiculo;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

	// salida de datos de reserva

	@Override
	public String toString() {
		return "Reserva nº " + numeroReserva + " -> DNI: " + dniCliente + " , Modelo: " + modeloVehiculo + " , Inicio: "
				+ fechaInicio + " , Dias:" + dias;
	}

}