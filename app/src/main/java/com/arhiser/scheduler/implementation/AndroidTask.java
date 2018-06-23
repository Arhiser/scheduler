package com.arhiser.scheduler.implementation;

import android.os.Handler;

import com.arhiser.scheduler.scheduler.Task.Task;
import com.arhiser.scheduler.scheduler.Task.TaskFunction;

public class AndroidTask<O> extends Task<O> {

    protected OnResult<O> onResult;

    protected OnError onError;

    protected Handler handler;

    protected Class<O> resultClass;

    public static <O> AndroidTask<O> create(TaskFunction<O> function, Class<O> resultClass) {
        return new AndroidTask<>(function, resultClass);
    }

    public static <O> AndroidTask<O> create(TaskFunction<O> function, Class<O> resultClass, OnResult<O> onResult, OnError onError) {
        return new AndroidTask<>(function, resultClass, onResult, onError);
    }

    private AndroidTask(TaskFunction<O> function, Class<O> resultClass) {
        super(function);
        this.resultClass = resultClass;
        handler = new Handler();
    }

    private AndroidTask(TaskFunction<O> function, Class<O> resultClass, OnResult<O> onResult, OnError onError) {
        this(function, resultClass);
        this.onResult = onResult;
        this.onError = onError;
    }

    @Override
    public Class<O> getResultClass() {
        return resultClass;
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

    public AndroidTask<O> addDependency(AndroidTask task) {
        task.setParent(this);
        dependencies.add(task);
        return this;
    }
}
