package network;

import data.Data;

public abstract class Trainer implements Runnable {

    private Thread runner;
    public boolean running;

    protected Trainer(){
        this.running = false;
    }

    protected abstract void train(Data data, int iterations);

    protected abstract void train(Data data, float stopError);

    public void start(){
        runner = new Thread(this);
        runner.setPriority(Thread.MAX_PRIORITY);
        running = true;
        runner.start();
    }

    public void join() throws InterruptedException{
        runner.join();
    }
}
