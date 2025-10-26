package problema2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class OperacionesLock {
    private static final Logger logger = LoggerFactory.getLogger(OperacionesLock.class);
    private final Random random = new Random();
    private ReentrantLock lock = new ReentrantLock();

    public void ejecutarOperaciones() {
        CuentaBancaria cuentaBancaria = new CuentaBancaria();

        int numClientes = Constantes.NUM_CLIENTES;
        int operacionesPorCliente = Constantes.NUM_OPERACIONES;
        int operacionesTotales = operacionesPorCliente * numClientes;

        List<Thread> hilos = new ArrayList<>();
        long inicio = System.currentTimeMillis();

        for (int i = 0; i < numClientes; i++) {
            Thread hilo = Thread.ofVirtual().start(() -> {
                for (int j = 0; j < operacionesPorCliente; j++) {
                    try {
                        Thread.sleep(random.nextInt(100, 301));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    double cantidad;
                    boolean esRetirada = random.nextDouble() < 0.6;

                    if (esRetirada) {
                        cantidad = random.nextInt(1, 101);
                    } else {
                        cantidad = random.nextInt(1, 51);
                    }

                    lock.lock();
                    try {
                        if (esRetirada) {
                            cuentaBancaria.retirar(cantidad);
                        } else {
                            cuentaBancaria.ingresar(cantidad);
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            });
            hilos.add(hilo);
        }

        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error finalizando un hilo: {}", e.getMessage());
            }
        }

        long fin = System.currentTimeMillis();
        double tiempo = (fin - inicio) / 1000.0;

        System.out.printf("Saldo: %.2f€%n", cuentaBancaria.consultarSaldo());
        System.out.printf("Opercaiones exitosas: %d/%d%n", cuentaBancaria.obtenerHistorial().size(), operacionesTotales);
        System.out.printf("Operaciones fallidas: %d%n", (operacionesTotales - cuentaBancaria.obtenerHistorial().size()));
        System.out.printf("Tiempo: %.1fs%n", tiempo);
    }

}
