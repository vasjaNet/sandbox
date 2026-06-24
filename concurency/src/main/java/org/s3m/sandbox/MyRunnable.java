package org.s3m.sandbox;

public class MyRunnable implements Runnable{
    private final String order;

    public MyRunnable(String order) {
        this.order = order;
    }

    @Override
    public void run() {
        System.out.println("Hello from Runnable" + Thread.currentThread().getName() + " " + order);
    }
}
