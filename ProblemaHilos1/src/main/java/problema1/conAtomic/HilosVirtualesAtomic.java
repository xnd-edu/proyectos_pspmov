package problema1.conAtomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import problema1.util.Constantes;
import problema1.util.ContadorVisitas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HilosVirtualesAtomic {
    private static final Logger logger = LoggerFactory.getLogger(HilosVirtualesAtomic.class);

    private ContadorVisitas contadorVisitas;
    private static int visitasEsperadas;

    private static final Random random = new Random();

    public HilosVirtualesAtomic(ContadorVisitas contadorVisitas) {
        this.contadorVisitas = contadorVisitas;
    }

    public void ejecutarPrueba() {
        visitasEsperadas = Constantes.NUM_ESPERADO;

        long tiempoInicio = System.currentTimeMillis();

        List<Thread> hilos = new ArrayList<>();

        for (int i = 0; i < visitasEsperadas; i++) {
            int pausa = random.nextInt(50, 151);
            Thread hiloVirtual = Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(pausa);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                contadorVisitas.incrementarVisita(true);
            });
            hilos.add(hiloVirtual);
        }

        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error finalizando un hilo: {}", e.getMessage());
                break;
            }
        }

        long tiempoFinalizacion = System.currentTimeMillis();
        long tiempoTotal = tiempoFinalizacion - tiempoInicio;

        System.out.printf("Visitas esperadas: %d%n", visitasEsperadas);
        System.out.printf("Visitas contadas: %d %s%n", contadorVisitas.getVisitas(true), (contadorVisitas.getVisitas(true) == visitasEsperadas ? "✅ CORRECTO" : "❌ INCORRECTO"));
        System.out.printf("Tiempo total: %d ms%n", tiempoTotal);

        contadorVisitas.setVisitas(true, 0);
    }
}
