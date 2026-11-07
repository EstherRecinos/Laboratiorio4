public class Luminaria extends Dispositivo implements Regulable {

    private int nivel;

    public Luminaria(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
        this.nivel = 100;
    }

    public int getNivel() {
        return nivel;
    }

    // solo se ajusta encendida y con nivel entre 10 y 100
    @Override
    public void ajustarNivel(int nivel) {
        if (!isEncendido()) {
            throw new IllegalStateException("No se puede ajustar el nivel de un dispositivo apagado");
        }
        if (nivel < 10 || nivel > 100) {
            throw new IllegalArgumentException("El nivel de una luminaria debe estar entre 10 y 100");
        }
        this.nivel = nivel;
    }

    // potencia por nivel, en kwh
    @Override
    public double calcularConsumoHora() {
        return getPotenciaNominal() * nivel / 100.0 / 1000.0;
    }

    @Override
    public String toString() {
        return super.toString() + ", Nivel: " + nivel;
    }
}
