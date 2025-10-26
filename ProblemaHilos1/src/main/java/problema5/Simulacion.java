package problema5;

import java.util.ArrayList;
import java.util.List;

public class Simulacion {
    private final Servidor servidor = new Servidor();
    private final List<Cliente> clientes = new ArrayList<>();

    public void iniciar() {
        System.out.println("=== GESTOR DE DESCARGAS ===");

        // Crear los clientes (sin hilos virtuales aún)
        for (int i = 0; i < Constantes.NUM_CLIENTES; i++) {
            Cliente cliente = new Cliente(i, Math.random() < 0.2);
            clientes.add(cliente);
        }

        long inicio = System.currentTimeMillis();
        // Iniciar hilos virtuales para simular conexiones de cliente
        for (Cliente cliente : clientes) {
            Thread.ofVirtual().start(() -> {
                servidor.manejarCliente(cliente);
            });
        }

        // Esperar un tiempo para observar la simulación
        try {
            Thread.sleep(30000); // 30 segundos de simulación
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        servidor.shutdown();
    }
}
