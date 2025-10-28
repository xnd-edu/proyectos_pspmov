import config.Constants;

import java.util.Random;

public class AdivinaNumero {
    private static final int intentosMax = Constants.MAX_INTENTOS;
    private static final int numeroMin = Constants.MIN_NUMERO;
    private static final int numeroMax = Constants.MAX_NUMERO;
    private int numeroSecreto;
    private int intentosRestantes;
    private boolean juegoTerminado;
    private boolean ganado;
    private String mensaje;

    public AdivinaNumero() {
        Random random = new Random();
        this.numeroSecreto = random.nextInt(numeroMax - numeroMin + 1) + numeroMin;
        this.intentosRestantes = intentosMax;
        this.juegoTerminado = false;
        this.ganado = false;
        this.mensaje = "¡Adivina el número entre " + numeroMin + " y " + numeroMax + "! Tienes " + intentosMax + " intentos.";
    }

    public String intentar(int numero) {
        if (juegoTerminado) {
            return mensaje;
        }

        if (numero < numeroMin || numero > numeroMax) {
            return "El número debe estar entre " + numeroMin + " y " + numeroMax;
        }

        intentosRestantes--;

        if (numero == numeroSecreto) {
            juegoTerminado = true;
            ganado = true;
            mensaje = "¡Felicidades! Has adivinado el número " + numeroSecreto + " en " + (intentosMax - intentosRestantes) + " intentos.";
        } else if (intentosRestantes == 0) {
            juegoTerminado = true;
            ganado = false;
            mensaje = "¡Game Over! Te has quedado sin intentos. El número era " + numeroSecreto;
        } else {
            if (numero < numeroSecreto) {
                mensaje = "El número secreto es MAYOR. Te quedan " + intentosRestantes + " intentos.";
            } else {
                mensaje = "El número secreto es MENOR. Te quedan " + intentosRestantes + " intentos.";
            }
        }

        return mensaje;
    }

    public void reiniciar() {
        Random random = new Random();
        this.numeroSecreto = random.nextInt(numeroMax - numeroMin + 1) + numeroMin;
        this.intentosRestantes = intentosMax;
        this.juegoTerminado = false;
        this.ganado = false;
        this.mensaje = "¡Adivina el número entre " + numeroMin + " y " + numeroMax + "! Tienes " + intentosMax + " intentos.";
    }

    // Getters
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

    public static int getMaxIntentos() {
        return intentosMax;
    }

    public static int getMinNumero() {
        return numeroMin;
    }

    public static int getMaxNumero() {
        return numeroMax;
    }
}
