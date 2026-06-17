package org.s3m.sandbox;

public class MyThread  extends Thread {
    private String order;

    public MyThread(String name, String order) {
        super(name);
        this.order = order;
    }
    @Override
    public void run() {
        System.out.println("Hello from " + getName() + " " + order);
    }
}
