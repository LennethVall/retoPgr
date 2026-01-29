package pruevas;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import clases.*;
import clases.Utilidades;

public class Main2 {
	private static File ficheroPersonas = new File("personas.obj");
	private static File ficheroReservas = new File("reservas.obj");

	public static void main(String[] args) {
		int opcion;
		do {
			System.out.println("\n===== ALQUILER DE VEHICULOS =====");
			System.out.println("1. Alta de cliente");
			System.out.println("2. Listar clientes");
			System.out.println("3. Añadir empleado");
			System.out.println("4. Añadir reserva");
			System.out.println("5. Listar reservas");
			System.out.println("6. Mostrar reservas de cliente");
			System.out.println("7. Anular reserva");
			System.out.println("8. Salir");
			opcion = Utilidades.leerInt("Seleccione opción: ", 1, 8);

			switch (opcion) {
				case 1: altaCliente(); break;
				case 2: listarClientes(); break;
				case 3: altaEmpleado(); break;
				case 4: añadirReserva(); break;
				case 5: listarReservas(); break;
				case 6: mostrarReservasCliente(); break;
				case 7: anularReserva(); break;
				case 8:
					System.out.println("¿Seguro que desea salir? (S/N)");
					if (Utilidades.leerChar('S', 'N') == 'S') System.exit(0);
					break;
			}
		} while (true);
	}

	// --- GESTIÓN DE PERSONAS ---

	private static void altaCliente() {
		HashMap<String, Persona> personas = cargarPersonas();
		String dni = Utilidades.introducirCadena("Dame el DNI del cliente:");

		if (personas.get(dni) != null) { // Read: Teoría HashMap
			System.out.println("El DNI ya existe.");
			return;
		}

		String nombre = Utilidades.introducirCadena("Dame el nombre:");
		int telefono = Utilidades.leerInt("Dame el teléfono:");

		Cliente c = new Cliente(dni, nombre, "", telefono);
		escribirPersona(c);
		System.out.println("Cliente añadido correctamente.");
	}

	private static void altaEmpleado() {
		HashMap<String, Persona> personas = cargarPersonas();
		String dni = Utilidades.introducirCadena("Introduce el DNI del nuevo empleado:");

		if (personas.get(dni) != null) {
			System.out.println("Ya existe ese DNI.");
			return;
		}

		String nombre = Utilidades.introducirCadena("Nombre:");
		String apellido = Utilidades.introducirCadena("Apellido:");
		int opc = Utilidades.leerInt("Cargo (1-Comercial / 2-Recepcionista / 3-Mecánico): ", 1, 3);
		
		Cargo cargo = (opc == 1) ? Cargo.COMERCIAL : (opc == 2) ? Cargo.RECEPCIONISTA : Cargo.MECANICO;

		Empleado nuevo = new Empleado(dni, nombre, apellido, cargo);
		escribirPersona(nuevo);
		System.out.println("Empleado añadido correctamente.");
	}

	private static void listarClientes() {
		HashMap<String, Persona> personas = cargarPersonas();
		if (personas.isEmpty()) {
			System.out.println("No hay clientes registrados.");
		} else {
			for (Persona p : personas.values()) {
				if (p instanceof Cliente) System.out.println(p);
			}
		}
	}

	// --- GESTIÓN DE RESERVAS ---

	private static void añadirReserva() {
		HashMap<String, Persona> personas = cargarPersonas();
		String dni = Utilidades.introducirCadena("Dame el DNI del cliente:");

		if (personas.get(dni) == null || !(personas.get(dni) instanceof Cliente)) {
			System.out.println("Error: No se encontró el cliente.");
			return;
		}

		int num = Utilidades.leerInt("Número de reserva:");
		String modelo = Utilidades.introducirCadena("Modelo de vehículo:");
		System.out.println("Fecha de inicio:");
		LocalDate fecha = Utilidades.leerFechaDMA();
		int dias = Utilidades.leerInt("Días de duración:");

		Reserva r = new Reserva(num, dni, modelo, fecha, dias);
		escribirReserva(r);
		System.out.println("Reserva guardada.");
	}

	private static void anularReserva() {
		HashMap<Integer, Reserva> reservas = cargarReservas();
		int nreserva = Utilidades.leerInt("Número de reserva a eliminar:");

		if (reservas.remove(nreserva) != null) { // Delete: Teoría HashMap
			// Al borrar un elemento, debemos reescribir el fichero completo (Baja física)
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroReservas))) {
				for (Reserva r : reservas.values()) oos.writeObject(r);
				System.out.println("Reserva anulada.");
			} catch (IOException e) {
				System.out.println("Error al actualizar el fichero.");
			}
		} else {
			System.out.println("No existe esa reserva.");
		}
	}

	private static void listarReservas() {
		HashMap<Integer, Reserva> reservas = cargarReservas();
		if (reservas.isEmpty()) System.out.println("No hay reservas.");
		else reservas.values().forEach(System.out::println);
	}

	private static void mostrarReservasCliente() {
		HashMap<Integer, Reserva> reservas = cargarReservas();
		String dni = Utilidades.introducirCadena("DNI del cliente:");
		reservas.values().stream()
				.filter(r -> r.getDniCliente().equalsIgnoreCase(dni))
				.forEach(System.out::println);
	}

	// --- PERSISTENCIA (CARGAR/GUARDAR) ---

	private static HashMap<String, Persona> cargarPersonas() {
		HashMap<String, Persona> mapa = new HashMap<>();
		if (!ficheroPersonas.exists()) return mapa;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroPersonas))) {
			while (true) {
				Persona p = (Persona) ois.readObject();
				mapa.put(p.getDni(), p);
			}
		} catch (EOFException e) {
		} catch (Exception e) { e.printStackTrace(); }
		return mapa;
	}

	private static HashMap<Integer, Reserva> cargarReservas() {
		HashMap<Integer, Reserva> mapa = new HashMap<>();
		if (!ficheroReservas.exists()) return mapa;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroReservas))) {
			while (true) {
				Reserva r = (Reserva) ois.readObject();
				mapa.put(r.getNumeroReserva(), r);
			}
		} catch (EOFException e) {
		} catch (Exception e) { e.printStackTrace(); }
		return mapa;
	}

	private static void escribirPersona(Persona p) {
		boolean existe = ficheroPersonas.exists();
		try (FileOutputStream fos = new FileOutputStream(ficheroPersonas, true);
			 ObjectOutputStream oos = existe ? new SinCabeceraObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
			oos.writeObject(p);
		} catch (IOException e) { e.printStackTrace(); }
	}

	private static void escribirReserva(Reserva r) {
		boolean existe = ficheroReservas.exists();
		try (FileOutputStream fos = new FileOutputStream(ficheroReservas, true);
			 ObjectOutputStream oos = existe ? new SinCabeceraObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
			oos.writeObject(r);
		} catch (IOException e) { e.printStackTrace(); }
	}
}