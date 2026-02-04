package main;

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
	private static HashMap<String, String[]> traducciones = new HashMap<>();

	private static ArrayList<Persona> personas = new ArrayList<>();

	private static ArrayList<Vehiculo> vehiculos = new ArrayList<>();
	private static ArrayList<Reserva> reservas = new ArrayList<>();
	private static int nreservas = 100;
	private static int idiomaSeleccionado = 1;
	
	public static void main(String[] args) {
		cargarTraducciones();

		cargarDatos();
		
				System.out.println("Seleccione idioma / Select language:");
				System.out.println("1. Español");
				System.out.println("2. English");
				idiomaSeleccionado = Util.leerInt();

				

		Scanner teclado = new Scanner(System.in);
		int opcion;

		do {
			mostrarMenu();
			opcion = leerOpcionValida(1, 13);

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
				msg("CONFIRMAR_SALIDA");
				if (teclado.next().equalsIgnoreCase("s")) {
					guardarDatos();
					System.exit(0);
				}
				break;
			}
			
			pausar();
		} while (true);
	}

	// ==================== METODOS DE INTERFAZ ====================
	
	private static void mostrarMenu() {
	    limpiarPantalla();

	    if (idiomaSeleccionado == 2) {

	        // ===== MENÚ EN INGLÉS DESDE ARCHIVO =====
	        File archivo = new File("ingles.txt");
	        FileReader fr;
	        BufferedReader br;

	        try {
	            fr = new FileReader(archivo);
	            br = new BufferedReader(fr);

	            String linea;
	            while ((linea = br.readLine()) != null) {
	                System.out.println(linea);
	            }

	            br.close();
	            fr.close();

	        } catch (IOException e) {
	            System.out.println("Error al leer el archivo en inglés.");
	        }

	    } else {

	        // ===== MENÚ EN CASTELLANO =====
	        System.out.println("===== ALQUILER DE VEHICULOS =====");
	        System.out.println("|||||||||||||||||||||||||||||||||");
	        System.out.println("¡Bienvenid@ al menu principal!");
	        System.out.println("\nOpciones disponibles:");
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
	    }
	}


	   

	
	private static void limpiarPantalla() {
		for (int i = 0; i < 2; i++) {
			System.out.println();
		}
	}
	
	private static void pausar() {
		msg("PAUSAR");
		try {
			System.in.read();
			while (System.in.available() > 0) {
				System.in.read();
			}
		} catch (IOException e) {
		}
	}
	
	private static int leerOpcionValida(int min, int max) {
		int opcion;
		do {
			msg("SELECCIONE_OPCION");
			opcion = Util.leerInt();
			if (opcion < min || opcion > max) {
				msg("OPCION_INVALIDA");
			}
		} while (opcion < min || opcion > max);
		return opcion;
	}
	
	private static String solicitarDNIValido() {
		String dni;
		do {
			msg("INTRODUCIR_DNI");
			dni = Util.introducirCadena();
			if (!dni.matches("\\d{8}[A-Za-z]")) {
				msg("DNI_FORMATO_INCORRECTO");
			} else {
				return dni;
			}
		} while (true);
	}
	
	private static void imprimirSeparador() {
		System.out.println("-".repeat(80));
	}
	
	private static void imprimirTitulo(String titulo) {
		System.out.println("\n" + "=".repeat(80));
		System.out.println("  " + titulo);
		System.out.println("=".repeat(80));
	}

	// ==================== GESTION DE CLIENTES ====================

	private static void altaCliente() {
	    imprimirTitulo("ALTA DE CLIENTES");
	    
	    String dni = solicitarDNIValido();
	    
	    boolean existe = false;
	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            if (dni.equalsIgnoreCase(c.getDni())) {
	                existe = true;
	            }
	        }
	    }

	    if (existe) { 
	    	msg("CLIENTE_EXISTE");
	        return;
	    }

	    msg("INTRODUCIR_NOMBRE");
	    String nombre = Util.introducirCadena();

	    msg("INTRODUCIR_APELLIDO");
	    String apellido = Util.introducirCadena();

	    msg("INTRODUCIR_TELEFONO");
	    int telefono = Util.leerInt();

	    Cliente c = new Cliente(dni, nombre, apellido, telefono);
	    personas.add(c);

	    msg("CLIENTE_ANADIDO");
	    guardarPersonas();
	}

	private static void listarClientes() {
	    imprimirTitulo("LISTADO DE CLIENTES");
	    
	    ArrayList<Cliente> clientes = new ArrayList<>();
	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            clientes.add((Cliente) p);
	        }
	    }
	    
	    if (clientes.isEmpty()) {
	    	msg("NO_HAY_CLIENTES");
	    } else {
	        System.out.println();
	        System.out.printf("%-12s %-20s %-20s %-12s%n", "DNI", "NOMBRE", "APELLIDO", "TELEFONO");
	        imprimirSeparador();
	        
	        for (Cliente c : clientes) {
	            System.out.printf("%-12s %-20s %-20s %-12d%n",
	                c.getDni(), c.getNombre(), c.getApellido(), c.getTelefono());
	        }
	        
	        msg("TOTAL_CLIENTES");
	    }
	}


	private static void modificarCliente() {
	    imprimirTitulo("MODIFICAR CLIENTE");
	    
	    if (personas.size() == 0) {
	    	msg("NO_HAY_CLIENTES_MODIFICAR");
	        return;
	    }

	    String dni = solicitarDNIValido();

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
	    	msg("CLIENTE_NO_ENCONTRADO");
	        return;
	    }

	    msg("DATOS_ACTUALES");
	    System.out.println(cliente.toString());

	    String nombreAnterior = cliente.getNombre();
	    String apellidoAnterior = cliente.getApellido();

	    msg("QUE_DESEA_MODIFICAR"); 
	    msg("MOD_NOMBRE"); 
	    msg("MOD_APELLIDO"); 
	    msg("MOD_TELEFONO");
	    msg("MOD_CANCELAR");
	    
	    int opcion = Util.leerInt();

	    switch (opcion) {
	    case 1:
	    	msg("NUEVO_NOMBRE");
	        String nuevoNombre = Util.introducirCadena();
	        cliente.setNombre(nuevoNombre);
	        break;

	    case 2:
	    	msg("NUEVO_APELLIDO");
	        String nuevoApellido = Util.introducirCadena();
	        cliente.setApellido(nuevoApellido);
	        break;

	    case 3:
	    	msg("NUEVO_TELEFONO");
	        int nuevoTelefono = Util.leerInt();
	        cliente.setTelefono(nuevoTelefono);
	        break;

	    case 4:
	    	msg("OPERACION_CANCELADA");
	        return;

	    default:
	    	msg("OPCION_INVALIDA");
	        return;
	    }

	    if (!cliente.getNombre().equals(nombreAnterior) || 
	        !cliente.getApellido().equals(apellidoAnterior)) {

	        actualizarNombreEnReservas(dni, cliente.getNombre(), cliente.getApellido());
	    }

	    guardarPersonas();
	    guardarReservas();

	    msg("CLIENTE_MODIFICADO");
	}

	private static void actualizarNombreEnReservas(String dni, String nuevoNombre, String nuevoApellido) {
		for (Reserva r : reservas) {
			if (r.getDnicli().equals(dni)) {
				r.setNombreCli(nuevoNombre);
				r.setApellidoCli(nuevoApellido);
			}
		}
	}

	// ==================== GESTION DE EMPLEADOS ====================

	private static void altaEmpleado() {
	    imprimirTitulo("ALTA DE EMPLEADO");
	    
	    msg("INTRODUCIR_DNI_EMPLEADO");
	    String dni = Util.introducirCadena();

	    try {
	        validarDNI(dni);
	    } catch (DNIException e) {
	        System.out.println(e.getMessage());
	        return;
	    }

	    if (dniEmpleadoExiste(dni)) {
	    	msg("EMPLEADO_EXISTE");
	        return;
	    }

	    msg("INTRODUCIR_NOMBRE_EMPLEADO");
	    String nombre = Util.introducirCadena();

	    msg("INTRODUCIR_APELLIDO_EMPLEADO");
	    String apellido = Util.introducirCadena();

	    Cargo cargo = null;
	    boolean cargoValido = false;
	    int opcionCargo;

	    while (!cargoValido) {
	    	msg("INTRODUCIR_CARGO");
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
	        	msg("CARGO_INVALIDO");
	        }
	    }

	    Empleado nuevo = new Empleado(dni, nombre, apellido, cargo);
	    personas.add(nuevo);

	    msg("EMPLEADO_ANADIDO");
	    System.out.println(nuevo);

	    guardarPersonas();
	}

	private static void listarEmpleados() {
	    imprimirTitulo("LISTADO DE EMPLEADOS");
	    
	    ArrayList<Empleado> empleados = new ArrayList<>();
	    for (Persona p : personas) {
	        if (p instanceof Empleado) {
	            empleados.add((Empleado) p);
	        }
	    }
	    
		if (empleados.size() == 0) {
			msg("NO_HAY_EMPLEADOS");
		} else {
		    System.out.println();
		    System.out.printf("%-12s %-20s %-20s %-15s%n", "DNI", "NOMBRE", "APELLIDO", "CARGO");
		    imprimirSeparador();
		    
			for (Empleado e : empleados) {
			    System.out.printf("%-12s %-20s %-20s %-15s%n",
			        e.getDni(), e.getNombre(), e.getApellido(), e.getCargo());
			}
			
			msg("TOTAL_EMPLEADOS");
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

	// ==================== GESTION DE VEHICULOS ====================

	private static void altaVehiculo() {
	    imprimirTitulo("ALTA DE VEHICULO");
	    
	    msg("TIPO_VEHICULO");
	    int tipo = Util.leerInt();

	    if (tipo != 1 && tipo != 2) {
	    	msg("TIPO_INVALIDO");
	        return;
	    }

	    msg("INTRODUCIR_MATRICULA");
	    String matricula = Util.introducirCadena();

	    for (Vehiculo v : vehiculos) {
	        if (v.getMatricula().equalsIgnoreCase(matricula)) {
	        	msg("VEHICULO_EXISTE");
	            return;
	        }
	    }

	    msg("INTRODUCIR_MARCA");
	    String marca = Util.introducirCadena();

	    msg("INTRODUCIR_MODELO");
	    String modelo = Util.introducirCadena();

	    Vehiculo vehiculo;

	    if (tipo == 2) {
	    	msg("INTRODUCIR_CILINDRADA");
	        int cilindrada = Util.leerInt();
	        vehiculo = new Moto(matricula, marca, modelo, cilindrada);
	    } else {
	    	msg("INTRODUCIR_PUERTAS");
	        int puertas = Util.leerInt();
	        vehiculo = new Coche(matricula, marca, modelo, puertas);
	    }

	    vehiculos.add(vehiculo);
	    msg("VEHICULO_ANADIDO");
	    guardarVehiculos();
	}

	private static void listarVehiculos() {
	    imprimirTitulo("LISTADO DE VEHICULOS");
	    
		if (vehiculos.size() == 0) {
			msg("NO_HAY_VEHICULOS");
		} else {
		    System.out.println();
		    System.out.printf("%-12s %-15s %-20s %-10s %-15s%n", 
		        "MATRICULA", "MARCA", "MODELO", "TIPO", "DETALLE");
		    imprimirSeparador();
		    
			for (Vehiculo v : vehiculos) {
			    String tipo, detalle;
			    if (v instanceof Coche) {
			        tipo = "Coche";
			        detalle = ((Coche) v).getPuertas() + " puertas";
			    } else {
			        tipo = "Moto";
			        detalle = ((Moto) v).getCilindrada() + " cc";
			    }
			    System.out.printf("%-12s %-15s %-20s %-10s %-15s%n",
			        v.getMatricula(), v.getMarca(), v.getModelo(), tipo, detalle);
			}
			
			System.out.println("\nTotal de vehiculos: " + vehiculos.size());
		}
	}

	// ==================== GESTION DE RESERVAS ====================

	private static void añadirReserva() {
	    imprimirTitulo("NUEVA RESERVA");
	    
	    if (personas.size() == 0) {
	    	msg("NO_HAY_CLIENTES_RESERVA");
	        return;
	    }

	    if (vehiculos.size() == 0) {
	    	msg("NO_HAY_VEHICULOS_RESERVA");
	        return;
	    }

	    // PASO 1: Listar y seleccionar cliente
	    msg("CLIENTES_DISPONIBLES");
	    System.out.printf("%-12s %-20s %-20s%n", "DNI", "NOMBRE", "APELLIDO");
	    imprimirSeparador();
	    
	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            System.out.printf("%-12s %-20s %-20s%n", c.getDni(), c.getNombre(), c.getApellido());
	        }
	    }
	    
	    String dni = solicitarDNIValido();

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
	    	msg("CLIENTE_NO_ENCONTRADO");
	        return;
	    }

	    // PASO 2: Solicitar fechas
	    LocalDate fechaini = null;
	    LocalDate fechafin = null;
	    
	    try {
	        String textoFecha;
	        LocalDate hoy = LocalDate.now();

	        do {
	        	msg("INTRODUCIR_FECHA_INICIO");
	            textoFecha = Util.introducirCadena();

	            if (!textoFecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
	            	msg("FORMATO_FECHA_INCORRECTO");
	                continue;
	            }

	            fechaini = LocalDate.parse(textoFecha);

	            if (!fechaini.isAfter(hoy)) {
	            	msg("FECHA_POSTERIOR_HOY");
	            }

	        } while (fechaini == null || !fechaini.isAfter(hoy));

	        msg("INTRODUCIR_DIAS");
	        int dias = Util.leerInt();

	        fechafin = fechaini.plusDays(dias);

	        System.out.println("Fecha fin calculada: " + fechafin);

	        // PASO 3: Mostrar vehiculos disponibles
	        ArrayList<Vehiculo> vehiculosDisponibles = obtenerVehiculosDisponibles(fechaini, fechafin);
	        
	        if (vehiculosDisponibles.isEmpty()) {
	        	msg("NO_HAY_VEHICULOS_FECHAS");
	            return;
	        }
	        
	        System.out.println("\n--- VEHICULOS DISPONIBLES ---\n");
	        System.out.printf("%-12s %-15s %-20s %-10s %-15s%n", 
	            "MATRICULA", "MARCA", "MODELO", "TIPO", "DETALLE");
	        imprimirSeparador();
	        
	        for (Vehiculo v : vehiculosDisponibles) {
	            String tipo, detalle;
	            if (v instanceof Coche) {
	                tipo = "Coche";
	                detalle = ((Coche) v).getPuertas() + " puertas";
	            } else {
	                tipo = "Moto";
	                detalle = ((Moto) v).getCilindrada() + " cc";
	            }
	            System.out.printf("%-12s %-15s %-20s %-10s %-15s%n",
	                v.getMatricula(), v.getMarca(), v.getModelo(), tipo, detalle);
	        }
	        
	        // PASO 4: Seleccionar por matricula
	        String matricula;
	        Vehiculo vehiculoSeleccionado = null;
	        
	        do {
	        	msg("INTRODUCIR_MATRICULA_RESERVA");
	            matricula = Util.introducirCadena();
	            
	            for (Vehiculo v : vehiculosDisponibles) {
	                if (v.getMatricula().equalsIgnoreCase(matricula)) {
	                    vehiculoSeleccionado = v;
	                    break;
	                }
	            }
	            
	            if (vehiculoSeleccionado != null) {
	                break;
	            }
	            
	            msg("MATRICULA_NO_VALIDA");
	        } while (true);

	        // PASO 5: Crear reserva
	        Reserva r = new Reserva(
	                nreservas,
	                dni,
	                cliente.getNombre(),
	                cliente.getApellido(),
	                vehiculoSeleccionado.getModelo(),
	                fechaini,
	                fechafin
	        );

	        cliente.aniadirReserva(r);
	        reservas.add(r);
	        nreservas++;

	        msg("RESERVA_CREADA");
	        System.out.println("Su número de reserva es " + r.getNreserva());

	        guardarReservas();
	        guardarPersonas();

	    } catch (DateTimeParseException e) {
	    	msg("FORMATO_FECHA_INCORRECTO");
	    }
	}

	private static void listarReservas() {
	    imprimirTitulo("LISTADO DE RESERVAS");
	    
		if (reservas.size() == 0) {
			msg("NO_HAY_RESERVAS");
		} else {
		    System.out.println();
		    System.out.printf("%-6s %-12s %-25s %-20s %-12s %-12s %-12s%n",
		        "NUM", "DNI", "CLIENTE", "MODELO", "INICIO", "FIN", "ESTADO");
		    imprimirSeparador();
		    
			for (Reserva r : reservas) {
			    String nombreCompleto = r.getNombreCli() + " " + r.getApellidoCli();
			    if (nombreCompleto.length() > 25) {
			        nombreCompleto = nombreCompleto.substring(0, 22) + "...";
			    }
			    
			    System.out.printf("%-6d %-12s %-25s %-20s %-12s %-12s %-12s%n",
			        r.getNreserva(), r.getDnicli(), nombreCompleto, 
			        r.getModelo(), r.getFechaini(), r.getFechafin(), r.getEstado());
			}
			
			System.out.println("\nTotal de reservas: " + reservas.size());
		}
	}

	private static void mostrarReservasCliente() {
	    imprimirTitulo("RESERVAS DE CLIENTE");
	    
	    if (personas.size() == 0) {
	    	msg("NO_HAY_CLIENTES_RESERVAS");
	        return;
	    }

	    String dni = solicitarDNIValido();

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
	    	msg("CLIENTE_NO_ENCONTRADO");
	        return;
	    }

	    cliente.listarReservas();
	}


	private static void anularReserva() {
	    imprimirTitulo("ANULAR RESERVA");
	    
	    if (personas.size() == 0) {
	    	msg("NO_HAY_CLIENTES_ANULAR");
	        return;
	    }

	    String dni = solicitarDNIValido();

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
	    	msg("CLIENTE_NO_ENCONTRADO");
	        return;
	    }

	    cliente.listarReservas();

	    System.out.println("Introduzca el numero de reserva de la reserva que desea eliminar");
	    int nreserva = Util.leerInt();

	    Reserva reservaAEliminar = null;

	    for (Reserva r : reservas) {
	        if (nreserva == r.getNreserva()) {
	            reservaAEliminar = r;
	            break;
	        }
	    }

	    if (reservaAEliminar == null) {
	    	msg("RESERVA_NO_EXISTE");
	        return;
	    }

	    cliente.eliminarReserva(reservaAEliminar);
	    reservas.remove(reservaAEliminar);

	    guardarReservas();
	    guardarPersonas();

	    msg("RESERVA_ANULADA");
	}

	// ==================== METODOS AUXILIARES ====================
	
	private static ArrayList<Vehiculo> obtenerVehiculosDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
	    ArrayList<Vehiculo> disponibles = new ArrayList<>();
	    
	    for (Vehiculo v : vehiculos) {
	        if (vehiculoDisponible(v.getMatricula(), fechaInicio, fechaFin)) {
	            disponibles.add(v);
	        }
	    }
	    
	    return disponibles;
	}
	
	private static boolean vehiculoDisponible(String matricula, LocalDate fechaInicio, LocalDate fechaFin) {
	    for (Reserva r : reservas) {
	        String matriculaReserva = obtenerMatriculaPorModelo(r.getModelo());
	        
	        if (matriculaReserva != null && matriculaReserva.equalsIgnoreCase(matricula)) {
	            if (fechasSeSuperponen(fechaInicio, fechaFin, r.getFechaini(), r.getFechafin())) {
	                return false;
	            }
	        }
	    }
	    return true;
	}
	
	private static String obtenerMatriculaPorModelo(String modelo) {
	    for (Vehiculo v : vehiculos) {
	        if (v.getModelo().equalsIgnoreCase(modelo)) {
	            return v.getMatricula();
	        }
	    }
	    return null;
	}
	
	private static boolean fechasSeSuperponen(LocalDate inicio1, LocalDate fin1, 
	                                         LocalDate inicio2, LocalDate fin2) {
	    return !inicio1.isAfter(fin2) && !fin1.isBefore(inicio2);
	}

	// ==================== GESTION DE ARCHIVOS ====================

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

	    for (Persona p : personas) {
	        if (p instanceof Cliente) {
	            Cliente c = (Cliente) p;
	            c.limpiarReservas();
	        }
	    }

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


	private static void msg(String etiqueta) {
	    String[] lineas = traducciones.get(etiqueta);

	    if (lineas == null) {
	        System.out.println("Etiqueta no encontrada: " + etiqueta);
	        return;
	    }

	    if (idiomaSeleccionado == 2) {
	        System.out.println(lineas[1]); // inglés
	    } else {
	        System.out.println(lineas[0]); // español
	    }
	}

private static void cargarTraducciones() {
    File archivo = new File("traducciones.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

        String linea;
        while ((linea = br.readLine()) != null) {

            if (linea.startsWith("#")) {
                String etiqueta = linea.substring(1).trim();

                String esp = br.readLine();
                String eng = br.readLine();

                traducciones.put(etiqueta, new String[]{esp, eng});
            }
        }

    } catch (IOException e) {
        System.out.println("Error al cargar traducciones.");
    }
}

}