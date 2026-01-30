package pruevas;



import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clases.Cargo;
import clases.DNIException;
import clases.Empleado;
import clases.SinCabeceraObjectOutputStream;



public class AltaEmpleado {
	private static File ficheroEmpleados = new File("empleados.dat");
	private static ArrayList<Empleado> empleados = new ArrayList<>();
    private static Scanner teclado = new Scanner(System.in);
	public void altaEmpleado() {
		
		cargarEmpleados();
   
		   
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

		    
		    Cargo cargo = null;
		    boolean cargoValido = false;
		    int opcionCargo;

		    while (!cargoValido) {

		        System.out.print("Introduce el cargo (1-Comercial / 2-Recepcionista / 3-Mecánico): ");
		        opcionCargo = teclado.nextInt();
		        teclado.nextLine(); 
		        
		        if (opcionCargo == 1) {
		            cargo = Cargo.comercial;
		            cargoValido = true;

		        } else if (opcionCargo == 2) {
		            cargo = Cargo.recepcionista;
		            cargoValido = true;

		        } else if (opcionCargo == 3) {
		            cargo = Cargo.mecanico;
		            cargoValido = true;

		        } else {
		            System.out.println("Cargo no válido. Debe ser 1, 2 o 3.");
		        }
		    }
		    

		    Empleado nuevo = new Empleado(dni, nombre, apellido, cargo);
		    empleados.add(nuevo);
		    guardarEmpleadoEnFichero(nuevo);

		    System.out.println("Empleado añadido correctamente:");
		    System.out.println(nuevo);
		}
	 private static boolean dniEmpleadoExiste(String dni) {

	        for (Empleado e : empleados) {
	            if (e.getDni().equalsIgnoreCase(dni)) {
	                return true;
	            }
	        }
	        return false;
	    }

	    private static void validarDNI(String dni) throws DNIException {

	        Pattern modelo = Pattern.compile("\\d{8}[A-HJ-NP-TV-Z]");
	        Matcher m = modelo.matcher(dni);

	        if (!m.matches()) {
	            throw new DNIException("El DNI no tiene el formato correcto.");
	        }
	    }
	    private static void guardarEmpleadoEnFichero(Empleado e) {

	        boolean existe = ficheroEmpleados.exists();

	        try (FileOutputStream fos = new FileOutputStream(ficheroEmpleados, true)) {

	            ObjectOutputStream oos;

	            if (existe) {
	                // Si el fichero ya existe → NO escribir cabecera
	                oos = new SinCabeceraObjectOutputStream(fos);
	            } else {
	                // Si el fichero NO existe → escribir cabecera normal
	                oos = new ObjectOutputStream(fos);
	            }

	            oos.writeObject(e);
	            oos.close();

	        } catch (IOException ex) {
	            System.out.println("Error al guardar el empleado.");
	        }
	    }
	    private static void cargarEmpleados() {
	        if (!ficheroEmpleados.exists() || ficheroEmpleados.length() == 0) {
	            return;
	        }

	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroEmpleados))) {
	            while (true) {
	                Empleado e = (Empleado) ois.readObject();
	                empleados.add(e);
	            }
	        } catch (EOFException e) {
	            
	        } catch (Exception e) {
	            System.out.println("Error al leer empleados del fichero.");
	        }
	    }

	
}
	
