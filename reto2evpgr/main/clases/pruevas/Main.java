package pruevas;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clases.Cargo;
import clases.Cliente;
import clases.DNIException;
import clases.Empleado;
import clases.Persona;
import clases.Reserva;
import clases.SinCabeceraObjectOutputStream;
import recursos.Util;


public class Main {
	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		File ficheroEmpleados = new File("empleados.dat");
		File ficheroClientes = new File("clientes.dat");
		ArrayList<Empleado> empleados = new ArrayList<>();
	    
		
		int opcion,nreservas=100;
		ArrayList<Cliente> Clientes=new ArrayList<Cliente>();
		ArrayList<Persona> personas=new ArrayList<Persona>();
		ArrayList<Reserva> reservas=new ArrayList<Reserva>();
		File refFichero=new File("personas.dat");
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
			System.out.println("8. Modificar Cliente");
			System.out.println("9. Salir");
			System.out.print("Seleccione opción: ");
			opcion = teclado.nextInt();

			switch (opcion) { // clases separadas del main
			case 1:
				altaCliente(personas,refFichero);
				break;
			case 2:
				listarClientes(personas);
				break;
			case 3:
				altaEmpleado();
				break;
			case 4:
				añadirReserva(personas,reservas,nreservas);
				break;
			case 5:
				listarReservas(reservas);
				break;
			case 6:
				mostrarReservasCliente(personas);
				break;
			case 7:
				anularReserva(personas,reservas);
				break;
			case 8:
				modificarCliente();
				break;
			case 9:
				System.out.println("¿Seguro que desea salir? (s/n)");
				if (teclado.next().equalsIgnoreCase("s")) //confirmacion de salida
					System.exit(0); // salida
				break;
			}
		} while (opcion!=9);
	}

	private static File ficheroEmpleados = new File("empleados.dat");
	private static ArrayList<Empleado> empleados = new ArrayList<>();
	private static ArrayList<Cliente> clientes = new ArrayList<>();
    private static Scanner teclado = new Scanner(System.in);
	   public static void altaEmpleado() {
		   cargarEmpleados();
		  
		   
		   
		    System.out.print("Introduce el DNI del nuevo empleado: ");
		    String dni = teclado.nextLine();
		    while (true) {
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
	   
	     
	    
	

		
	

	private static void anularReserva(ArrayList<Persona> personas,ArrayList<Reserva> reservas) {
		if (personas.size()==0) {
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
			} while(!dni.matches("\\d{8}[A-Za-z]"));
;
		for(Persona p : personas) {
			if(p instanceof Cliente) {
				Cliente c=(Cliente)p;
				if(dni.equalsIgnoreCase(c.getDni())) {
					contador++;
					c.listarReservas();
					System.out.println("Introduzca el numero de reserva de la reserva que desea eliminar");
					nreserva=Util.leerInt();
					c.eliminarReserva(nreserva);
					for(Reserva r : reservas) {
						if(nreserva==r.getNreserva()) {
							reservas.remove(r);
						}
					}
				}
			}
			
		}
		if(contador==0) {
			System.out.println("DNI incorrecto, no se encontro el cliente");
		}
		}
	}

	private static void mostrarReservasCliente(ArrayList<Persona> personas) {
		if (personas.size()==0) {
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
		for(Persona p : personas) {
			if(p instanceof Cliente) {
				Cliente c=(Cliente)p;
				if(dni.equalsIgnoreCase(c.getDni())) {
					contador++;
					c.listarReservas();
				}
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

	private static void añadirReserva(ArrayList<Persona> personas,ArrayList<Reserva> reservas,int nreservas) {
		if (personas.size()==0) {
			System.out.println("No hay clientes a los que añadirles reservas");	
		}
		else {
		String dni,modelo;
		int contador=0;
		LocalDate fechaini,fechafin;
		do {
			System.out.println("Dame el dni del cliente");
			dni=Util.introducirCadena();
			 if (!dni.matches("\\d{8}[A-Za-z]")) {
				 contador++;
				 System.out.println("Formato de dni incorrecto, intentalo de nuevo");
			 }
			}while(!dni.matches("\\d{8}[A-Za-z]"));
		for(Persona p : personas) {
			if(p instanceof Cliente) {
				Cliente c=(Cliente)p;
			if(dni.equalsIgnoreCase(c.getDni())) {
				System.out.println("Que modelo desea alquilar?");
				modelo=Util.introducirCadena();
				System.out.println("Cuando quiere empezar la reserva formato(YYYY-MM-DD)");
				String textoFecha = Util.introducirCadena();
				fechaini = LocalDate.parse(textoFecha);
				System.out.println("Cuando quiere terminar la reserva formato(YYYY-MM-DD)");
				String textoFecha1 = Util.introducirCadena();
				fechafin = LocalDate.parse(textoFecha1);
				Reserva r=new Reserva(dni,modelo,fechaini,fechafin);
				c.aniadirReserva(nreservas,r);
				reservas.add(r);
				nreservas++;
				System.out.println("Su numero de reserva es "+r.getNreserva());
			}
			}
		}
		if(contador==0) {
			System.out.println("Dni incorrecto, no se encontro el cliente");
		}
		}
	}

	private static void listarClientes(ArrayList<Persona> personas) {
		if (personas.size()==0) {
			System.out.println("No hay clientes que mostrar");	
		}
		else {
			for(Persona p : personas) {
				if(p instanceof Cliente) {
					Cliente c=(Cliente)p;
					System.out.println(c.toString());
				}
			
			}
		}
	}

	private static void altaCliente(ArrayList<Persona> personas,File refFichero) {
		String nombre,apellido,dni;
		String telefono;
		int contador=0,contador1=0;
		do {
		do {
		System.out.println("Dame el dni del cliente");
		dni=Util.introducirCadena();
		 if (!dni.matches("\\d{8}[A-Za-z]")) {
			 contador++;
			 System.out.println("Formato de dni incorrecto, intentalo de nuevo");
		 }
		}while(!dni.matches("\\d{8}[A-Za-z]"));
		for(Persona p : personas) {
			if (dni.equalsIgnoreCase(p.getDni())) {
			    contador1++;
			

				}
			}
		
		}while(contador1==1);
		System.out.println("Dame el nombre del cliente");
		nombre=Util.introducirCadena();
		System.out.println("Dame el apellido del cliente");
		apellido=Util.introducirCadena();
		System.out.println("Dame el telefono del cliente");
		telefono=Util.introducirCadena();
		Cliente c=new Cliente(dni,nombre,apellido,telefono);
		personas.add(c);
		System.out.println("Cliente añadido :)");
		aniadirclientefic(personas,refFichero);
	}
	private static void aniadirclientefic(ArrayList<Persona> personas,File refFichero) {
		try (ObjectOutputStream personaOStream =
		         new ObjectOutputStream(new FileOutputStream(refFichero))) {
			for(Persona p : personas) {
				if(p instanceof Cliente) {
					Cliente c=(Cliente)p;
					personaOStream.writeObject(c);
				} 
			}
		} catch (IOException e) {
		    System.out.println("Error de escritura: " + e.getMessage());
		}
		
	}

	private static void Crearfichero() {
		
	}
public static void modificarCliente() {
	
	    File refFichero = new File("personas.dat");

	    if (!refFichero.exists() || refFichero.length() == 0) {
	        System.out.println("Fichero no existente o vacío");
	        return;
	    }

	    ArrayList<Cliente> lista = new ArrayList<>();

	   
	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(refFichero))) {
	        while (true) {
	            try {
	                Cliente c = (Cliente) ois.readObject();
	                lista.add(c);
	            } catch (EOFException eof) {
	                break;
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Error leyendo: " + e.getMessage());
	        return;
	    }

	    
	    Scanner sc = new Scanner(System.in);
	    System.out.print("Introduce el DNI del cliente a modificar: ");
	    String dni = sc.nextLine();

	    Cliente encontrado = null;

	    for (Cliente c : lista) {
	        if (c.getDni().equalsIgnoreCase(dni)) {
	            encontrado = c;
	            break;
	        }
	    }

	    if (encontrado == null) {
	        System.out.println("No existe un cliente con ese DNI.");
	        return;
	    }

	    
	    System.out.print("Nuevo nombre: ");
	    encontrado.setNombre(sc.nextLine());

	    System.out.print("Nuevo apellido: ");
	    encontrado.setApellido(sc.nextLine());

	    System.out.print("Nuevo telefono: ");
	    encontrado.setTelefono(sc.nextLine());
	    
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(refFichero))) {
	        for (Cliente c : lista) {
	            oos.writeObject(c);
	        }
	    } catch (IOException e) {
	        System.out.println("Error escribiendo fichero.");
	    }

	    System.out.println("Cliente modificado correctamente.");
	

}
}

