package problema4;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParkingInteligente {
    private final BlockingQueue<Coche> colaParking = new ArrayBlockingQueue<>(Constantes.MAX_COLA);
    private final BlockingQueue<Plaza> parkingPlazas = new ArrayBlockingQueue<>(Constantes.NUM_PLAZAS_NORMALES + Constantes.NUM_PLAZAS_VIP);
    private final List<Plaza> plazas = new ArrayList<>();
    private final Random random = new Random();
    private double ingresosTotales = 0.0;
    private final List<String> eventosRecientes = new ArrayList<>();

    private int cochesProcesados = 0;
    private int cochesAtendidos = 0;
    private int cochesRechazados = 0;
    private double sumaTiemposEstancia = 0.0;
    private int ocupacionMaxima = 0;


    public ParkingInteligente() {
        for (int i = 0; i < Constantes.NUM_PLAZAS_NORMALES + Constantes.NUM_PLAZAS_VIP; i++) {
            boolean esVip = i < Constantes.NUM_PLAZAS_VIP;
            Plaza plaza = new Plaza(i + 1, esVip);
            plazas.add(plaza);
            parkingPlazas.add(plaza);
        }
    }

    public void startParking() {
        // Hilo que asigna coches a plazas cuando ambos están disponibles
        Thread.ofVirtual().start(() -> {
            while (true) {
                try {
                    Coche coche = colaParking.take(); // espera coche
                    Plaza plaza = parkingPlazas.take(); // espera plaza libre
                    Thread.ofVirtual().start(() -> procesarEntrada(coche));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Hilo generador de coches
        Thread generadorCoches = Thread.ofVirtual().start(() -> {
            try {
                for (int i = 1; i <= Constantes.NUM_COCHES; i++) {
                    Coche coche = new Coche(i);
                    cochesProcesados++;
                    boolean enCola = colaParking.offer(coche);
                    if (enCola) {
                        registrarEvento(String.format("[%s] 🚗 Coche-%03d (%s) esperando en cola", tiempoActual(), coche.getId(), coche.getTipoVehiculo()));
                    } else {
                        cochesRechazados++;
                        registrarEvento(String.format("[%s] 🚗 Coche-%03d (%s) se va (cola llena)", tiempoActual(), coche.getId(), coche.getTipoVehiculo()));
                    }
                    Thread.sleep(500 + random.nextInt(1500)); // llegada aleatoria
                }

                Thread.sleep(60000); // Espera al final de la simulación
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Monitor visual
        Thread monitor = Thread.ofVirtual().start(this::mostrarEstado);

        try {
            generadorCoches.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        mostrarResumen();
    }


    private void procesarEntrada(Coche coche) {
        try {
            // 🚨 IMPORTANTE: quitar el coche de la cola
            colaParking.remove(coche);

            Plaza plaza = asignarPlaza(coche);
            if (plaza != null) {
                registrarEvento(String.format("[%s] 🚗 Coche-%03d (%s) entra - Plaza %s-%02d",
                        tiempoActual(), coche.getId(), coche.getTipoVehiculo(),
                        plaza.isVip() ? "V" : "N", plaza.getId()));
                cochesAtendidos++;
                long inicio = System.currentTimeMillis();

                Thread.sleep((10 + random.nextInt(21)) * 1000);
                double pago = liberarPlaza(plaza, coche);

                long fin = System.currentTimeMillis();
                double tiempoEstanciaSeg = (fin - inicio) / 1000.0;
                sumaTiemposEstancia += tiempoEstanciaSeg;

                ingresosTotales += pago;
                registrarEvento(String.format("[%s] ⭐ Coche-%03d (%s) sale - Plaza %s-%02d - Pagó: %.2f€",
                        tiempoActual(), coche.getId(), coche.getTipoVehiculo(),
                        plaza.isVip() ? "V" : "N", plaza.getId(), pago));
            } else {
                registrarEvento(String.format("[%s] 🚗 Coche-%03d (%s) no encuentra plaza y se va",
                        tiempoActual(), coche.getId(), coche.getTipoVehiculo()));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private synchronized Plaza asignarPlaza(Coche coche) {
        for (Plaza plaza : plazas) {
            if (plaza.getCoche() == null && (coche.getTipoVehiculo() == TipoVehiculo.VIP || !plaza.isVip())) {
                plaza.setCoche(coche);
                return plaza;
            }
        }
        return null;
    }

    private synchronized double liberarPlaza(Plaza plaza, Coche coche) {
        try {
            plaza.setCoche(null);
            parkingPlazas.put(plaza);
            return coche.getTipoVehiculo().getTarifaPorMinuto() * random.nextInt(21) + 10; // 10-30 minutos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0.0;
        }
    }

    private synchronized void registrarEvento(String evento) {
        if (eventosRecientes.size() >= 10) {
            eventosRecientes.remove(0);
        }
        eventosRecientes.add(evento);
    }

    private void mostrarEstado() {
        while (cochesAtendidos + cochesRechazados < Constantes.NUM_COCHES) {
            try {
                Thread.sleep(5000);
                imprimirEstado();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        // última impresión al final
        imprimirEstado();
    }

    private void imprimirEstado() {
        int ocupacionActual = contarPlazas(false) + contarPlazas(true);
        if (ocupacionActual > ocupacionMaxima) {
            ocupacionMaxima = ocupacionActual;
        }
        System.out.println("=== PARKING INTELIGENTE ===");
        System.out.printf("🅿️  Estado actual: [%s]\n", tiempoActual());
        System.out.println("┌─────────────────────────────────┐");
        System.out.printf("│ PLAZAS NORMALES: %-10s %2d/%2d │\n", barraProgreso(contarPlazas(false), Constantes.NUM_PLAZAS_NORMALES), contarPlazas(false), Constantes.NUM_PLAZAS_NORMALES);
        System.out.printf("│ PLAZAS VIP:     %-10s %2d/%2d │\n", barraProgreso(contarPlazas(true), Constantes.NUM_PLAZAS_VIP), contarPlazas(true), Constantes.NUM_PLAZAS_VIP);
        System.out.printf("│ COLA DE ESPERA: %-10s %2d/%2d │\n", barraProgreso(colaParking.size(), Constantes.MAX_COLA), colaParking.size(), Constantes.MAX_COLA);
        System.out.printf("│ INGRESOS HOY:              %.2f€ │\n", ingresosTotales);
        System.out.println("└─────────────────────────────────┘");
        System.out.println("\nÚltimos eventos:");
        eventosRecientes.forEach(System.out::println);
        System.out.println();
    }


    private void mostrarResumen() {
        double porcentajeAtendidos = (cochesAtendidos * 100.0) / cochesProcesados;
        double tiempoMedio = cochesAtendidos > 0 ? sumaTiemposEstancia / cochesAtendidos : 0;

        System.out.println("\n--- RESUMEN DEL DÍA ---");
        System.out.printf("Vehículos procesados: %d\n", cochesProcesados);
        System.out.printf("Vehículos atendidos: %d (%.1f%%)\n", cochesAtendidos, porcentajeAtendidos);
        System.out.printf("Vehículos rechazados: %d (parking+cola llenos)\n", cochesRechazados);
        System.out.printf("Tiempo promedio de estancia: %.1fs\n", tiempoMedio);
        System.out.printf("Ingresos totales: %.2f€\n", ingresosTotales);
        System.out.printf("Ocupación máxima: %d/%d plazas (%.0f%%)\n",
                ocupacionMaxima,
                Constantes.NUM_PLAZAS_NORMALES + Constantes.NUM_PLAZAS_VIP,
                (ocupacionMaxima * 100.0) / (Constantes.NUM_PLAZAS_NORMALES + Constantes.NUM_PLAZAS_VIP));
    }

    private int contarPlazas(boolean vip) {
        return (int) plazas.stream().filter(p -> p.getCoche() != null && p.isVip() == vip).count();
    }

    private String barraProgreso(int valor, int maximo) {
        int longitud = 10;
        int llenos = (int) ((double) valor / maximo * longitud);
        return "█".repeat(llenos) + "░".repeat(longitud - llenos);
    }

    private String tiempoActual() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
