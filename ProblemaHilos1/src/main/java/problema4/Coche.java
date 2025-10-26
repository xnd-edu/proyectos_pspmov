package problema4;

public class Coche implements Runnable {
    private TipoVehiculo tipoVehiculo;
    private int id;

    public Coche(int id) {
        this.id = id;
        this.tipoVehiculo = TipoVehiculo.aleatorio();
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public int getId() {
        return id;
    }

    public void run() {
        // Lógica del coche (llegar, aparcar, salir)
    }
}
