package com.arhiser.scheduler.scheduler.Task;

public interface TaskDependencyResult {
    <O> O getResultOfClass(Class<O> clazz);
}
