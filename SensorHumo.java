public class SensorHumo extends Dispositivo implements Medible, GeneradorAlerta {

    public SensorHumo(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
        encender();
    }

    // el sensor siempre permanece encendido
    @Override
    public void apagar() {
        throw new IllegalStateException("El sensor de humo no se puede apagar");
    }

    // valida los ppm y deja que generarAlerta decida si hay alerta
    @Override
    public Alerta registrarLectura(double ppm, int horaSimulacion) {
        if (!isEncendido()) {
            throw new IllegalStateException("No se puede registrar una lectura en un dispositivo apagado");
        }
        if (ppm < 0 || ppm > 1000) {
            throw new IllegalArgumentException("La concentracion de humo debe estar entre 0 y 1000 ppm");
        }
        return generarAlerta(ppm, horaSimulacion);
    }

    // umbrales de 150 y 300 ppm, devuelve null si no llega a ninguno
    @Override
    public Alerta generarAlerta(double ppm, int horaSimulacion) {
        if (ppm >= 300) {
            return new Alerta(this, horaSimulacion, Severidad.CRITICA, "Humo de " + ppm + " ppm");
        }
        if (ppm >= 150) {
            return new Alerta(this, horaSimulacion, Severidad.ALTA, "Humo de " + ppm + " ppm");
        }
        return null;
    }

    // consume su potencia nominal completa, en kwh
    @Override
    public double calcularConsumoHora() {
        return getPotenciaNominal() / 1000.0;
    }
}
