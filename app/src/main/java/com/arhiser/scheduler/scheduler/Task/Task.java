package com.arhiser.scheduler.scheduler.Task;

import java.util.ArrayList;

public abstract class Task<O> implements TaskDependencyResult, Cancelable {

    protected O result;

    protected Throwable error;

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
            parent.dispatchFailed(error);
        }
        for(Task task: dependencies) {
            task.dispatchFailed(error);
        }
    }

    public abstract Class<O> getResultClass();

    public void execute() throws Throwable {
        result = function.execute(this);
    }

    @Override
    public <R> R getResultOfClass(Class<R> clazz) {
        for(Task task: dependencies) {
            if (task.getResultClass() == clazz) {
               return (R)task.getResult();
            }
        }
        throw new RuntimeException("Can't find result of class: " + clazz.getCanonicalName());
    }
}
