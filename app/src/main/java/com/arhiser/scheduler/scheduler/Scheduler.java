package com.arhiser.scheduler.scheduler;

import com.arhiser.scheduler.scheduler.Task.Cancelable;
import com.arhiser.scheduler.scheduler.Task.Task;

import java.util.Collection;

public class Scheduler<T extends Task> {

    Thread[] threads;
    Queue<T> queue;

    boolean isFinished = false;

    public Scheduler(int threadPoolSize, Collection<Condition<T>> conditions) {
        threads = new Thread[threadPoolSize];
        queue = new Queue<>(conditions);
        initThreadPool(threadPoolSize);
    }

    private void initThreadPool(int threadPoolSize) {
        for (int i = 0; i < threadPoolSize; i++) {
            threads[i] = new Thread(threadRunnable);
            threads[i].start();
        }
    }

    public Cancelable post(T task) {
        queue.put(task);
        return task;
    }

    public void shutdown() {
        isFinished = true;
        for(Thread thread: threads) {
            thread.interrupt();
        }
    }

    private Runnable threadRunnable = new Runnable() {
        @Override
        public void run() {
            while (!isFinished) {
                try {
                    T task = queue.take();
                    try {
                        task.execute();
                        task.dispatchSuccess();
                    } catch (Throwable error) {
                        task.dispatchFailed(error);
                    }
                    queue.onFinishExecution(task);
                } catch (InterruptedException e) {

                }
            }
        }
    };

}
