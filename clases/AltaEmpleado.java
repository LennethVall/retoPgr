package clases;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AltaEmpleado {

    private static final File ficheroEmpleados = new File("empleados.dat");
    private static final ArrayList<Empleado> empleados = new ArrayList<>();
    private static final Scanner teclado = new Scanner(System.in);

    public void altaEmpleado() {

        cargarEmpleadosSiExisten();

        System.out.print("Introduce el DNI del nuevo empleado: ");
        String dni = teclado.nextLine();

        try {
            validarDNI(dni);
        } catch (DNIException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (dniEmpleadoExiste(dni)) {
            System.out.println("Ya existe un empleado con ese DNI.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();

        System.out.print("Apellido: ");
        String apellido = teclado.nextLine();

        Cargo cargo = pedirCargo();

        Empleado nuevo = new Empleado(dni, nombre, apellido, cargo);
        empleados.add(nuevo);
        guardarEmpleadoEnFichero(nuevo);

        System.out.println("Empleado añadido correctamente:");
        System.out.println(nuevo);
    }

    private static void cargarEmpleadosSiExisten() {
        if (!ficheroEmpleados.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroEmpleados))) {
            while (true) {
                empleados.add((Empleado) ois.readObject());
            }
        } catch (EOFException e) {
            // Fin del fichero → normal
        } catch (Exception e) {
            System.out.println("Error al cargar empleados.");
        }
    }

    private static Cargo pedirCargo() {
        while (true) {
            System.out.print("Introduce el cargo (1-Comercial / 2-Recepcionista / 3-Mecánico): ");
            String opcion = teclado.nextLine();

            switch (opcion) {
                case "1": return Cargo.COMERCIAL;
                case "2": return Cargo.RECEPCIONISTA;
                case "3": return Cargo.MECANICO;
                default:
                    System.out.println("Cargo no válido. Debe ser 1, 2 o 3.");
            }
        }
    }

    private static boolean dniEmpleadoExiste(String dni) {
        return empleados.stream().anyMatch(e -> e.getDni().equalsIgnoreCase(dni));
    }

    private static void validarDNI(String dni) {
        Pattern modelo = Pattern.compile("\\d{8}[A-HJ-NP-TV-Z]");
        if (!modelo.matcher(dni).matches()) {
            throw new DNIException("El DNI no tiene el formato correcto.");
        }
    }

    private static void guardarEmpleadoEnFichero(Empleado e) {
        boolean existe = ficheroEmpleados.exists();

        try (FileOutputStream fos = new FileOutputStream(ficheroEmpleados, true);
             ObjectOutputStream oos = existe
                     ? new SinCabeceraObjectOutputStream(fos)
                     : new ObjectOutputStream(fos)) {

            oos.writeObject(e);

        } catch (IOException ex) {
            System.out.println("Error al guardar el empleado.");
        }
    }
}
