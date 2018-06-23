package com.arhiser.scheduler.scheduler.Task;

public interface TaskFunction<R> {
    R execute(TaskDependencyResult taskDependencyResult) throws Throwable;
}
