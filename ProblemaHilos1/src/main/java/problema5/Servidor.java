package problema5;

import java.util.concurrent.*;

public class Servidor {
    private static final int MAX_CONEXIONES = Constantes.MAX_DESCARGAS;
    private final Semaphore limite = new Semaphore(MAX_CONEXIONES);
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private int descargasId = 0;
    private int descargasRealizadas = 0;

    public void manejarCliente(Cliente cliente) {
        CompletableFuture.runAsync(() -> {
            for (int intento = 1; intento <= 3; intento++) {
                try {
                    if (limite.tryAcquire(3, TimeUnit.SECONDS)) {
                        Descarga descarga = new Descarga(++descargasId);
                        cliente.setDescarga(descarga);
                        System.out.println("✅ Cliente #" + cliente.getId() + " inicia descarga #" + descarga.getId() +
                                " (" + descarga.getArchivo() + ")");
                        pool.submit(() -> {
                            try {
                                descarga.run();
                            } finally {
                                limite.release();
                            }
                        });
                        return; // descarga lanzada
                    } else {
                        System.out.println("⏳ Cliente #" + cliente.getId() + " reintentando (" + intento + "/3)");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("❌ Cliente #" + cliente.getId() + " no pudo iniciar la descarga.");
        });
    }

    public void shutdown() {
        pool.shutdown();
    }
}
