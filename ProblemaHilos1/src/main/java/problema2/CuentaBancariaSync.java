package problema2;

public class CuentaBancariaSync extends CuentaBancaria {
    public synchronized boolean retirar(double cantidad) {
        return super.retirar(cantidad);
    }
    public synchronized void ingresar(double cantidad) {
        super.ingresar(cantidad);
    }
}
