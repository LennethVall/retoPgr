package clases;

public class Moto extends Vehiculo {

    private int cilindrada;

    public Moto(String matricula, String marca, String modelo, int cilindrada) {
        super(matricula, marca, modelo);

        if (cilindrada <= 0) {
            throw new IllegalArgumentException("La cilindrada debe ser mayor que 0");
        }

        this.cilindrada = cilindrada;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        if (cilindrada <= 0) {
            throw new IllegalArgumentException("La cilindrada debe ser mayor que 0");
        }
        this.cilindrada = cilindrada;
    }

    @Override
    public String toString() {
        return "Moto -> " + super.toString() +
               ", Cilindrada: " + cilindrada + "cc";
    }
}
