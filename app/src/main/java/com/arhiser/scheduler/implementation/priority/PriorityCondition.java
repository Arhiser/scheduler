package com.arhiser.scheduler.implementation.priority;

import com.arhiser.scheduler.implementation.AndroidTask;
import com.arhiser.scheduler.scheduler.Condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class PriorityCondition implements Condition<AndroidTask> {
    @Override
    public ArrayList<AndroidTask> getNextTasks(Collection<AndroidTask> tasksInQueue, Collection<AndroidTask> tasksInExecution) {
        ArrayList<AndroidTask> tasks = new ArrayList<>(tasksInQueue);
        tasks.removeAll(tasksInExecution);
        Collections.sort(tasks, new Comparator<AndroidTask>() {
            @Override
            public int compare(AndroidTask o1, AndroidTask o2) {
                return o2.getPriority() - o1.getPriority();
            }
        });

        return tasks;
    }
}
