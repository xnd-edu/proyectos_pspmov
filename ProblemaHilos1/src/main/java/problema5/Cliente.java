package problema5;

import java.util.Random;

public class Cliente {
    private int id;
    private boolean premium;
    private Descarga descarga;

    public Cliente(int id, boolean premium) {
        this.id = id;
        this.premium = premium;
        this.descarga = null;
    }

    public int getId() {
        return id;
    }

    public boolean isPremium() {
        return premium;
    }

    public Descarga getDescarga() {
        return descarga;
    }

    public void setDescarga(Descarga descarga) {
        this.descarga = descarga;
    }
}
