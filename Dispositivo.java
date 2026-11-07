public abstract class Dispositivo {

    private String codigo;
    private Oficina oficina;
    private double potenciaNominal;
    private boolean encendido;
    private double consumoDia;

    public Dispositivo(String codigo, Oficina oficina, double potenciaNominal) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del dispositivo no puede estar vacio");
        }
        if (oficina == null) {
            throw new IllegalArgumentException("El dispositivo debe pertenecer a una oficina");
        }
        if (potenciaNominal <= 0) {
            throw new IllegalArgumentException("La potencia nominal debe ser mayor que cero");
        }
        this.codigo = codigo;
        this.oficina = oficina;
        this.potenciaNominal = potenciaNominal;
        this.encendido = false;
        this.consumoDia = 0.0;
    }

    public String getCodigo() {
        return codigo;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public double getPotenciaNominal() {
        return potenciaNominal;
    }

    public boolean isEncendido() {
        return encendido;
    }

    public double getConsumoDia() {
        return consumoDia;
    }

    // enciende el dispositivo
    public void encender() {
        encendido = true;
    }

    // apaga el dispositivo, el sensor de humo lo sobrescribe para rechazarlo
    public void apagar() {
        encendido = false;
    }

    // cada tipo define su formula, devuelve los kwh de una hora encendido
    public abstract double calcularConsumoHora();

    // suma el consumo de una hora al acumulado del dia
    public void acumularConsumo(double kwh) {
        if (kwh < 0) {
            throw new IllegalArgumentException("El consumo no puede ser negativo");
        }
        consumoDia += kwh;
    }

    // deja el acumulado en cero al cerrar el dia
    public void reiniciarConsumo() {
        consumoDia = 0.0;
    }

    @Override
    public String toString() {
        return "Tipo: " + getClass().getSimpleName() +
                ", Codigo: " + codigo +
                ", Oficina: " + oficina.getCodigo() +
                ", Potencia nominal: " + potenciaNominal + " W" +
                ", Estado: " + (encendido ? "Encendido" : "Apagado") +
                ", Consumo del dia: " + String.format("%.3f", consumoDia) + " kWh";
    }
}
