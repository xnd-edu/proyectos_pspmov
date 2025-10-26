package problema1.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ContadorVisitas {
    private int contadorVisitas;
    private AtomicInteger contadorVisitasAtomic;

    public ContadorVisitas(int contador) {
        contadorVisitas = contador;
        contadorVisitasAtomic = new AtomicInteger(contador);
    }

    public int getVisitas(boolean atomic) {
        if (atomic)
            return contadorVisitasAtomic.get();
        else
            return contadorVisitas;
    }

    public void setVisitas(boolean atomic, int contadorVisitas) {
        if (atomic)
            contadorVisitasAtomic.set(contadorVisitas);
        else
            this.contadorVisitas = contadorVisitas;
    }

    public void incrementarVisita(boolean atomic) {
        if (atomic)
            contadorVisitasAtomic.incrementAndGet();
        else
            contadorVisitas++;
    }

    public synchronized void incrementarVisitaSynchronized() {
        contadorVisitas++;
    }
}
