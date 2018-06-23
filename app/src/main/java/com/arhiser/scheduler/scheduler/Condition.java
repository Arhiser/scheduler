package com.arhiser.scheduler.scheduler;

import com.arhiser.scheduler.scheduler.Task.Task;

import java.util.ArrayList;
import java.util.Collection;

public interface Condition<T extends Task> {
    ArrayList<T> getNextTasks(Collection<T> tasksInQueue, Collection<T> tasksInExecution);
}
