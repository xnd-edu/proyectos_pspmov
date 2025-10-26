package problema4;

import java.util.Random;

public enum TipoVehiculo {
    NORMAL(1.0),
    VIP(2.0);

    private final double tarifaPorMinuto;

    TipoVehiculo(double tarifaPorMinuto) {
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public double getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public static TipoVehiculo aleatorio() {
        TipoVehiculo[] tipos = TipoVehiculo.values();
        return tipos[new Random().nextInt(tipos.length)];
    }
}
