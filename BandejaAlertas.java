import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class BandejaAlertas implements Iterable<Alerta> {

    private ArrayList<Alerta> alertas;

    public BandejaAlertas() {
        alertas = new ArrayList<Alerta>();
    }

    // guarda la alerta, el orden lo decide Alerta con compareTo
    public void agregar(Alerta alerta) {
        if (alerta == null) {
            throw new IllegalArgumentException("La alerta no puede ser nula");
        }
        alertas.add(alerta);
    }

    // copia de las pendientes ya ordenadas por urgencia
    private ArrayList<Alerta> obtenerPendientesOrdenadas() {
        ArrayList<Alerta> pendientes = new ArrayList<Alerta>();
        for (Alerta alerta : alertas) {
            if (!alerta.isAtendida()) {
                pendientes.add(alerta);
            }
        }
        Collections.sort(pendientes);
        return pendientes;
    }

    // devuelve la mas urgente sin atenderla, null si no hay pendientes
    public Alerta obtenerMasUrgente() {
        ArrayList<Alerta> pendientes = obtenerPendientesOrdenadas();
        if (pendientes.isEmpty()) {
            return null;
        }
        return pendientes.get(0);
    }

    // atiende la mas urgente, si la bandeja esta vacia no cambia nada
    public Alerta atenderMasUrgente(int horaActual) {
        Alerta alerta = obtenerMasUrgente();
        if (alerta == null) {
            throw new IllegalStateException("No hay alertas pendientes en la bandeja");
        }
        alerta.atender(horaActual);
        return alerta;
    }

    // copia de las alertas atendidas fuera de su plazo
    public ArrayList<Alerta> obtenerAtendidasConRetraso() {
        ArrayList<Alerta> conRetraso = new ArrayList<Alerta>();
        for (Alerta alerta : alertas) {
            if (alerta.isAtendida() && alerta.getRetraso() > 0) {
                conRetraso.add(alerta);
            }
        }
        return conRetraso;
    }

    // recorre las pendientes en orden de urgencia sin exponer la lista
    @Override
    public Iterator<Alerta> iterator() {
        return obtenerPendientesOrdenadas().iterator();
    }
}
