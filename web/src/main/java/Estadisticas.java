import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Estadisticas implements Serializable {
    private List<AdivinaNumero> listaPartidas;
    private final String usuario;

    public Estadisticas(String usuario) {
        this.listaPartidas = new ArrayList<>();
        this.usuario = usuario;
    }

    public void agregarPartida(AdivinaNumero partida) {
        listaPartidas.add(partida);
    }

    public String getUsuario() {
        return usuario;
    }

    public int getPartidasJugadas() {
        return listaPartidas.size();
    }

    public int getPartidasGanadas() {
        return (int) listaPartidas.stream().filter(AdivinaNumero::isGanado).count();
    }

    public double getPorcentajeGanadas() {
        if (getPartidasJugadas() == 0) {
            return 0.0;
        }
        return (getPartidasGanadas() * 100.0) / getPartidasJugadas();
    }
}
