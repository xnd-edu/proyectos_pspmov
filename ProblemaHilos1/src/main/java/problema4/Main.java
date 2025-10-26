package problema4;


public class Main {
    public static void main(String[] args) {
        ParkingInteligente parkingInteligente = new ParkingInteligente();
        parkingInteligente.startParking();

        // Evitar que el programa termine inmediatamente
        try {
            Thread.currentThread().join(); // Mantiene vivo el hilo principal
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
