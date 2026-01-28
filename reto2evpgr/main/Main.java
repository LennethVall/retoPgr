package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clases.Cargo;
import clases.Cliente;
import clases.DNIException;
import clases.Empleado;
import clases.Reserva;
import recursos.Util;

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
	
	private static void altaCliente(ArrayList<Cliente> clientes) {
		String nombre,apellido,dni;
		int telefono,contador=0;
		do {
		System.out.println("Dame el dni del cliente");
		dni=Util.introducirCadena();
		 if (!dni.matches("\\d{8}[A-Za-z]")) {
			 contador++;
			 System.out.println("Formato de dni incorrecto, intentalo de nuevo");
		 }
		}while(!dni.matches("\\d{8}[A-Za-z]"));
		
		System.out.println("Dame el nombre del cliente");
		nombre=Util.introducirCadena();
		System.out.println("Dame el apellido del cliente");
		apellido=Util.introducirCadena();
		System.out.println("Dame el telefono del cliente");
		telefono=Util.leerInt();
		Cliente c=new Cliente(dni,nombre,apellido,telefono);
		clientes.add(c);
		System.out.println("Cliente añadido :)");
	}
	private static void listarClientes(ArrayList<Cliente> clientes) {
		if (clientes.size()==0) {
			System.out.println("No hay clientes que mostrar");	
		}
		else {
			for(Cliente c : clientes) {
			System.out.println(c.toString());
			}
		}
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
		

		       private static void añadirReserva(ArrayList<Cliente> clientes,ArrayList<Reserva> reservas) {
				if (clientes.size()==0) {
					System.out.println("No hay clientes a los que añadirles reservas");	
				}
				else {
				String dniCliente,modeloVehiculo;
				int contador=0;
				LocalDate fechaInicio;
				int dias;
				int numeroReserva=0;
				do {
					System.out.println("Dame el dni del cliente");
					dniCliente=Util.introducirCadena();
					 if (!dniCliente.matches("\\d{8}[A-Za-z]")) {
						 contador++;
						 System.out.println("Formato de dni incorrecto, intentalo de nuevo");
					 }
					}while(!dniCliente.matches("\\d{8}[A-Za-z]"));
				for(Cliente c : clientes) {
					if(dniCliente.equalsIgnoreCase(c.getDni())) {
						System.out.println("Que modelo desea alquilar?");
						modeloVehiculo=Util.introducirCadena();
						System.out.println("Cuando quiere empezar la reserva formato(YYYY-MM-DD)");
						String textoFecha = Util.introducirCadena();
						fechaInicio = LocalDate.parse(textoFecha);
						System.out.println("cuantos dias quiere reservar: ");
						int numeroReserva = Util.leerInt();
						dias = Util.leerInt();
						Reserva r=new Reserva(numeroReserva,dniCliente,modeloVehiculo,fechaInicio,dias);
						c.aniadirReserva(r);
						reservas.add(r);
						System.out.println("Su numero de reserva es "+r.getNumeroReserva());
					}
				}
				if(contador==0) {
					System.out.println("Dni incorrecto, no se encontro el cliente");
				}
				}
			}
	private static void listarReservas(ArrayList<Reserva> reservas) {
		if (reservas.size()==0) {
			System.out.println("No hay reservas que mostrar");	
		}
		else {
		for(Reserva r : reservas) {
			System.out.println(r.toString());
		}
		}
	}
	private static void mostrarReservasCliente(ArrayList<Cliente> clientes) {
		if (clientes.size()==0) {
			System.out.println("No hay clientes de los cuales mostrar reservas");	
		}
		else {
		String dni;
		int contador=0;
		do {
			System.out.println("Dame el dni del cliente");
			dni=Util.introducirCadena();
			 if (!dni.matches("\\d{8}[A-Za-z]")) {
				 contador++;
				 System.out.println("Formato de dni incorrecto, intentalo de nuevo");
			 }
			}while(!dni.matches("\\d{8}[A-Za-z]"));
		for(Cliente c : clientes) {
			contador++;
			if(dni.equalsIgnoreCase(c.getDni())) {
				c.listarReservas();
			}
		}
		if(contador==0) {
			System.out.println("Dni incorrecto, no se encontro el cliente");
		}
		}
	}
	private static void anularReserva(ArrayList<Cliente> clientes) {
		if (clientes.size()==0) {
			System.out.println("No hay clientea a los que anular reservas");	
		}
		else {
		String dni;
		int nreserva,contador=0;
		do {
			System.out.println("Dame el dni del cliente");
			dni=Util.introducirCadena();
			 if (!dni.matches("\\d{8}[A-Za-z]")) {
				 contador++;
				 System.out.println("Formato de dni incorrecto, intentalo de nuevo");
			 }
			}while(contador==1);
		for(Cliente c : clientes) {
			if(dni.equalsIgnoreCase(c.getDni())) {
				contador++;
				c.listarReservas();
				System.out.println("Introduzca el numero de reserva de la reserva que desea eliminar");
				nreserva=Util.leerInt();
				c.eliminarReserva(nreserva);
			}
		}
		if(contador==0) {
			System.out.println("DNI incorrecto, no se encontro el cliente");
		}
		}
	}
}