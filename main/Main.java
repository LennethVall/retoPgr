package main;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import clases.Cargo;
import clases.Cliente;
import clases.DNIException;
import clases.Empleado;
import clases.Reserva;
import clases.SinCabeceraObjectOutputStream;
import clases.Util;
import enums.MarcaCoche;
import enums.MarcaMoto;
import enums.ModeloCocheBMW;
import enums.ModeloCocheFord;
import enums.ModeloCocheToyota;
import enums.ModeloMotoHonda;
import enums.ModeloMotoKawasaki;
import enums.ModeloMotoYamaha;

public class Main {

	private static File ficheroClientes = new File("clientes.dat");
	private static File ficheroEmpleados = new File("empleados.dat");
	private static File ficheroReservas = new File("reservas.dat");
	private static int idiomaSeleccionado = 1;

	public static void main(String[] args) {

		// 1. Selección de idioma al arrancar (solo una vez)
		System.out.println("Seleccione idioma / Select language:");
		System.out.println("1. Español");
		System.out.println("2. English");
		idiomaSeleccionado = Util.leerInt();

		int opcion;

		// 2. Bucle principal
		do {
			mostrarMenu();
			opcion = Util.leerInt();

			switch (opcion) {
			case 1:
				altaCliente(ficheroClientes);
				break;
			case 2:
				listarClientes(ficheroClientes);
				break;
			case 3:
				altaEmpleado(ficheroEmpleados);
				break;
			case 4:
				altaReserva(ficheroClientes, ficheroReservas);
				break;
			case 5:
				listarReservas(ficheroReservas);
				break;
			case 6:
				mostrarReservasCliente(ficheroClientes, ficheroReservas);
				break;
			case 7:
				anularReserva(ficheroClientes, ficheroReservas);
				break;
			case 8:
				modificarCliente(ficheroClientes);
				break;
			case 9:
				System.out.println(idiomaSeleccionado == 2 ? "Are you sure? (s/n)" : "¿Seguro que desea salir? (s/n)");
				char respuesta = Util.leerChar();
				if (respuesta == 's' || respuesta == 'S') {
					System.out.println(idiomaSeleccionado == 2 ? "Exiting..." : "Saliendo del programa...");
					System.exit(0);
				}
				break;
			default:
				System.out.println(idiomaSeleccionado == 2 ? "Invalid option" : "Opción no válida");
			}
		} while (opcion != 9);
	}

	// A partir de aquí los métodos, uno detrás de otro, FUERA del main

