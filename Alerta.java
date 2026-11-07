public class Alerta implements Comparable<Alerta> {

    private static int contadorCodigos = 0;

    private int codigo;
    private Dispositivo dispositivo;
    private int horaGeneracion;
    private Severidad severidad;
    private String mensaje;
    private boolean atendida;
    private int retraso;

    public Alerta(Dispositivo dispositivo, int horaGeneracion, Severidad severidad, String mensaje) {
        if (dispositivo == null) {
            throw new IllegalArgumentException("La alerta debe tener un dispositivo");
        }
        if (severidad == null) {
            throw new IllegalArgumentException("La alerta debe tener una severidad");
        }
        if (horaGeneracion < 0) {
            throw new IllegalArgumentException("La hora de generacion no puede ser negativa");
        }
        // el codigo solo avanza si la alerta se creo bien
        contadorCodigos++;
        this.codigo = contadorCodigos;
        this.dispositivo = dispositivo;
        this.horaGeneracion = horaGeneracion;
        this.severidad = severidad;
        this.mensaje = mensaje;
        this.atendida = false;
        this.retraso = 0;
    }

    public int getCodigo() {
        return codigo;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public int getHoraGeneracion() {
        return horaGeneracion;
    }

    public Severidad getSeveridad() {
        return severidad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public int getRetraso() {
        return retraso;
    }

    // marca la alerta como atendida y guarda las horas de retraso sobre el plazo
    public int atender(int horaActual) {
        if (atendida) {
            throw new IllegalStateException("La alerta ya fue atendida");
        }
        int transcurridas = horaActual - horaGeneracion;
        retraso = Math.max(0, transcurridas - severidad.getPlazoAtencion());
        atendida = true;
        return retraso;
    }

    // credito por las horas de retraso, segun la severidad
    public double calcularCredito() {
        return retraso * severidad.getCreditoHora();
    }

    // mas urgente primero: severidad mayor, luego la mas antigua, luego codigo menor
    @Override
    public int compareTo(Alerta otra) {
        int porSeveridad = Integer.compare(otra.severidad.getPrioridad(), severidad.getPrioridad());
        if (porSeveridad != 0) {
            return porSeveridad;
        }
        int porHora = Integer.compare(horaGeneracion, otra.horaGeneracion);
        if (porHora != 0) {
            return porHora;
        }
        return Integer.compare(codigo, otra.codigo);
    }

    @Override
    public String toString() {
        return "Alerta " + codigo +
                ", Severidad: " + severidad +
                ", Dispositivo: " + dispositivo.getCodigo() +
                ", Hora: " + horaGeneracion +
                ", Mensaje: " + mensaje +
                ", Estado: " + (atendida ? "Atendida, retraso de " + retraso + " h" : "Pendiente");
    }
}
