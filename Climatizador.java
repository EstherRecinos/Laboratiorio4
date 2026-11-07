public class Climatizador extends Dispositivo implements Regulable, Medible, GeneradorAlerta {

    private int nivel;

    public Climatizador(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
        this.nivel = 50;
    }

    public int getNivel() {
        return nivel;
    }

    // nivel entre 20 y 100
    @Override
    public void ajustarNivel(int nivel) {
        establecerNivel(nivel, 20);
    }

    // valida y guarda el nivel, el inverter reutiliza esto con otro minimo
    protected void establecerNivel(int nivel, int minimo) {
        if (!isEncendido()) {
            throw new IllegalStateException("No se puede ajustar el nivel de un dispositivo apagado");
        }
        if (nivel < minimo || nivel > 100) {
            throw new IllegalArgumentException("El nivel debe estar entre " + minimo + " y 100");
        }
        this.nivel = nivel;
    }

    // valida la temperatura y deja que generarAlerta decida si hay alerta
    @Override
    public Alerta registrarLectura(double temperatura, int horaSimulacion) {
        if (!isEncendido()) {
            throw new IllegalStateException("No se puede registrar una lectura en un dispositivo apagado");
        }
        if (temperatura < -10 || temperatura > 60) {
            throw new IllegalArgumentException("La temperatura debe estar entre -10 y 60 grados Celsius");
        }
        return generarAlerta(temperatura, horaSimulacion);
    }

    // umbrales de 28, 32 y 40 grados, devuelve null si no llega a ninguno
    @Override
    public Alerta generarAlerta(double temperatura, int horaSimulacion) {
        if (temperatura >= 40) {
            return new Alerta(this, horaSimulacion, Severidad.CRITICA, "Temperatura de " + temperatura + " C");
        }
        if (temperatura >= 32) {
            return new Alerta(this, horaSimulacion, Severidad.ALTA, "Temperatura de " + temperatura + " C");
        }
        if (temperatura >= 28) {
            return new Alerta(this, horaSimulacion, Severidad.BAJA, "Temperatura de " + temperatura + " C");
        }
        return null;
    }

    // potencia por (0.30 + 0.70 * nivel / 100), en kwh
    @Override
    public double calcularConsumoHora() {
        return getPotenciaNominal() * (0.30 + 0.70 * nivel / 100.0) / 1000.0;
    }

    @Override
    public String toString() {
        return super.toString() + ", Nivel: " + nivel;
    }
}