	private static void mostrarMenu() {
		if (idiomaSeleccionado == 2) {
			File archivo = new File("ingles.txt");
			if (archivo.exists()) {
				try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
					String linea;
					while ((linea = br.readLine()) != null) {
						System.out.println(linea);
					}
				} catch (IOException e) {
					mostrarMenuEspanol();
				}
			} else {
				mostrarMenuEspanol();
			}
		}
	}

	private static void mostrarMenuEspanol() {
		System.out.println("\n===== ALQUILER DE VEHÍCULOS =====");
		System.out.println("1. Alta de cliente");
		System.out.println("2. Listar clientes");
		System.out.println("3. Añadir empleado");
		System.out.println("4. Añadir reserva");
		System.out.println("5. Listar reservas");
		System.out.println("6. Mostrar reservas de cliente");
		System.out.println("7. Anular reserva");
		System.out.println("8. Modificar cliente");
		System.out.println("9. Salir");
		System.out.print("Seleccione opción: ");
	}

	private static void altaCliente(File refFichero) {

		// 1. Cargar clientes desde fichero
		ArrayList<Cliente> clientes = cargarClientesDesdeFichero(refFichero);

		String dni;
		Cliente clienteEncontrado = null;

		// 2. Pedir DNI (la clase Persona valida el formato)
		do {
			System.out.println("Dame el DNI del cliente");
			dni = Util.introducirCadena();

			// Buscar si ya existe
			clienteEncontrado = buscarClientePorDni(dni, clientes);

			if (clienteEncontrado != null) {
				System.out.println("Ese DNI ya existe, introduce otro");
			}

		} while (clienteEncontrado != null);

		// 3. Pedir datos restantes
		System.out.println("Dame el nombre del cliente");
		String nombre = Util.introducirCadena();

		System.out.println("Dame el apellido del cliente");
		String apellido = Util.introducirCadena();

		System.out.println("Dame el teléfono del cliente");
		String telefono = Util.introducirCadena();

		// 4. Crear cliente (la clase valida el DNI)
		Cliente nuevo = new Cliente(dni, nombre, apellido, telefono);

		// 5. Añadir a la lista
		clientes.add(nuevo);

		// 6. Guardar lista completa en fichero
		guardarClientesEnFichero(clientes, refFichero);

		System.out.println("Cliente añadido :)");
	}

	private static ArrayList<Cliente> cargarClientesDesdeFichero(File fichero) {
		ArrayList<Cliente> lista = new ArrayList<>();

		if (!fichero.exists())
			return lista;

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
			lista = (ArrayList<Cliente>) ois.readObject();
		} catch (Exception e) {
			System.out.println("Error al cargar clientes");
		}

		return lista;
	}

	private static void guardarClientesEnFichero(ArrayList<Cliente> clientes, File fichero) {

	    File aux = new File("clientes.aux");
	    File backup = new File("clientes.backup");

	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(aux))) {

	        // 1. Escribir en el auxiliar
	        oos.writeObject(clientes);
	        oos.flush(); // Asegura que todo está en disco

	    } catch (IOException e) {
	        System.out.println("Error escribiendo en el fichero auxiliar: " + e.getMessage());
	        return;
	    }

	    // 2. Borrar backup si existe (Windows lo exige)
	    if (backup.exists()) {
	        backup.delete();
	    }

	    // 3. Renombrar original → backup
	    if (fichero.exists()) {
	        if (!fichero.renameTo(backup)) {
	            System.out.println("No se pudo crear el backup. Abortando guardado.");
	            return;
	        }
	    }

	    // 4. Renombrar aux → fichero definitivo
	    if (!aux.renameTo(fichero)) {
	        System.out.println("Error al renombrar el fichero auxiliar. Restaurando backup...");

	        // Restaurar backup
	        if (backup.exists()) {
	            backup.renameTo(fichero);
	        }
	    } else {
	        // 5. Si todo fue bien, borrar backup
	        if (backup.exists()) {
	            backup.delete();
	        }
	    }
	}


	private static Cliente buscarClientePorDni(String dni, ArrayList<Cliente> clientes) {
		for (Cliente c : clientes) {
			if (c.getDni().equalsIgnoreCase(dni)) {
				return c;
			}
		}
		return null;
	}

	private static void listarClientes(File ficheroClientes) {

		// 1. Cargar clientes desde fichero
		ArrayList<Cliente> clientes = cargarClientesDesdeFichero(ficheroClientes);

		// 2. Comprobar si hay clientes
		if (clientes.isEmpty()) {
			System.out.println("No hay clientes que mostrar");
			return;
		}

		// 3. Mostrar clientes
		for (Cliente c : clientes) {
			System.out.println(c.toString());
		}
	}

	public static void altaEmpleado(File ficheroEmpleados) {

	    // 1. Cargar empleados existentes
	    boolean ficheroTieneDatos = ficheroEmpleados.exists() && ficheroEmpleados.length() > 0;

	    // 2. Pedir DNI
	    String dni = pedirDniValido();

	    // 3. Comprobar si existe
	    if (ficheroTieneDatos) {
	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroEmpleados))) {
	            while (true) {
	                try {
	                    Empleado e = (Empleado) ois.readObject();
	                    if (e.getDni().equalsIgnoreCase(dni)) {
	                        System.out.println("Ya existe un empleado con ese DNI.");
	                        return;
	                    }
	                } catch (EOFException fin) {
	                    break;
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("Error leyendo empleados.");
	        }
	    }

	    // 4. Pedir datos
	    System.out.println("Nombre:");
	    String nombre = Util.introducirCadena();

	    System.out.println("Apellido:");
	    String apellido = Util.introducirCadena();

	    System.out.println("Cargo (1-Comercial, 2-Recepcionista, 3-Mecánico):");
	    int opcionCargo = Util.leerInt();
	    Util.introducirCadena();

	    Cargo cargo = switch (opcionCargo) {
	        case 1 -> Cargo.COMERCIAL;
	        case 2 -> Cargo.RECEPCIONISTA;
	        case 3 -> Cargo.MECANICO;
	        default -> null;
	    };

	    if (cargo == null) {
	        System.out.println("Cargo no válido.");
	        return;
	    }

	    Empleado nuevo = new Empleado(dni, nombre, apellido, cargo);

	    // 5. Guardar usando SinCabecera si el fichero ya tenía datos
	    try {
	        FileOutputStream fos = new FileOutputStream(ficheroEmpleados, true); // ← aquí NO pones append tú
	        ObjectOutputStream oos;

	        if (ficheroTieneDatos) {
	            oos = new SinCabeceraObjectOutputStream(fos);
	        } else {
	            oos = new ObjectOutputStream(fos);
	        }

	        oos.writeObject(nuevo);
	        oos.close();

	        System.out.println("Empleado añadido correctamente.");

	    } catch (Exception e) {
	        System.out.println("Error al guardar el empleado.");
	    }
	}


	private static void altaReserva(File ficheroClientes, File ficheroReservas) {

		// 1. Cargar clientes
		ArrayList<Cliente> clientes = cargarClientesDesdeFichero(ficheroClientes);

		if (clientes.isEmpty()) {
			System.out.println("No hay clientes a los que añadirles reservas");
			return;
		}

		// 2. Pedir DNI
		String dni;
		Cliente clienteEncontrado;

		do {
			dni = pedirDniValido();
			clienteEncontrado = buscarClientePorDni(dni, clientes);

			if (clienteEncontrado == null) {
				System.out.println("Cliente no encontrado. ¿Nuevo DNI? (s/n)");
				if (Util.leerChar() == 'n' || Util.leerChar() == 'N') {
					return;
				}
			}
		} while (clienteEncontrado == null);

		// 3. Elegir tipo de vehículo
		System.out.println("¿Qué desea alquilar?");
		System.out.println("1. Coche");
		System.out.println("2. Moto");

		// 3 y 4. Elegir tipo, marca y modelo (RA3: Validación de datos de entrada)
		int opcion = Util.leerInt();
		String modeloElegido = "";

		switch (opcion) {
		case 1: // COCHE
			MarcaCoche marcaCoche = null;
			do {
				try {
					System.out.println("Elige una marca de coche:");
					for (MarcaCoche m : MarcaCoche.values())
						System.out.println("- " + m);

					marcaCoche = MarcaCoche.valueOf(Util.introducirCadena().toUpperCase());
				} catch (IllegalArgumentException e) {
					System.out.println("Error: Esa marca no existe en nuestro catálogo. Inténtalo de nuevo.");
				}
			} while (marcaCoche == null);

			System.out.println("Modelos disponibles:");
			mostrarModelosCoche(marcaCoche);
			modeloElegido = Util.introducirCadena().toUpperCase();
			break;

		case 2: // MOTO
			MarcaMoto marcaMoto = null;
			do {
				try {
					System.out.println("Elige una marca de moto:");
					for (MarcaMoto m : MarcaMoto.values())
						System.out.println("- " + m);

					marcaMoto = MarcaMoto.valueOf(Util.introducirCadena().toUpperCase());
				} catch (IllegalArgumentException e) {
					System.out.println("Error: Esa marca de moto no existe. Inténtalo de nuevo.");
				}
			} while (marcaMoto == null);

			System.out.println("Modelos disponibles:");
			mostrarModelosMoto(marcaMoto);
			modeloElegido = Util.introducirCadena().toUpperCase();
			break;

		default:
			System.out.println("Opción no válida");
			return;
		}
		// 5. Pedir fechas
		// 5. Pedir fechas con validación (RA3)
		LocalDate fechaini = null;
		boolean fechaValida = false;

		do {
			System.out.println("Fecha inicio (formato YYYY-MM-DD, ej: 2024-10-25):");
			String entrada = Util.introducirCadena();

			try {
				fechaini = LocalDate.parse(entrada);

				// Opcional: Validar que la reserva no sea en el pasado
				if (fechaini.isBefore(LocalDate.now())) {
					System.out.println("La fecha no puede ser anterior a hoy.");
				} else {
					fechaValida = true;
				}
			} catch (Exception e) {
				// Mensaje apropiado para el usuario (Nivel Master RA3)
				System.out.println("Error: Formato de fecha no válido. Use guiones y el orden Año-Mes-Día.");
			}
		} while (!fechaValida);

		System.out.println("¿Cuántos días quiere tener el vehículo?");
		int dias = Util.leerInt();

		// Uso de duraciones temporales (Requisito del enunciado)
		LocalDate fechafin = fechaini.plusDays(dias);
		System.out.println("La reserva terminará el " + fechafin);

		// 6. Cargar reservas desde fichero (TreeMap)
		TreeMap<Integer, Reserva> mapaReservas = cargarReservas(ficheroReservas);

		// 7. Calcular número de reserva
		int nreserva = mapaReservas.isEmpty() ? 1 : mapaReservas.lastKey() + 1;

		// 8. Crear reserva (tu clase solo necesita modelo)
		Reserva nueva = new Reserva(nreserva, dni, modeloElegido, fechaini, fechafin);

		// 9. Añadir al cliente y al mapa
		clienteEncontrado.aniadirReserva(nreserva, nueva);
		mapaReservas.put(nreserva, nueva);

		// 10. Guardar clientes y reservas
		guardarClientesEnFichero(clientes, ficheroClientes);
		guardarReservas(mapaReservas, ficheroReservas);

		System.out.println("Reserva añadida correctamente. Su número de reserva es " + nreserva);
	}

	public static void mostrarModelosCoche(MarcaCoche marca) {

		switch (marca) {
		case BMW:
			for (ModeloCocheBMW m : ModeloCocheBMW.values())
				System.out.println(m);
			break;

		case TOYOTA:
			for (ModeloCocheToyota m : ModeloCocheToyota.values())
				System.out.println(m);
			break;

		case FORD:
			for (ModeloCocheFord m : ModeloCocheFord.values())
				System.out.println(m);
			break;

		}
	}

	public static void mostrarModelosMoto(MarcaMoto marca) {

		switch (marca) {
		case HONDA:
			for (ModeloMotoHonda m : ModeloMotoHonda.values())
				System.out.println(m);
			break;

		case YAMAHA:
			for (ModeloMotoYamaha m : ModeloMotoYamaha.values())
				System.out.println(m);
			break;

		case KAWASAKI:
			for (ModeloMotoKawasaki m : ModeloMotoKawasaki.values())
				System.out.println(m);
			break;
		}

	}

	public static TreeMap<Integer, Reserva> cargarReservas(File fichero) {
		TreeMap<Integer, Reserva> mapa = new TreeMap<>();

		if (!fichero.exists()) {
			return mapa; // si no existe, devolvemos un mapa vacío
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
			mapa = (TreeMap<Integer, Reserva>) ois.readObject();
		} catch (Exception e) {
			System.out.println("Error al cargar reservas: " + e.getMessage());
		}

		return mapa;
	}

	public static void guardarReservas(TreeMap<Integer, Reserva> mapa, File fichero) {

	    File aux = new File("reservas.aux");
	    File backup = new File("reservas.backup");

	    // 1. Escribir en el fichero auxiliar
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(aux))) {

	        oos.writeObject(mapa);
	        oos.flush(); // Asegura que todo está escrito en disco

	    } catch (IOException e) {
	        System.out.println("Error escribiendo en el fichero auxiliar: " + e.getMessage());
	        return;
	    }

	    // 2. Borrar backup si existe (Windows no permite sobrescribir)
	    if (backup.exists()) {
	        backup.delete();
	    }

	    // 3. Renombrar original → backup
	    if (fichero.exists()) {
	        if (!fichero.renameTo(backup)) {
	            System.out.println("No se pudo crear el backup. Abortando guardado.");
	            return;
	        }
	    }

	    // 4. Renombrar aux → fichero definitivo
	    if (!aux.renameTo(fichero)) {
	        System.out.println("Error al renombrar el fichero auxiliar. Restaurando backup...");

	        // Restaurar backup si existe
	        if (backup.exists()) {
	            backup.renameTo(fichero);
	        }

	    } else {
	        // 5. Si todo fue bien, borrar backup
	        if (backup.exists()) {
	            backup.delete();
	        }
	    }
	}


	public static String pedirDniValido() {
		String dni = null;
		boolean valido = false;

		do {
			try {
				System.out.println("Introduce el DNI:");
				dni = Util.introducirCadena();

				if (!dni.matches("\\d{8}[A-Za-z]")) {
					throw new DNIException("DNI incorrecto: " + dni);
				}

				valido = true;

			} catch (DNIException e) {
				System.out.println(e.getMessage());
			}

		} while (!valido);

		return dni;
	}

	public static void listarReservas(File ficheroReservas) {

		// 1. Cargar reservas desde fichero
		TreeMap<Integer, Reserva> mapa = cargarReservas(ficheroReservas);

		if (mapa.isEmpty()) {
			System.out.println("No hay reservas registradas.");
			return;
		}

		// 2. Pedir DNI
		String dni = pedirDniValido();

		boolean encontrado = false;

		System.out.println("RESERVAS DEL CLIENTE " + dni + ":");

		// 3. Recorrer el mapa y filtrar por DNI
		for (Reserva r : mapa.values()) {
			if (r.getDnicli().equalsIgnoreCase(dni)) {
				System.out.println(r);
				encontrado = true;
			}
		}

		// 4. Si no se encontró ninguna
		if (!encontrado) {
			System.out.println("Este cliente no tiene reservas registradas.");
		}
	}

	private static void anularReserva(File ficheroClientes, File ficheroReservas) {
		// 1. Cargar datos (RA5: Tratamiento complejo de ficheros relacionados)
		ArrayList<Cliente> clientes = cargarClientesDesdeFichero(ficheroClientes);
		TreeMap<Integer, Reserva> mapaReservas = cargarReservas(ficheroReservas);

		if (clientes.isEmpty() || mapaReservas.isEmpty()) {
			System.out.println("No hay datos suficientes para realizar anulaciones.");
			return;
		}

		// 2. Pedir y validar DNI (RA3)
		String dni = pedirDniValido();
		Cliente clienteEncontrado = buscarClientePorDni(dni, clientes);

		if (clienteEncontrado == null) {
			System.out.println("Cliente no registrado en el sistema.");
			return;
		}

		// 3. Mostrar reservas específicas del cliente (Listado especial)
		System.out.println("Reservas actuales del cliente " + dni + ":");
		clienteEncontrado.listarReservas();

		System.out.println("Introduce el ID de la reserva a anular:");
		int idAnular = Util.leerInt();

		// 4. Sincronización Doble (Punto crítico de lógica)
		// Comprobamos si la reserva pertenece realmente a ese cliente
		if (mapaReservas.containsKey(idAnular) && mapaReservas.get(idAnular).getDnicli().equalsIgnoreCase(dni)) {

			// A. Eliminar del mapa global
			mapaReservas.remove(idAnular);

			// B. Eliminar del historial del objeto Cliente
			clienteEncontrado.eliminarReserva(idAnular);

			// 5. Persistencia Atómica (Guardar ambos ficheros para mantener coherencia)
			guardarClientesEnFichero(clientes, ficheroClientes);
			guardarReservas(mapaReservas, ficheroReservas);

			System.out.println("Reserva " + idAnular + " anulada con éxito en todo el sistema.");
		} else {
			System.out.println("Error: La reserva no existe o no pertenece a este cliente.");
		}
	}

	private static void mostrarReservasCliente(File ficheroClientes, File ficheroReservas) {
		ArrayList<Cliente> clientes = cargarClientesDesdeFichero(ficheroClientes);
		TreeMap<Integer, Reserva> mapaReservas = cargarReservas(ficheroReservas);

		if (clientes.isEmpty()) {
			System.out.println("No hay clientes registrados.");
			return;
		}

		String dni = pedirDniValido();
		Cliente clienteEncontrado = buscarClientePorDni(dni, clientes);

		if (clienteEncontrado == null) {
			System.out.println("Cliente no encontrado.");
			return;
		}

		// 4. Filtrar y Ordenar (RA5: Listado especial)
		ArrayList<Reserva> reservasCliente = new ArrayList<>();
		for (Reserva r : mapaReservas.values()) {
			if (r.getDnicli().equalsIgnoreCase(clienteEncontrado.getDni())) {
				reservasCliente.add(r);
			}
		}

		if (reservasCliente.isEmpty()) {
			System.out.println("El cliente no tiene reservas registradas.");
			return;
		}

		// Ordenar por fecha de inicio (Uso de comparadores - Nivel Avanzado)
		reservasCliente.sort(Comparator.comparing(Reserva::getFechaini));

		System.out.println("Reservas de " + clienteEncontrado.getNombre() + " (ordenadas por fecha):");
		for (Reserva r : reservasCliente) {
			System.out.println(r);
		}
	}

	public static void modificarCliente(File ficheroClientes) {

		// 1. Cargar clientes desde fichero
		ArrayList<Cliente> clientes = cargarClientesDesdeFichero(ficheroClientes);

		if (clientes.isEmpty()) {
			System.out.println("No hay clientes en el fichero.");
			return;
		}

		// 2. Pedir DNI
		String dni;
		Cliente clienteEncontrado;

		do {
			dni = pedirDniValido();
			clienteEncontrado = buscarClientePorDni(dni, clientes);

			if (clienteEncontrado == null) {
				System.out.println("Cliente no encontrado. ¿Nuevo DNI? (s/n)");
				if (Util.leerChar() == 'n' || Util.leerChar() == 'N') {
					return;
				}
			}
		} while (clienteEncontrado == null);

		// 3. Pedir nuevos datos
		System.out.println("Nuevo nombre:");
		clienteEncontrado.setNombre(Util.introducirCadena());

		System.out.println("Nuevo apellido:");
		clienteEncontrado.setApellido(Util.introducirCadena());

		System.out.println("Nuevo teléfono:");
		clienteEncontrado.setTelefono(Util.introducirCadena());

		// 4. Guardar lista completa usando escritura segura
		guardarClientesEnFichero(clientes, ficheroClientes);

		System.out.println("Cliente modificado correctamente.");
	}
}