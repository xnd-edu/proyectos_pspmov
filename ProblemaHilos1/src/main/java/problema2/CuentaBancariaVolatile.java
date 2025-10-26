package problema2;

import java.util.List;

public class CuentaBancariaVolatile extends CuentaBancaria {
    private volatile double saldo = Constantes.SALDO_INICIAL;

    public boolean retirar(double cantidad) {
        return super.retirar(cantidad);
    }
    public void ingresar(double cantidad) {
        super.ingresar(cantidad);
    }
}
