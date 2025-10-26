package problema2;

import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria {
    private double saldo = Constantes.SALDO_INICIAL;
    private List<String> operaciones = new ArrayList<>();

    public boolean retirar(double cantidad) {
        if (cantidad <= saldo && cantidad > 0) {
            saldo -= cantidad;
            operaciones.add("Retirada: " + cantidad);
            return true;
        } else {
            return false;
        }
    }
    public void ingresar(double cantidad) {
        saldo += cantidad;
        operaciones.add("Ingreso: " + cantidad);
    }
    public double consultarSaldo() {
        return saldo;
    }
    public List<String> obtenerHistorial() {
        return operaciones;
    }
}
