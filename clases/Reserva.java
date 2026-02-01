package clases;

import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;
    

    private String dnicli;
    private String modelo;
    private int nreserva;
    private LocalDate fechaini;
    private LocalDate fechafin;

    public Reserva(int nreserva,String dnicli, String modelo, LocalDate fechaini, LocalDate fechafin) {

        if (!dnicli.matches("\\d{8}[A-Za-z]")) {
            throw new DNIException("DNI de cliente incorrecto: " + dnicli);
        }

        if (fechaini == null || fechafin == null || fechaini.isAfter(fechafin)) {
            throw new IllegalArgumentException("Fechas de reserva inválidas");
        }

        this.nreserva = nreserva;
        this.dnicli = dnicli;
        this.modelo = modelo;
        this.fechaini = fechaini;
        this.fechafin = fechafin;
    }

    public String getDnicli() {
        return dnicli;
    }

    public void setDnicli(String dnicli) {
        if (!dnicli.matches("\\d{8}[A-Za-z]")) {
            throw new DNIException("DNI de cliente incorrecto: " + dnicli);
        }
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

    public LocalDate getFechaini() {
        return fechaini;
    }

    public void setFechaini(LocalDate fechaini) {
        if (fechaini.isAfter(this.fechafin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la de fin");
        }
        this.fechaini = fechaini;
    }

    public LocalDate getFechafin() {
        return fechafin;
    }

    public void setFechafin(LocalDate fechafin) {
        if (fechafin.isBefore(this.fechaini)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
        this.fechafin = fechafin;
    }

    @Override
    public String toString() {
        return "Reserva -> Nº: " + nreserva +
               ", DNI Cliente: " + dnicli +
               ", Modelo: " + modelo +
               ", Inicio: " + fechaini +
               ", Fin: " + fechafin;
    }
}
