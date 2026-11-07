import java.util.ArrayList;

public class TorreGT {

    private ArrayList<Oficina> oficinas;
    private BandejaAlertas bandeja;
    private int horaSimulacion;

    public TorreGT() {
        oficinas = new ArrayList<Oficina>();
        bandeja = new BandejaAlertas();
        horaSimulacion = 0;
    }

    public int getHoraSimulacion() {
        return horaSimulacion;
    }

    public ArrayList<Oficina> getOficinas() {
        return new ArrayList<Oficina>(oficinas);
    }

    // registra la oficina si su codigo no existe
    public void registrarOficina(Oficina oficina) {
        if (oficina == null) {
            throw new IllegalArgumentException("La oficina no puede ser nula");
        }
        if (buscarOficina(oficina.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe una oficina con ese codigo");
        }
        oficinas.add(oficina);
    }

    // registra el dispositivo en su oficina si su codigo no existe en ninguna
    public void registrarDispositivo(Dispositivo dispositivo) {
        if (dispositivo == null) {
            throw new IllegalArgumentException("El dispositivo no puede ser nulo");
        }
        if (buscarDispositivo(dispositivo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un dispositivo con ese codigo");
        }
        dispositivo.getOficina().agregarDispositivo(dispositivo);
    }

    // devuelve null si no existe
    public Oficina buscarOficina(String codigo) {
        for (Oficina oficina : oficinas) {
            if (oficina.getCodigo().equals(codigo)) {
                return oficina;
            }
        }
        return null;
    }

    // busca en todas las oficinas, devuelve null si no existe
    public Dispositivo buscarDispositivo(String codigo) {
        for (Oficina oficina : oficinas) {
            for (Dispositivo dispositivo : oficina.obtenerDispositivosOrdenados()) {
                if (dispositivo.getCodigo().equals(codigo)) {
                    return dispositivo;
                }
            }
        }
        return null;
    }

    // busca el dispositivo o rechaza la operacion si no existe
    private Dispositivo obtenerDispositivo(String codigo) {
        Dispositivo dispositivo = buscarDispositivo(codigo);
        if (dispositivo == null) {
            throw new IllegalArgumentException("No existe un dispositivo con ese codigo");
        }
        return dispositivo;
    }

    public void encenderDispositivo(String codigo) {
        obtenerDispositivo(codigo).encender();
    }

    // el sensor de humo rechaza esta operacion por su cuenta
    public void apagarDispositivo(String codigo) {
        obtenerDispositivo(codigo).apagar();
    }

    // solo los que cumplen la capacidad Regulable, el dispositivo valida estado y rango
    public void ajustarNivelDispositivo(String codigo, int nivel) {
        Dispositivo dispositivo = obtenerDispositivo(codigo);
        if (!(dispositivo instanceof Regulable)) {
            throw new IllegalArgumentException("El dispositivo no es regulable");
        }
        ((Regulable) dispositivo).ajustarNivel(nivel);
    }

    // solo los que cumplen la capacidad Medible, la alerta se guarda hasta que la lectura sea valida
    public Alerta registrarLectura(String codigo, double valor) {
        Dispositivo dispositivo = obtenerDispositivo(codigo);
        if (!(dispositivo instanceof Medible)) {
            throw new IllegalArgumentException("El dispositivo no mide lecturas");
        }
        Alerta alerta = ((Medible) dispositivo).registrarLectura(valor, horaSimulacion);
        if (alerta != null) {
            bandeja.agregar(alerta);
        }
        return alerta;
    }

    // solo los que cumplen la capacidad DetectorMovimiento
    public Alerta registrarMovimiento(String codigo) {
        Dispositivo dispositivo = obtenerDispositivo(codigo);
        if (!(dispositivo instanceof DetectorMovimiento)) {
            throw new IllegalArgumentException("El dispositivo no detecta movimientos");
        }
        Alerta alerta = ((DetectorMovimiento) dispositivo).registrarMovimiento(horaSimulacion);
        if (alerta != null) {
            bandeja.agregar(alerta);
        }
        return alerta;
    }

    // consume la hora actual, avanza y cierra el dia al pasar de la hora 23 a la 0
    public void avanzarHora() {
        int horaDelDia = horaSimulacion % 24;
        for (Oficina oficina : oficinas) {
            oficina.acumularConsumoHora(horaDelDia);
        }
        horaSimulacion++;
        if (horaSimulacion % 24 == 0) {
            generarFacturas();
        }
    }

    // atiende la mas urgente y acredita el retraso a la oficina del dispositivo
    public Alerta atenderAlerta() {
        Alerta alerta = bandeja.atenderMasUrgente(horaSimulacion);
        double credito = alerta.calcularCredito();
        if (credito > 0) {
            alerta.getDispositivo().getOficina().agregarCredito(credito);
        }
        return alerta;
    }

    // cierra el dia de cada oficina
    public void generarFacturas() {
        for (Oficina oficina : oficinas) {
            oficina.cerrarDia();
        }
    }

    // dispositivos de una oficina ordenados por codigo
    public String generarReporteDispositivos(String codigoOficina) {
        Oficina oficina = buscarOficina(codigoOficina);
        if (oficina == null) {
            throw new IllegalArgumentException("No existe una oficina con ese codigo");
        }
        StringBuilder reporte = new StringBuilder();
        reporte.append("===== DISPOSITIVOS DE ").append(oficina.getCodigo()).append(" =====\n");
        ArrayList<Dispositivo> dispositivos = oficina.obtenerDispositivosOrdenados();
        if (dispositivos.isEmpty()) {
            reporte.append("La oficina no tiene dispositivos\n");
        }
        for (Dispositivo dispositivo : dispositivos) {
            reporte.append(dispositivo.toString()).append("\n");
        }
        return reporte.toString();
    }

    // bandeja en orden de urgencia y alertas atendidas con retraso
    public String generarReporteAlertas() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("===== BANDEJA DE ALERTAS (por urgencia) =====\n");
        boolean hayPendientes = false;
        for (Alerta alerta : bandeja) {
            hayPendientes = true;
            reporte.append(alerta.toString()).append("\n");
        }
        if (!hayPendientes) {
            reporte.append("No hay alertas pendientes\n");
        }
        reporte.append("===== ALERTAS ATENDIDAS CON RETRASO =====\n");
        ArrayList<Alerta> conRetraso = bandeja.obtenerAtendidasConRetraso();
        if (conRetraso.isEmpty()) {
            reporte.append("No hay alertas atendidas con retraso\n");
        }
        for (Alerta alerta : conRetraso) {
            reporte.append(alerta.toString())
                    .append(", Credito: Q").append(String.format("%.2f", alerta.calcularCredito())).append("\n");
        }
        return reporte.toString();
    }

    // facturas por oficina, totales por franja y creditos aplicados
    public String generarReporteFacturas() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("===== FACTURAS POR OFICINA =====\n");
        double totalValle = 0.0;
        double totalNormal = 0.0;
        double totalPunta = 0.0;
        double totalCreditos = 0.0;
        boolean hayFacturas = false;
        for (Oficina oficina : oficinas) {
            for (Factura factura : oficina.getFacturas()) {
                hayFacturas = true;
                reporte.append("Oficina ").append(oficina.getCodigo()).append(" - ").append(factura.toString()).append("\n");
                totalValle += factura.getMontoValle();
                totalNormal += factura.getMontoNormal();
                totalPunta += factura.getMontoPunta();
                totalCreditos += factura.getCreditos();
            }
        }
        if (!hayFacturas) {
            reporte.append("No hay facturas generadas\n");
            return reporte.toString();
        }
        reporte.append("===== TOTALES FACTURADOS POR FRANJA =====\n");
        reporte.append("Valle: Q").append(String.format("%.2f", totalValle)).append("\n");
        reporte.append("Normal: Q").append(String.format("%.2f", totalNormal)).append("\n");
        reporte.append("Punta: Q").append(String.format("%.2f", totalPunta)).append("\n");
        reporte.append("Creditos aplicados: Q").append(String.format("%.2f", totalCreditos)).append("\n");
        return reporte.toString();
    }
}
