package com.arhiser.scheduler.implementation;

import android.os.Handler;

import com.arhiser.scheduler.scheduler.Task.Task;
import com.arhiser.scheduler.scheduler.Task.TaskFunction;

public class AndroidTask<O> extends Task<O> {

    protected OnResult<O> onResult;

    protected OnError onError;

    protected Handler handler;

    protected Class<O> resultClass;

    protected int priority;

    public static <O> AndroidTask<O> create(TaskFunction<O> function, Class<O> resultClass) {
        return new AndroidTask<>(function, resultClass);
    }

    public static <O> AndroidTask<O> create(TaskFunction<O> function, Class<O> resultClass, OnResult<O> onResult, OnError onError) {
        return new AndroidTask<>(function, resultClass, onResult, onError);
    }

    public AndroidTask(TaskFunction<O> function, Class<O> resultClass) {
        super(function);
        this.resultClass = resultClass;
    }

    public AndroidTask(TaskFunction<O> function, Class<O> resultClass, OnResult<O> onResult, OnError onError) {
        this(function, resultClass);
        this.onResult = onResult;
        this.onError = onError;
        handler = new Handler();
    }

    @Override
    public Class<O> getResultClass() {
        return resultClass;
    }

    @Override
    public void dispatchSuccess(O result) {
        super.dispatchSuccess(result);
        if (onResult != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onResult.onResult(result);
                }
            });
        }
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

    public AndroidTask<O> addDependency(AndroidTask task) {
        task.setParent(this);
        dependencies.add(task);
        return this;
    }

    public int getPriority() {
        if (parent != null) {
            return ((AndroidTask)parent).getPriority();
        } else {
            return priority;
        }
    }

    public AndroidTask<O> setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
