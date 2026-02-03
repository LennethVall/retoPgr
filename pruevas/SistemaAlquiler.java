package pruevas;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clases.*;

public class SistemaAlquiler {

	
	private static File ficheroPersonas = new File("personas.dat");
	private static File ficheroVehiculos = new File("vehiculos.dat");
	private static File ficheroReservas = new File("reservas.dat");
	
	private static ArrayList<Persona> personas = new ArrayList<>();

	private static ArrayList<Vehiculo> vehiculos = new ArrayList<>();
	private static ArrayList<Reserva> reservas = new ArrayList<>();
	private static int nreservas = 100;
	private static int idiomaSeleccionado = 1;
	
	public static void main(String[] args) {
		cargarDatos();
		
				System.out.println("Seleccione idioma / Select language:");
				System.out.println("1. Español");
				System.out.println("2. English");
				idiomaSeleccionado = Util.leerInt();

				

		Scanner teclado = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("===== ALQUILER DE VEHICULOS =====");
			System.out.println("|||||||||||||||||||||||||||||||||");
			System.out.println("¡Bienvenid@ al menu principal!");
			System.out.println("/nOpciones disponibles:");
			System.out.println("1. Alta de cliente");
			System.out.println("2. Listar clientes");
			System.out.println("3. Modificar cliente");
			System.out.println("4. Añadir empleado");
			System.out.println("5. Listar empleados");
			System.out.println("6. Añadir vehiculo");
			System.out.println("7. Listar vehiculos");
			System.out.println("8. Añadir reserva");
			System.out.println("9. Listar reservas");
			System.out.println("10. Mostrar reservas de cliente");
			System.out.println("11. Anular reserva");
			System.out.println("12. Guardar datos");
			System.out.println("13. Salir");
			System.out.print("Seleccione opción: ");
			opcion = teclado.nextInt();

			switch (opcion) {
			case 1:
				altaCliente();
				break;
			case 2:
				listarClientes();
				break;
			case 3:
				modificarCliente();
				break;
			case 4:
				altaEmpleado();
				break;
			case 5:
				listarEmpleados();
				break;
			case 6:
				altaVehiculo();
				break;
			case 7:
				listarVehiculos();
				break;
			case 8:
				añadirReserva();
				break;
			case 9:
				listarReservas();
				break;
			case 10:
				mostrarReservasCliente();
				break;
			case 11:
				anularReserva();
				break;
			case 12:
				guardarDatos();
				break;
			case 13:
				System.out.println("¿Seguro que desea salir? (s/n)");
				if (teclado.next().equalsIgnoreCase("s")) {
					guardarDatos();
					System.exit(0);
				}
				break;
			}
		} while (true);
	}

	private static void altaCliente() {
	    String nombre, apellido, dni;
	    int telefono;
	    boolean dniValido = false;
	    boolean existe = false;

	    // Pedir DNI válido y que no exista
	    do {
	        System.out.println("Dame el dni del cliente");
	        dni = Util.introducirCadena();

	        if (!dni.matches("\\d{8}[A-Za-z]")) {
	            System.out.println("Formato de dni incorrecto, intentalo de nuevo");
	            continue;
	        }

	        // Comprobar si ya existe un cliente con ese DNI
	        existe = false;
	        for (Persona p : personas) {
	            if (p instanceof Cliente) {
	                Cliente c = (Cliente) p;
	                if (dni.equalsIgnoreCase(c.getDni())) {
	                    existe = true;
	                }
	            }
	        }

	        if (existe) {
	            System.out.println("Ya existe un cliente con ese DNI.");
	        } else {
	            dniValido = true;
	        }

	    } while (!dniValido);

	    // Pedir datos
	    System.out.println("Dame el nombre del cliente");
	    nombre = Util.introducirCadena();

	    System.out.println("Dame el apellido del cliente");
	    apellido = Util.introducirCadena();

	    System.out.println("Dame el telefono del cliente");
	    telefono = Util.leerInt();

	    // Crear cliente
	    Cliente c = new Cliente(dni, nombre, apellido, telefono);

	    // Añadir a la lista general de personas
	    personas.add(c);

	    System.out.println("Cliente añadido :)");

	    // Guardar en fichero único personas.dat
	    guardarPersonas();
	}

	private static void listarClientes() {
	    if (personas.isEmpty()) {
	        System.out.println("No hay clientes que mostrar");
	    } else {
	        for (Persona p : personas) {
	            if (p instanceof Cliente) {
	                Cliente c = (Cliente) p;  
	                System.out.println(c.toString());
	            }
	        }
	    }
	}


	private static void modificarCliente() {
	    if (personas.size() == 0) {
	        System.out.println("No hay clientes que modificar");
	        return;
	    }

	    String dni;
	    boolean dniValido = false;

	    // Pedir DNI válido
	    do {
	        System.out.println("Dame el dni del cliente");
	        dni = Util.introducirCadena();

	        if (!dni.matches("\\d{8}[A-Za-z]")) {
	            System.out.println("Formato de dni incorrecto, intentalo de nuevo");
	        } else {
	            dniValido = true;
	        }

	    } while (!dniValido);

	    // Buscar cliente
	    Cliente cliente = null;

	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            if (dni.equalsIgnoreCase(c.getDni())) {
	                cliente = c;
	            }
	        }
	    }

	    if (cliente == null) {
	        System.out.println("Dni incorrecto, no se encontro el cliente");
	        return;
	    }

	    System.out.println("Datos actuales:");
	    System.out.println(cliente.toString());

	    String nombreAnterior = cliente.getNombre();
	    String apellidoAnterior = cliente.getApellido();

	    System.out.println("¿Que desea modificar?");
	    System.out.println("1. Nombre");
	    System.out.println("2. Apellido");
	    System.out.println("3. Telefono");
	    System.out.println("4. Cancelar");
	    int opcion = Util.leerInt();

	    switch (opcion) {
	    case 1:
	        System.out.println("Nuevo nombre:");
	        String nuevoNombre = Util.introducirCadena();
	        cliente.setNombre(nuevoNombre);
	        break;

	    case 2:
	        System.out.println("Nuevo apellido:");
	        String nuevoApellido = Util.introducirCadena();
	        cliente.setApellido(nuevoApellido);
	        break;

	    case 3:
	        System.out.println("Nuevo telefono:");
	        int nuevoTelefono = Util.leerInt();
	        cliente.setTelefono(nuevoTelefono);
	        break;

	    case 4:
	        System.out.println("Operacion cancelada");
	        return;

	    default:
	        System.out.println("Opcion no valida");
	        return;
	    }

	    // Si cambia nombre o apellido → actualizar reservas
	    if (!cliente.getNombre().equals(nombreAnterior) || 
	        !cliente.getApellido().equals(apellidoAnterior)) {

	        actualizarNombreEnReservas(dni, cliente.getNombre(), cliente.getApellido());
	    }

	    guardarPersonas();
	    guardarReservas();

	    System.out.println("Cliente modificado correctamente");
	}

	private static void actualizarNombreEnReservas(String dni, String nuevoNombre, String nuevoApellido) {
		for (Reserva r : reservas) {
			if (r.getDnicli().equals(dni)) {
				r.setNombreCli(nuevoNombre);
				r.setApellidoCli(nuevoApellido);
			}
		}
	}

	private static void altaEmpleado() {
	    System.out.print("Introduce el DNI del nuevo empleado: ");
	    String dni = Util.introducirCadena();

	    try {
	        validarDNI(dni);
	    } catch (DNIException e) {
	        System.out.println(e.getMessage());
	        return;
	    }

	    // Comprobar si ya existe un empleado con ese DNI
	    if (dniEmpleadoExiste(dni)) {
	        System.out.println("Ya existe un empleado con ese DNI.");
	        return;
	    }

	    System.out.print("Nombre: ");
	    String nombre = Util.introducirCadena();

	    System.out.print("Apellido: ");
	    String apellido = Util.introducirCadena();

	    Cargo cargo = null;
	    boolean cargoValido = false;
	    int opcionCargo;

	    while (!cargoValido) {
	        System.out.print("Introduce el cargo (1-Comercial / 2-Recepcionista / 3-Mecánico): ");
	        opcionCargo = Util.leerInt();

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
	    personas.add(nuevo);

	    System.out.println("Empleado añadido correctamente:");
	    System.out.println(nuevo);

	    guardarPersonas();
	}

	private static void listarEmpleados() {
		if (personas.size() == 0) {
			System.out.println("No hay empleados que mostrar");
		} else {
			for (Persona p : personas) {
				if (p instanceof Empleado) {
					Empleado e = (Empleado) p;
					System.out.println(e.toString());
				}
			}
		}
	}

	private static boolean dniEmpleadoExiste(String dni) {
		for (Persona p : personas) {
			if (p instanceof Empleado) {
				Empleado e = (Empleado) p;
				if (e.getDni().equalsIgnoreCase(dni)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void validarDNI(String dni) throws DNIException {
	    Pattern modelo = Pattern.compile("\\d{8}[A-Za-z]");
	    Matcher m = modelo.matcher(dni);

	    if (!m.matches()) {
	        throw new DNIException("El DNI no tiene el formato correcto.");
	    }
	}


	private static void altaVehiculo() {
	    System.out.println("Tipo de vehiculo (1-Coche / 2-Moto):");
	    int tipo = Util.leerInt();

	    if (tipo != 1 && tipo != 2) {
	        System.out.println("Tipo no válido.");
	        return;
	    }

	    System.out.println("Matricula:");
	    String matricula = Util.introducirCadena();

	    for (Vehiculo v : vehiculos) {
	        if (v.getMatricula().equalsIgnoreCase(matricula)) {
	            System.out.println("Ya existe un vehiculo con esa matricula");
	            return;
	        }
	    }

	    System.out.println("Marca:");
	    String marca = Util.introducirCadena();

	    System.out.println("Modelo:");
	    String modelo = Util.introducirCadena();

	    Vehiculo vehiculo;

	    if (tipo == 2) {
	        System.out.println("Cilindrada (cc):");
	        int cilindrada = Util.leerInt();
	        vehiculo = new Moto(matricula, marca, modelo, cilindrada);
	    } else {
	        System.out.println("Numero de puertas:");
	        int puertas = Util.leerInt();
	        vehiculo = new Coche(matricula, marca, modelo, puertas);
	    }

	    vehiculos.add(vehiculo);
	    System.out.println("Vehiculo añadido correctamente");
	    guardarVehiculos();
	}

	private static void listarVehiculos() {
		if (vehiculos.size() == 0) {
			System.out.println("No hay vehiculos que mostrar");
		} else {
			for (Vehiculo v : vehiculos) {
				System.out.println(v.toString());
			}
		}
	}

	private static void añadirReserva() {
	    if (personas.size() == 0) {
	        System.out.println("No hay clientes a los que añadirles reservas");
	        return;
	    }

	    if (vehiculos.size() == 0) {
	        System.out.println("No hay vehiculos disponibles");
	        return;
	    }

	    String dni, modelo;
	    LocalDate fechaini = null;
	    LocalDate fechafin = null;
	    boolean dniValido = false;

	    // Pedir DNI válido
	    do {
	        System.out.println("Dame el dni del cliente");
	        dni = Util.introducirCadena();

	        if (!dni.matches("\\d{8}[A-Za-z]")) {
	            System.out.println("Formato de dni incorrecto, intentalo de nuevo");
	        } else {
	            dniValido = true;
	        }

	    } while (!dniValido);

	    // Buscar cliente
	    Cliente cliente = null;

	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            if (dni.equalsIgnoreCase(c.getDni())) {
	                cliente = c;
	            }
	        }
	    }

	    if (cliente == null) {
	        System.out.println("Dni incorrecto, no se encontro el cliente");
	        return;
	    }

	    // Pedir modelo
	    System.out.println("Que modelo desea alquilar?");
	    modelo = Util.introducirCadena();

	    // Comprobar si existe un vehículo con ese modelo
	    boolean modeloExiste = false;
	    for (Vehiculo v : vehiculos) {
	        if (v.getModelo().equalsIgnoreCase(modelo)) {
	            modeloExiste = true;
	        }
	    }

	    if (!modeloExiste) {
	        System.out.println("No existe ningún vehículo con ese modelo");
	        return;
	    }

	    try {
	        String textoFecha;
	        LocalDate hoy = LocalDate.now();
	       

	        // FECHA INICIO
	        do {
	            System.out.println("¿Cuándo quiere empezar la reserva? (YYYY-MM-DD)");
	            textoFecha = Util.introducirCadena();

	            // Validar formato
	            if (!textoFecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
	                System.out.println("Formato incorrecto");
	                continue;
	            }

	            fechaini = LocalDate.parse(textoFecha);

	            // Validar que sea posterior a hoy
	            if (!fechaini.isAfter(hoy)) {
	                System.out.println("La fecha debe ser posterior a hoy");
	            }

	        } while (fechaini == null || !fechaini.isAfter(hoy));


	        // NÚMERO DE DÍAS
	        System.out.println("¿Cuántos días desea reservar?");
	        int dias = Util.leerInt();

	        // FECHA FIN AUTOMÁTICA
	        fechafin = fechaini.plusDays(dias);

	        // CREAR RESERVA
	        Reserva r = new Reserva(
	                nreservas,
	                dni,
	                cliente.getNombre(),
	                cliente.getApellido(),
	                modelo,
	                fechaini,
	                fechafin
	        );

	        cliente.aniadirReserva(r);
	        reservas.add(r);
	        nreservas++;

	        System.out.println("Reserva creada correctamente");
	        System.out.println("Fecha fin calculada: " + fechafin);
	        System.out.println("Su número de reserva es " + r.getNreserva());

	        guardarReservas();
	        guardarPersonas();

	    } catch (DateTimeParseException e) {
	        System.out.println("Formato de fecha incorrecto");
	    }
	}


	private static void listarReservas() {
		if (reservas.size() == 0) {
			System.out.println("No hay reservas que mostrar");
		} else {
			for (Reserva r : reservas) {
				System.out.println(r.toString());
			}
		}
	}

	private static void mostrarReservasCliente() {
	    if (personas.size() == 0) {
	        System.out.println("No hay clientes de los cuales mostrar reservas");
	        return;
	    }

	    String dni;
	    boolean dniValido = false;

	    // Validar formato del DNI
	    do {
	        System.out.println("Dame el dni del cliente");
	        dni = Util.introducirCadena();

	        if (!dni.matches("\\d{8}[A-Za-z]")) {
	            System.out.println("Formato de dni incorrecto, intentalo de nuevo");
	        } else {
	            dniValido = true;
	        }

	    } while (!dniValido);

	    // Buscar cliente
	    Cliente cliente = null;

	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            if (dni.equalsIgnoreCase(c.getDni())) {
	                cliente = c;
	            }
	        }
	    }

	    if (cliente == null) {
	        System.out.println("Dni incorrecto, no se encontro el cliente");
	        return;
	    }

	    // Mostrar reservas del cliente
	    cliente.listarReservas();
	}


	private static void anularReserva() {
	    if (personas.size() == 0) {
	        System.out.println("No hay clientes a los que anular reservas");
	        return;
	    }

	    String dni;
	    boolean dniValido = false;

	    // Validar formato del DNI
	    do {
	        System.out.println("Dame el dni del cliente");
	        dni = Util.introducirCadena();

	        if (!dni.matches("\\d{8}[A-Za-z]")) {
	            System.out.println("Formato de dni incorrecto, intentalo de nuevo");
	        } else {
	            dniValido = true;
	        }

	    } while (!dniValido);

	    // Buscar cliente
	    Cliente cliente = null;

	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            if (dni.equalsIgnoreCase(c.getDni())) {
	                cliente = c;
	            }
	        }
	    }

	    if (cliente == null) {
	        System.out.println("DNI incorrecto, no se encontro el cliente");
	        return;
	    }

	    // Mostrar reservas del cliente
	    cliente.listarReservas();

	    System.out.println("Introduzca el numero de reserva de la reserva que desea eliminar");
	    int nreserva = Util.leerInt();

	    // Buscar la reserva en la lista general
	    Reserva reservaAEliminar = null;

	    for (Reserva r : reservas) {
	        if (nreserva == r.getNreserva()) {
	            reservaAEliminar = r;
	            break;
	        }
	    }

	    if (reservaAEliminar == null) {
	        System.out.println("No existe ninguna reserva con ese número");
	        return;
	    }

	    // Eliminar de cliente y de lista general
	    cliente.eliminarReserva(reservaAEliminar);
	    reservas.remove(reservaAEliminar);

	    guardarReservas();
	    guardarPersonas();

	    System.out.println("Reserva anulada correctamente");
	}


	private static void cargarDatos() {
	    cargarPersonas();
	    cargarVehiculos();
	    cargarReservas();
	    reconstruirRelaciones();
	}

	private static void guardarDatos() {
	    guardarPersonas();
	    guardarVehiculos();
	    guardarReservas();
	    System.out.println("Datos guardados correctamente");
	}


	@SuppressWarnings("unchecked")
	private static void cargarPersonas() {
	    if (!ficheroPersonas.exists()) return;

	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroPersonas))) {
	        personas = (ArrayList<Persona>) ois.readObject();
	    } catch (Exception e) {
	        System.out.println("Error al cargar personas");
	    }
	}


	private static void guardarPersonas() {
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroPersonas))) {
	        oos.writeObject(personas);
	    } catch (IOException e) {
	        System.out.println("Error al guardar personas: " + e.getMessage());
	    }
	}

	@SuppressWarnings("unchecked")
	private static void cargarVehiculos() {
		if (!ficheroVehiculos.exists()) {
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroVehiculos))) {
			vehiculos = (ArrayList<Vehiculo>) ois.readObject();
		} catch (Exception e) {
			System.out.println("Error al cargar vehiculos");
		}
	}

	private static void guardarVehiculos() {
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroVehiculos))) {
	        oos.writeObject(vehiculos);
	    } catch (IOException e) {
	        System.out.println("Error de escritura: " + e.getMessage());
	    }
	}


	
	@SuppressWarnings("unchecked")
	private static void cargarReservas() {
	    if (!ficheroReservas.exists()) {
	        return;
	    }

	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroReservas))) {
	        reservas = (ArrayList<Reserva>) ois.readObject();

	        if (!reservas.isEmpty()) {
	            int maxNum = 0;
	            for (Reserva r : reservas) {
	                if (r.getNreserva() > maxNum) {
	                    maxNum = r.getNreserva();
	                }
	            }
	            nreservas = maxNum + 1;
	        }

	    } catch (Exception e) {
	        System.out.println("Error al cargar reservas");
	    }
	}

	private static void guardarReservas() {
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroReservas))) {
	        oos.writeObject(reservas);
	    } catch (IOException e) {
	        System.out.println("Error de escritura: " + e.getMessage());
	    }
	}


	private static void reconstruirRelaciones() {

	    if (personas == null) personas = new ArrayList<>();
	    if (reservas == null) reservas = new ArrayList<>();

	    // Limpiar reservas de cada cliente
	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            c.limpiarReservas();
	        }
	    }

	    // Volver a asignar reservas a sus clientes
	    for (Reserva r : reservas) {
	        for (Persona p : personas) {
	            if (p instanceof Cliente) {
	                Cliente c = (Cliente) p;
	                if (r.getDnicli().equals(c.getDni())) {
	                    c.aniadirReserva(r);
	                }
	            }
	        }
	    }
	}
}
