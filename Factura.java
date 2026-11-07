public class Factura {

    private static final double TARIFA_VALLE = 0.85;
    private static final double TARIFA_NORMAL = 1.40;
    private static final double TARIFA_PUNTA = 2.10;

    private double consumoValle;
    private double consumoNormal;
    private double consumoPunta;
    private double creditos;
    private double total;

    public Factura(double consumoValle, double consumoNormal, double consumoPunta, double creditos) {
        if (consumoValle < 0 || consumoNormal < 0 || consumoPunta < 0 || creditos < 0) {
            throw new IllegalArgumentException("Los consumos y creditos no pueden ser negativos");
        }
        this.consumoValle = consumoValle;
        this.consumoNormal = consumoNormal;
        this.consumoPunta = consumoPunta;
        this.creditos = creditos;
        this.total = calcularTotal();
    }

    public double getConsumoValle() {
        return consumoValle;
    }

    public double getConsumoNormal() {
        return consumoNormal;
    }

    public double getConsumoPunta() {
        return consumoPunta;
    }

    public double getMontoValle() {
        return consumoValle * TARIFA_VALLE;
    }

    public double getMontoNormal() {
        return consumoNormal * TARIFA_NORMAL;
    }

    public double getMontoPunta() {
        return consumoPunta * TARIFA_PUNTA;
    }

    public double getCreditos() {
        return creditos;
    }

    public double getTotal() {
        return total;
    }

    // suma de montos menos creditos, nunca menor que cero y a dos decimales
    public double calcularTotal() {
        double montos = getMontoValle() + getMontoNormal() + getMontoPunta();
        double neto = Math.max(0.0, montos - creditos);
        return Math.round(neto * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Valle: " + String.format("%.3f", consumoValle) + " kWh (Q" + String.format("%.2f", getMontoValle()) + ")" +
                ", Normal: " + String.format("%.3f", consumoNormal) + " kWh (Q" + String.format("%.2f", getMontoNormal()) + ")" +
                ", Punta: " + String.format("%.3f", consumoPunta) + " kWh (Q" + String.format("%.2f", getMontoPunta()) + ")" +
                ", Creditos: Q" + String.format("%.2f", creditos) +
                ", Total: Q" + String.format("%.2f", total);
    }
}
