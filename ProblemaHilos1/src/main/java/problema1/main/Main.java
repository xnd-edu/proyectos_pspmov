package problema1.main;

import problema1.conAtomic.HilosVirtualesAtomic;
import problema1.conSynchronized.HilosVirtualesSync;
import problema1.noSync.HilosVirtualesSinSync;
import problema1.util.Constantes;
import problema1.util.ContadorVisitas;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CONTADOR DE VISITAS WEB ===");
        System.out.println("Esperando " + Constantes.NUM_ESPERADO + " visitantes...");

        System.out.println();
        System.out.println("--- SIN SINCRONIZACIÓN ---");
        HilosVirtualesSinSync hilosVirtualesSinSync = new HilosVirtualesSinSync(new ContadorVisitas(0));
        hilosVirtualesSinSync.ejecutarPrueba();

        System.out.println();
        System.out.println("--- CON SYNCHRONIZED ---");
        HilosVirtualesSync hilosVirtualesSync = new HilosVirtualesSync(new ContadorVisitas(0));
        hilosVirtualesSync.ejecutarPrueba();

        System.out.println();
        System.out.println("--- CON ATOMICINTEGER ---");
        HilosVirtualesAtomic hilosVirtualesAtomic = new HilosVirtualesAtomic(new ContadorVisitas(0));
        hilosVirtualesAtomic.ejecutarPrueba();
    }
}
