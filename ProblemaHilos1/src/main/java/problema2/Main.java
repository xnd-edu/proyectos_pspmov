package problema2;

public class Main {
    public static void main(String[] args) {
        System.out.println("== BANCO VIRTUAL ==");
        System.out.println("Saldo inicial: " + Constantes.SALDO_INICIAL + "€");
        System.out.println(Constantes.NUM_CLIENTES + " clientes realizando " + Constantes.NUM_OPERACIONES * Constantes.NUM_CLIENTES + " operaciones totales...");

        System.out.println();
        System.out.println("--- CON REENTRANTLOCK ---");
        OperacionesLock operacionesLock = new OperacionesLock();
        operacionesLock.ejecutarOperaciones();

        System.out.println();
        System.out.println("--- CON SYNCHRONIZED ---");
        OperacionesSync operacionesSync = new OperacionesSync();
        operacionesSync.ejecutarOperaciones();

        System.out.println();
        System.out.println("--- CON VOLATILE ---");
        OperacionesVolatile operacionesVolatile = new OperacionesVolatile();
        operacionesVolatile.ejecutarOperaciones();
    }
}
