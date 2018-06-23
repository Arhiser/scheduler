package com.arhiser.scheduler.scheduler.Task;

import java.util.ArrayList;

public abstract class Task<O> implements Cancelable {

    protected volatile O result;

    protected volatile Throwable error;

    protected ArrayList<Task> dependencies = new ArrayList<>();

    protected Task parent;

    protected TaskFunction<O> function;

    protected volatile boolean isCancelled;

    protected Task(TaskFunction<O> function) {
        this.function = function;
    }

    public ArrayList<Task> getDependencies() {
        return dependencies;
    }

    public boolean isResolved() {
        return result != null;
    }

    public boolean isFailed() {
        return error != null;
    }

    @Override
    public void cancel() {
        isCancelled = true;
        for(Task task: dependencies) {
            task.cancel();
        }
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean isDependenciesSatisfied() {
        if (dependencies.isEmpty()) {
            return true;
        }
        for (Task task: dependencies) {
            if (!task.isResolved()) {
                return false;
            }
        }
        return true;
    }

    public O getResult() {
        return result;
    }

    public void dispatchSuccess() {

    }

    public void dispatchFailed(Throwable error) {
        this.error = error;
        if (parent != null) {
            if (!parent.isFailed()) {
                parent.dispatchFailed(error);
            }
        }
        for(Task task: dependencies) {
            if (!task.isFailed()) {
                task.dispatchFailed(error);
            }
        }
    }

    public abstract Class<O> getResultClass();

    public void execute() throws Throwable {
        result = function.execute(dependencies);
    }
}
