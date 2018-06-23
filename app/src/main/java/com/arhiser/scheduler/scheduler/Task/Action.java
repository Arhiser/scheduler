package com.arhiser.scheduler.scheduler.Task;

public interface Action<R> {
    R execute(TaskDependencyResult args);
}
