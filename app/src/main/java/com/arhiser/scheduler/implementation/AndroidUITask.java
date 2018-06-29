package com.arhiser.scheduler.implementation;

import com.arhiser.scheduler.scheduler.Scheduler;
import com.arhiser.scheduler.scheduler.Task.TaskFunction;

public class AndroidUITask<O> extends AndroidTask<O> {

    Scheduler scheduler;

    public AndroidUITask(TaskFunction<O> function, Scheduler scheduler, Class<O> resultClass) {
        super(function, resultClass);
        this.scheduler = scheduler;
    }

    public AndroidUITask(TaskFunction<O> function, Scheduler scheduler, Class<O> resultClass, OnResult<O> onResult, OnError onError) {
        super(function, resultClass, onResult, onError);
        this.scheduler = scheduler;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    public void execute() {
        super.execute();
        scheduler.notifyQueue();
    }
}
