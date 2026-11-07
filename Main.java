import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private TorreGT torre;
    private Scanner scanner;

    public Main() {
        torre = new TorreGT();
        scanner = new Scanner(System.in);
        cargarDatosIniciales();
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.iniciar();
    }

    // datos de demostracion, sin consumos, alertas ni facturas
    private void cargarDatosIniciales() {
        Oficina of1 = new Oficina("OF01", "Estudio Maya");
        Oficina of2 = new Oficina("OF02", "Clinica Quetzal");
        Oficina of3 = new Oficina("OF03", "Bufete Antigua");
        torre.registrarOficina(of1);
        torre.registrarOficina(of2);
        torre.registrarOficina(of3);

        torre.registrarDispositivo(new Luminaria("LU01", of1, 60.0));
        torre.registrarDispositivo(new Luminaria("LU02", of2, 120.0));

        // CL01 y CI01 con 1500 W repiten los ejemplos del enunciado
        torre.registrarDispositivo(new Climatizador("CL01", of1, 1500.0));
        torre.registrarDispositivo(new Climatizador("CL02", of3, 2000.0));
        torre.registrarDispositivo(new ClimatizadorInverter("CI01", of2, 1500.0));

        // CI02 queda apagado pero conserva el nivel 15, menor que 20
        ClimatizadorInverter inverter = new ClimatizadorInverter("CI02", of3, 1800.0);
        inverter.encender();
        inverter.ajustarNivel(15);
        inverter.apagar();
        torre.registrarDispositivo(inverter);

        torre.registrarDispositivo(new SensorHumo("SH01", of1, 10.0));
        torre.registrarDispositivo(new SensorHumo("SH02", of2, 12.0));

        torre.registrarDispositivo(new CamaraSeguridad("CA01", of1, 25.0));
        torre.registrarDispositivo(new CamaraSeguridad("CA02", of3, 30.0));
    }

    // menu en ciclo hasta que el usuario decide salir
    private void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    registrarOficina();
                    break;
                case 2:
                    registrarDispositivo();
                    break;
                case 3:
                    encenderDispositivo();
                    break;
                case 4:
                    apagarDispositivo();
                    break;
                case 5:
                    ajustarNivel();
                    break;
                case 6:
                    registrarLectura();
                    break;
                case 7:
                    registrarMovimiento();
                    break;
                case 8:
                    avanzarHora();
                    break;
                case 9:
                    atenderAlerta();
                    break;
                case 10:
                    consultarOficinas();
                    break;
                case 11:
                    consultarDispositivos();
                    break;
                case 12:
                    System.out.println(torre.generarReporteAlertas());
                    break;
                case 13:
                    System.out.println(torre.generarReporteFacturas());
                    break;
                case 14:
                    salir = true;
                    System.out.println("Saliendo del sistema");
                    break;
                default:
                    System.out.println("Opcion invalida, intente nuevamente");
            }
        }
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== TORREGT =====");
        System.out.println("Hora de simulacion: " + torre.getHoraSimulacion() + " (hora del dia: " + torre.getHoraSimulacion() % 24 + ")");
        System.out.println("1. Registrar oficina");
        System.out.println("2. Registrar dispositivo");
        System.out.println("3. Encender dispositivo");
        System.out.println("4. Apagar dispositivo");
        System.out.println("5. Ajustar nivel");
        System.out.println("6. Registrar lectura");
        System.out.println("7. Registrar movimiento");
        System.out.println("8. Avanzar una hora");
        System.out.println("9. Atender la alerta mas urgente");
        System.out.println("10. Consultar oficinas");
        System.out.println("11. Consultar dispositivos de una oficina");
        System.out.println("12. Consultar alertas");
        System.out.println("13. Consultar facturas");
        System.out.println("14. Salir");
    }

    private void registrarOficina() {
        try {
            String codigo = leerTexto("Codigo de la oficina: ");
            String inquilino = leerTexto("Nombre del inquilino: ");
            torre.registrarOficina(new Oficina(codigo, inquilino));
            System.out.println("Oficina registrada correctamente");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // la oficina debe existir, el tipo se elige aqui y las validaciones las hace cada clase
    private void registrarDispositivo() {
        try {
            System.out.println("Tipo de dispositivo:");
            System.out.println("1. Luminaria");
            System.out.println("2. Climatizador");
            System.out.println("3. Climatizador inverter");
            System.out.println("4. Sensor de humo");
            System.out.println("5. Camara de seguridad");
            int tipo = leerEntero("Tipo: ");
            String codigo = leerTexto("Codigo del dispositivo: ");
            String codigoOficina = leerTexto("Codigo de la oficina: ");
            Oficina oficina = torre.buscarOficina(codigoOficina);
            if (oficina == null) {
                System.out.println("No existe una oficina con ese codigo");
                return;
            }
            double potencia = leerDouble("Potencia nominal en watts: ");
            Dispositivo dispositivo;
            if (tipo == 1) {
                dispositivo = new Luminaria(codigo, oficina, potencia);
            } else if (tipo == 2) {
                dispositivo = new Climatizador(codigo, oficina, potencia);
            } else if (tipo == 3) {
                dispositivo = new ClimatizadorInverter(codigo, oficina, potencia);
            } else if (tipo == 4) {
                dispositivo = new SensorHumo(codigo, oficina, potencia);
            } else if (tipo == 5) {
                dispositivo = new CamaraSeguridad(codigo, oficina, potencia);
            } else {
                System.out.println("Tipo de dispositivo invalido");
                return;
            }
            torre.registrarDispositivo(dispositivo);
            System.out.println("Dispositivo registrado correctamente");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void encenderDispositivo() {
        try {
            String codigo = leerTexto("Codigo del dispositivo: ");
            torre.encenderDispositivo(codigo);
            System.out.println("Dispositivo encendido");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void apagarDispositivo() {
        try {
            String codigo = leerTexto("Codigo del dispositivo: ");
            torre.apagarDispositivo(codigo);
            System.out.println("Dispositivo apagado");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void ajustarNivel() {
        try {
            String codigo = leerTexto("Codigo del dispositivo: ");
            int nivel = leerEntero("Nuevo nivel: ");
            torre.ajustarNivelDispositivo(codigo, nivel);
            System.out.println("Nivel ajustado correctamente");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarLectura() {
        try {
            String codigo = leerTexto("Codigo del dispositivo: ");
            double valor = leerDouble("Valor de la lectura (grados Celsius o ppm segun el dispositivo): ");
            Alerta alerta = torre.registrarLectura(codigo, valor);
            mostrarResultadoAlerta(alerta);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarMovimiento() {
        try {
            String codigo = leerTexto("Codigo de la camara: ");
            Alerta alerta = torre.registrarMovimiento(codigo);
            mostrarResultadoAlerta(alerta);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // null significa que la lectura no llego a ningun umbral
    private void mostrarResultadoAlerta(Alerta alerta) {
        if (alerta == null) {
            System.out.println("Registro correcto, no se genero ninguna alerta");
        } else {
            System.out.println("Se genero una alerta: " + alerta.toString());
        }
    }

    private void avanzarHora() {
        torre.avanzarHora();
        System.out.println("Hora de simulacion: " + torre.getHoraSimulacion());
    }

    private void atenderAlerta() {
        try {
            Alerta alerta = torre.atenderAlerta();
            System.out.println("Alerta atendida: " + alerta.toString());
            if (alerta.getRetraso() > 0) {
                System.out.println("Credito acreditado a la oficina: Q" + String.format("%.2f", alerta.calcularCredito()));
            } else {
                System.out.println("Se atendio dentro del plazo");
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void consultarOficinas() {
        ArrayList<Oficina> oficinas = torre.getOficinas();
        if (oficinas.isEmpty()) {
            System.out.println("No hay oficinas registradas");
            return;
        }
        for (Oficina oficina : oficinas) {
            System.out.println(oficina.toString());
        }
    }

    private void consultarDispositivos() {
        try {
            String codigoOficina = leerTexto("Codigo de la oficina: ");
            System.out.println(torre.generarReporteDispositivos(codigoOficina));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // si el formato es incorrecto limpia la entrada y vuelve a preguntar
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida, debe ingresar un numero entero");
                scanner.nextLine();
            }
        }
    }

    // igual que leerEntero pero para decimales
    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida, debe ingresar un numero");
                scanner.nextLine();
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}
