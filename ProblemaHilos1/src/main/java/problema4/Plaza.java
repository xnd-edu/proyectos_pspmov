package problema4;

public class Plaza {
    public int id;
    private boolean vip;
    private Coche coche;

    public Plaza(int id, boolean vip) {
        this.id = id;
        this.vip = vip;
        this.coche = null;
    }

    public int getId() {
        return id;
    }

    public boolean isVip() {
        return vip;
    }

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }
}
