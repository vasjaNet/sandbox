package org.s3m.sandbox;

public class Main {
    static void main() {

        System.out.println(Thread.currentThread().getName() + " " + "Hello World");

        Runnable r = () -> System.out.println(Thread.currentThread().getName() + " " + "Hello World");

        for(int i = 0; i < 10; i++) {
            Thread t = new MyThread("MyThreadZZZZ", String.valueOf(i));
            Thread t2 = new Thread(r);
            MyThread mt = new MyThread("MyThreadXXX", String.valueOf(i));
            t.start();
            mt.run();
            t2.run();
        }


        System.out.println(Thread.currentThread().getName() + " " + "By World");

    }
}
