package com.arhiser.scheduler.scheduler.Task;

import java.util.List;

public interface TaskFunction<R> {
    R execute(List<Task> dependencies) throws Throwable;
}
