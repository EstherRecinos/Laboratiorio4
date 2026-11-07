public enum Severidad {
    BAJA(1, 24, 0.50),
    ALTA(2, 4, 2.00),
    CRITICA(3, 1, 5.00);

    private int prioridad;
    private int plazoAtencion;
    private double creditoHora;

    private Severidad(int prioridad, int plazoAtencion, double creditoHora) {
        this.prioridad = prioridad;
        this.plazoAtencion = plazoAtencion;
        this.creditoHora = creditoHora;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getPlazoAtencion() {
        return plazoAtencion;
    }

    public double getCreditoHora() {
        return creditoHora;
    }
}
