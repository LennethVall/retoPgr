package main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clases.Cargo;
import clases.DNIException;
import clases.Empleado;

public class Main {
	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		int opcion;

		do { // mostrar menu
			System.out.println("===== ALQUILER DE VEHICULOS =====");
			System.out.println("|||||||||||||||||||||||||||||||||");
			System.out.println("¡Bienvenid@ al menu principal!");
			System.out.println("\nOpciones disponibles:");
			System.out.println("1. Alta de cliente");
			System.out.println("2. Listar clientes");
			System.out.println("3. Añadir empleado");
			System.out.println("4. Añadir reserva");
			System.out.println("5. Listar reservas");
			System.out.println("6. Mostrar reservas de cliente");
			System.out.println("7. Anular reserva");
			System.out.println("8. Salir");
			System.out.print("Seleccione opción: ");
			opcion = teclado.nextInt();

			switch (opcion) { // clases separadas del main
			case 1:
				altaCliente();
				break;
			case 2:
				listarClientes();
				break;
			case 3:
				altaEmpleado();
				break;
			case 4:
				añadirReserva();
				break;
			case 5:
				listarReservas();
				break;
			case 6:
				mostrarReservasCliente();
				break;
			case 7:
				anularReserva();
				break;
			case 8:
				System.out.println("¿Seguro que desea salir? (s/n)");
				if (teclado.next().equalsIgnoreCase("s")) //confirmacion de salida
					System.exit(0); // salida
				break;
			}
		} while (true);
	}
	
	public void altaCliente(){
		
	}
	public void listarClientes(){
		
	}
	private static ArrayList<Empleado> empleados = new ArrayList<>();
    private static Scanner teclado = new Scanner(System.in);
	public void altaEmpleado() {
		
		   
		
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
		            cargo = Cargo.COMERCIAL;
		            cargoValido = true;

		        } else if (opcionCargo == 2) {
		            cargo = Cargo.RECEPCIONISTA;
		            cargoValido = true;

		        } else if (opcionCargo == 3) {
		            cargo = Cargo.MECANICO;
		            cargoValido = true;

		        } else {
		            System.out.println("Cargo no válido. Debe ser 1, 2 o 3.");
		        }
		    }
		    

		    Empleado nuevo = new Empleado(dni, nombre, apellido, cargo);
		    empleados.add(nuevo);

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
		

		    
		

	
	public void añadirReserva() {
		
	}
	public void listarReservas() {
		
	}
	public void mostrarReservasCliente() {
		
	}
	public void anularReserva() {
		
	}
}