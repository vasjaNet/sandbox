package org.s3m.sandbox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the different ways to create and run threads in Java.
 *
 * <p>The five classical approaches shown here:
 * <ol>
 *   <li>Subclassing {@link Thread} directly (anonymous inner class).</li>
 *   <li>Subclassing {@link Thread} in a named class ({@link MyThread}).</li>
 *   <li>Implementing {@link Runnable} as a lambda.</li>
 *   <li>Implementing {@link Runnable} in a named class ({@link MyRunnable}).</li>
 *   <li>Submitting tasks to an {@link ExecutorService} (the modern preferred approach).</li>
 * </ol>
 *
 * <p>Important distinction: calling {@code start()} launches a new OS thread,
 * while calling {@code run()} directly executes the task on the <em>current</em>
 * thread — defeating the purpose of using {@code Thread}.
 */
public class Main {

    private static final int ITERATIONS = 10;
    private static final int THREAD_POOL_SIZE = 4;

    public static void main(String[] args) throws InterruptedException {
        log("Program started");

        // --- Approach 1: anonymous Thread subclass ---------------------------
        demonstrateAnonymousThreadSubclass();

        // --- Approach 2: named Thread subclass -------------------------------
        demonstrateNamedThreadSubclass();

        // --- Approach 3: Runnable as a lambda --------------------------------
        demonstrateRunnableLambda();

        // --- Approach 4: Runnable in a named class ---------------------------
        demonstrateRunnableClass();

        // --- Approach 5: ExecutorService (modern, recommended) ---------------
        demonstrateExecutorService();

        log("Program finished");
    }

    /**
     * Approach 1: declare a thread by subclassing {@link Thread} with an
     * anonymous inner class. Convenient for one-off tasks, but less reusable
     * than a {@link Runnable}.
     */
    private static void demonstrateAnonymousThreadSubclass() throws InterruptedException {
        logSection("Approach 1 — anonymous Thread subclass");

        Thread anonymous = new Thread() {
            @Override
            public void run() {
                log("Hello from anonymous Thread subclass");
            }
        };
        anonymous.start();
        anonymous.join(); // wait so output is grouped nicely
    }

    /**
     * Approach 2: subclass {@link Thread} in a reusable named class
     * ({@link MyThread}). The thread carries both a name and an {@code order}.
     */
    private static void demonstrateNamedThreadSubclass() throws InterruptedException {
        logSection("Approach 2 — named Thread subclass (MyThread)");

        MyThread thread = new MyThread("MyThreadZZZZ", "0");
        thread.start();
        thread.join();
    }

    /**
     * Approach 3: pass a {@link Runnable} lambda to the {@link Thread}
     * constructor. Concise and avoids creating a new class.
     */
    private static void demonstrateRunnableLambda() {
        logSection("Approach 3 — Runnable lambda");

        Runnable task = () -> log("Hello from Runnable lambda");
        Thread thread = new Thread(task);
        thread.start();
        // Note: no join() — letting the JVM print asynchronously for variety.
    }

    /**
     * Approach 4: implement {@link Runnable} in a reusable named class
     * ({@link MyRunnable}). Preferred over subclassing {@link Thread} because
     * the task is decoupled from the thread mechanism and can be reused with
     * executors, thread pools, etc.
     */
    private static void demonstrateRunnableClass() {
        logSection("Approach 4 — Runnable implementation (MyRunnable)");

        Thread thread = new Thread(new MyRunnable("0"));
        thread.start();
    }

    /**
     * Approach 5: {@link ExecutorService}. In production code, prefer an
     * executor over raw {@link Thread} instances — it manages a pool of
     * reusable threads, supports task submission, and handles shutdown cleanly.
     */
    private static void demonstrateExecutorService() throws InterruptedException {
        logSection("Approach 5 — ExecutorService (thread pool)");

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            for (int i = 0; i < ITERATIONS; i++) {
                final String order = String.valueOf(i);
                pool.submit(() -> log("Hello from pool worker, task=" + order));
            }
        } finally {
            // Initiate an orderly shutdown: previously submitted tasks execute,
            // but no new tasks are accepted.
            pool.shutdown();
            // Block until all tasks have completed (or timeout elapses).
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        }
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    /** Prints a message prefixed with the current thread's name. */
    private static void log(String message) {
        System.out.println(Thread.currentThread().getName() + " | " + message);
    }

    /** Prints a section header to make demo output easier to read. */
    private static void logSection(String title) {
        System.out.println("----- " + title + " -----");
    }
}