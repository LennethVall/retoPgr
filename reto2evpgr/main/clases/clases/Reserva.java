package clases;

import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable{
	/**
	 * 
	 */
	private static int nreservas=100;
	private static final long serialVersionUID = 1L;
	private String dnicli, modelo;
	private int nreserva;
	private LocalDate fechaini,fechafin;
	
	public Reserva(String dnicli, String modelo, LocalDate fechaini, LocalDate fechafin) {
		this.nreserva=nreservas;
		this.dnicli=dnicli;
		this.modelo=modelo;
		this.fechaini=fechaini;
		this.fechafin=fechafin;
		nreservas++;
	}

	public String getDnicli() {
		return dnicli;
	}

	public void setDnicli(String dnicli) {
		this.dnicli = dnicli;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public int getNreserva() {
		return nreserva;
	}

	public void setNreserva(int nreserva) {
		this.nreserva = nreserva;
	}

	public LocalDate getFechaini() {
		return fechaini;
	}

	public void setFechaini(LocalDate fechaini) {
		this.fechaini = fechaini;
	}

	public LocalDate getFechafin() {
		return fechafin;
	}

	public void setFechafin(LocalDate fechafin) {
		this.fechafin = fechafin;
	}

	@Override
	public String toString() {
		return "Reserva [dnicli=" + dnicli + ", modelo=" + modelo + ", nreserva=" + nreserva + ", fechaini=" + fechaini
				+ ", fechafin=" + fechafin + "]";
	}
	
}