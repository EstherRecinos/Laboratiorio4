public class CamaraSeguridad extends Dispositivo implements GeneradorAlerta, DetectorMovimiento {

    public CamaraSeguridad(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
    }

    // solo con la camara encendida, pasa la hora del dia para evaluar el horario laboral
    @Override
    public Alerta registrarMovimiento(int horaSimulacion) {
        if (!isEncendido()) {
            throw new IllegalStateException("No se puede registrar un movimiento en una camara apagada");
        }
        return generarAlerta(horaSimulacion % 24, horaSimulacion);
    }

    // aqui la medida es la hora del dia, fuera de 7:00 a 18:59 genera alerta alta
    @Override
    public Alerta generarAlerta(double horaDelDia, int horaSimulacion) {
        if (horaDelDia < 7 || horaDelDia >= 19) {
            return new Alerta(this, horaSimulacion, Severidad.ALTA, "Movimiento fuera del horario laboral a las " + (int) horaDelDia + ":00");
        }
        return null;
    }

    // consume su potencia nominal completa, en kwh
    @Override
    public double calcularConsumoHora() {
        return getPotenciaNominal() / 1000.0;
    }
}
