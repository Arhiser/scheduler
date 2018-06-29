package com.arhiser.scheduler.scheduler;

import com.arhiser.scheduler.scheduler.Task.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Queue<T extends Task> {

    private ArrayList<Condition<T>> conditions;

    private ArrayList<T> tasks = new ArrayList<>();
    private ArrayList<T> tasksInExecution = new ArrayList<>();
    private ArrayList<T> tasksCanBeExecuted = new ArrayList<>();

    Queue(Collection<Condition<T>> conditions) {
        this.conditions = new ArrayList<>(conditions);
    }

    public synchronized void put(T taskToPut) {
        addTaskToQueue(taskToPut);
        notifyUpdate();
    }

    synchronized void notifyUpdate() {
        reviewTaskCanBeExecuted();
        if (!tasksCanBeExecuted.isEmpty()) {
            notifyAll();
        }
    }

    public synchronized void onFinishExecution(T task) {
        if (!taskIsOnSomeoneDependencies(task)) {
            removeCompletedTaskWithDependencies(task);
        }
        reviewTaskCanBeExecuted();
        if (!tasksCanBeExecuted.isEmpty()) {
            notifyAll();
        }
    }

    private boolean taskIsOnSomeoneDependencies(T taskToCheck) {
        for(T task: tasks) {
            if (task.getDependencies().contains(taskToCheck)) {
                return true;
            }
        }
        return false;
    }

    private void removeCompletedTaskWithDependencies(T taskToRemove) {
        tasks.remove(taskToRemove);
        for(T task: (ArrayList<T>)taskToRemove.getDependencies()) {
            removeCompletedTaskWithDependencies(task);
        }
    }

    public synchronized T take() throws InterruptedException {
        if (tasksCanBeExecuted.isEmpty()) {
            wait();
        }
        if (tasksCanBeExecuted.isEmpty()) {
            throw new InterruptedException();
        }
        T task = tasksCanBeExecuted.get(0);
        tasksCanBeExecuted.remove(task);
        tasksInExecution.add(task);
        return task;
    }

    private void addTaskToQueue(T task) {
        if (!tasks.contains(task)) {
            tasks.add(task);
        }
        for(T t: (ArrayList<T>)task.getDependencies()) {
            addTaskToQueue(t);
        }
    }

    private void reviewTaskCanBeExecuted() {
        Iterator<T> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            T task = iterator.next();
            if (task.isCancelled() || task.isFailed()) {
                iterator.remove();
            }
        }
        ArrayList<T> tasks = new ArrayList<>(this.tasks);
        for(Condition<T> condition: conditions) {
            tasks = condition.getNextTasks(tasks, tasksInExecution);
        }
        tasksCanBeExecuted.clear();
        for(T task: tasks) {
            if (task.isDependenciesSatisfied()
                    && !task.isResolved()
                    && !task.isCancelled()
                    && !task.isFailed()
                    && !task.isExternal()
                    && !tasksInExecution.contains(task)) {
                tasksCanBeExecuted.add(task);
            }
        }
    }

}
