import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Oficina {

    private String codigo;
    private String inquilino;
    private ArrayList<Dispositivo> dispositivos;
    private double creditos;
    private ArrayList<Factura> facturas;
    private double consumoValle;
    private double consumoNormal;
    private double consumoPunta;

    public Oficina(String codigo, String inquilino) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de la oficina no puede estar vacio");
        }
        if (inquilino == null || inquilino.trim().isEmpty()) {
            throw new IllegalArgumentException("El inquilino no puede estar vacio");
        }
        this.codigo = codigo;
        this.inquilino = inquilino;
        this.dispositivos = new ArrayList<Dispositivo>();
        this.creditos = 0.0;
        this.facturas = new ArrayList<Factura>();
        this.consumoValle = 0.0;
        this.consumoNormal = 0.0;
        this.consumoPunta = 0.0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getInquilino() {
        return inquilino;
    }

    public double getCreditos() {
        return creditos;
    }

    public ArrayList<Factura> getFacturas() {
        return new ArrayList<Factura>(facturas);
    }

    // agrega un dispositivo que fue creado para esta oficina
    public void agregarDispositivo(Dispositivo dispositivo) {
        if (dispositivo == null) {
            throw new IllegalArgumentException("El dispositivo no puede ser nulo");
        }
        if (dispositivo.getOficina() != this) {
            throw new IllegalArgumentException("El dispositivo pertenece a otra oficina");
        }
        dispositivos.add(dispositivo);
    }

    // suma un credito al dia en curso
    public void agregarCredito(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El credito debe ser mayor que cero");
        }
        creditos += monto;
    }

    // cada dispositivo encendido calcula su consumo y se acumula en la franja de la hora
    public void acumularConsumoHora(int horaDelDia) {
        for (Dispositivo dispositivo : dispositivos) {
            if (dispositivo.isEncendido()) {
                double kwh = dispositivo.calcularConsumoHora();
                dispositivo.acumularConsumo(kwh);
                registrarConsumo(kwh, horaDelDia);
            }
        }
    }

    // valle de 22 a 5, normal de 6 a 17 y punta de 18 a 21
    private void registrarConsumo(double kwh, int horaDelDia) {
        if (horaDelDia >= 22 || horaDelDia < 6) {
            consumoValle += kwh;
        } else if (horaDelDia < 18) {
            consumoNormal += kwh;
        } else {
            consumoPunta += kwh;
        }
    }

    // genera la factura del dia y deja todos los acumulados en cero
    public void cerrarDia() {
        Factura factura = new Factura(consumoValle, consumoNormal, consumoPunta, creditos);
        facturas.add(factura);
        consumoValle = 0.0;
        consumoNormal = 0.0;
        consumoPunta = 0.0;
        creditos = 0.0;
        for (Dispositivo dispositivo : dispositivos) {
            dispositivo.reiniciarConsumo();
        }
    }

    // copia de los dispositivos ordenada por codigo
    public ArrayList<Dispositivo> obtenerDispositivosOrdenados() {
        ArrayList<Dispositivo> ordenados = new ArrayList<Dispositivo>(dispositivos);
        Collections.sort(ordenados, new Comparator<Dispositivo>() {
            @Override
            public int compare(Dispositivo a, Dispositivo b) {
                return a.getCodigo().compareTo(b.getCodigo());
            }
        });
        return ordenados;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo +
                ", Inquilino: " + inquilino +
                ", Dispositivos: " + dispositivos.size() +
                ", Creditos del dia: Q" + String.format("%.2f", creditos);
    }
}
