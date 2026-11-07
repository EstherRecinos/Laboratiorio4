public class ClimatizadorInverter extends Climatizador implements Regulable {

    public ClimatizadorInverter(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
    }

    // mismo ajuste del climatizador pero desde nivel 10
    @Override
    public void ajustarNivel(int nivel) {
        establecerNivel(nivel, 10);
    }

    // el 80% de lo que consumiria un climatizador normal
    @Override
    public double calcularConsumoHora() {
        return super.calcularConsumoHora() * 0.80;
    }
}
