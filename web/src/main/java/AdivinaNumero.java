import config.Constants;

import java.io.Serializable;
import java.util.Random;

public class AdivinaNumero implements Serializable {
    private static final int intentosMax = Constants.MAX_INTENTOS;
    private static final int numeroMin = Constants.MIN_NUMERO;
    private static final int numeroMax = Constants.MAX_NUMERO;
    private int numeroSecreto;
    private int intentosRestantes;
    private boolean juegoTerminado;
    private boolean ganado;
    private String mensaje;

    private final Random random = new Random();

    public AdivinaNumero() {
        this.numeroSecreto = random.nextInt(numeroMax - numeroMin + 1) + numeroMin;
        this.intentosRestantes = intentosMax;
        this.juegoTerminado = false;
        this.ganado = false;
        this.mensaje = String.format(Constants.MSG_INICIO, numeroMin, numeroMax, intentosMax);
    }

    public String intentar(int numero) {
        if (juegoTerminado) {
            return mensaje;
        }

        if (numero < numeroMin || numero > numeroMax) {
            return String.format(Constants.MSG_FUERA_RANGO, numeroMin, numeroMax);
        }

        intentosRestantes--;

        if (numero == numeroSecreto) {
            juegoTerminado = true;
            ganado = true;
            mensaje = String.format(Constants.MSG_GANADO, numeroSecreto, intentosMax - intentosRestantes);
        } else if (intentosRestantes == 0) {
            juegoTerminado = true;
            ganado = false;
            mensaje = String.format(Constants.MSG_PERDIDO, numeroSecreto);
        } else {
            if (numero < numeroSecreto) {
                mensaje = String.format(Constants.MSG_MAYOR, intentosRestantes);
            } else {
                mensaje = String.format(Constants.MSG_MENOR, intentosRestantes);
            }
        }

        return mensaje;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public boolean isGanado() {
        return ganado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getNumeroSecreto() {
        return numeroSecreto;
    }

    public int getMaxIntentos() {
        return intentosMax;
    }

    public int getMinNumero() {
        return numeroMin;
    }

    public int getMaxNumero() {
        return numeroMax;
    }
}
