package problema5;

import java.util.concurrent.*;

public class Descarga implements Runnable {
    private final TipoArchivo archivo;
    private final int id;

    public Descarga(int id) {
        this.id = id;
        this.archivo = TipoArchivo.aleatorio();
    }

    public int getId() { return id; }
    public TipoArchivo getArchivo() { return archivo; }

    @Override
    public void run() {
        long duracionMs = archivo.getSizeMB() * 100L;
        int total = archivo.getSizeMB();
        int[] progreso = {0};

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            int porcentaje = (int) ((progreso[0] * 100.0) / total);
            System.out.printf("📦 Descarga #%d [%s] progreso: %3d%%%n", id, archivo, porcentaje);
            if (porcentaje >= 100) {
                System.out.println("✅ Descarga #" + id + " completada: " + archivo);
                scheduler.shutdown();
            }
        }, 0, duracionMs / 10, TimeUnit.MILLISECONDS);

        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(duracionMs / 10);
                progreso[0] += total / 10;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        progreso[0] = total;
    }
}
