package clases;

public class Coche extends Vehiculo {

    private int puertas;

    public Coche(String matricula, String marca, String modelo, int puertas) {
        super(matricula, marca, modelo);

        if (puertas <= 0) {
            throw new IllegalArgumentException("El número de puertas debe ser mayor que 0");
        }

        this.puertas = puertas;
    }

    public int getPuertas() {
        return puertas;
    }

    public void setPuertas(int puertas) {
        if (puertas <= 0) {
            throw new IllegalArgumentException("El número de puertas debe ser mayor que 0");
        }
        this.puertas = puertas;
    }

    @Override
    public String toString() {
        return "Coche -> " + super.toString() +
               ", Puertas: " + puertas;
    }
}
