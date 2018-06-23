package com.arhiser.scheduler.implementation;

import android.os.Handler;

import com.arhiser.scheduler.scheduler.Task.Task;
import com.arhiser.scheduler.scheduler.Task.TaskFunction;

public abstract class AndroidTask<O> extends Task<O> {

    protected OnResult<O> onResult;

    protected OnError onError;

    protected Handler handler;

    protected AndroidTask(TaskFunction<O> function) {
        super(function);
        handler = new Handler();
    }

    @Override
    public void dispatchSuccess() {
        if (onResult != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onResult.onResult(result);
                }
            });
        }
        super.dispatchSuccess();
    }

    @Override
    public void dispatchFailed(final Throwable error) {
        if (onError != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onError.onError(error);
                }
            });
        }
        super.dispatchFailed(error);
    }

    protected void setParent(AndroidTask parent) {
        this.parent = parent;
    }

    public AndroidTask(TaskFunction<O> function, OnResult<O> onResult, OnError onError) {
        super(function);
        this.onResult = onResult;
        this.onError = onError;
    }

    public void addDependency(AndroidTask task) {
        task.setParent(this);
        dependencies.add(task);
    }
}
