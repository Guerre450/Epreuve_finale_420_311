package mv.sdd.sim.thread;

import java.util.concurrent.*;

/**
 * Classe qui s'occupe des operations threads
 */

public class ThreadPool {
    private final ExecutorService executorService;
    private int nombrePool;

    public int getNombrePool() {
        return nombrePool;
    }

    public void setNombrePool(int nombrePool) {
        this.nombrePool = nombrePool;
    }



    public ThreadPool(int nb) {
        executorService = Executors.newFixedThreadPool(nb);
        setNombrePool(nb);
    }

    /**
     * Lance un thread à partir du Callable passé en paramètre
     *
     * @param task Tache Runnable à éxécuter
     */
    public Future<?> submitTask(Runnable task) {
        return executorService.submit(() -> {
            Thread thread = new Thread(task);
            thread.start();
        });
    }

    /**
     * Termine les threads de manière gracieuse si possible
     */
    public void shutdownAndAwaitTermination() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public boolean awaitTermination() throws InterruptedException {
        return executorService.awaitTermination(60, TimeUnit.SECONDS);
    }
}
