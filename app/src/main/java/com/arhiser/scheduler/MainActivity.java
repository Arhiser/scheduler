package com.arhiser.scheduler;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.arhiser.scheduler.implementation.AndroidTask;
import com.arhiser.scheduler.implementation.AndroidUITask;
import com.arhiser.scheduler.implementation.priority.PriorityCondition;
import com.arhiser.scheduler.scheduler.Condition;
import com.arhiser.scheduler.scheduler.Scheduler;
import com.arhiser.scheduler.scheduler.Task.NoResult;
import com.arhiser.scheduler.scheduler.Task.Task;
import com.arhiser.scheduler.scheduler.Task.TaskFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tasks";

    Scheduler<AndroidTask> scheduler = new Scheduler<>(8, Arrays.asList(new PriorityCondition()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidUITask<NoResult> testUITask = new AndroidUITask<NoResult> (dependencies -> {
            Log.v(TAG, "UI task");
            return NoResult.value;
        }, scheduler, NoResult.class);

        scheduler.post(AndroidTask.create(taskDependencyResult -> {
                    Log.v(TAG, "main");
                    Thread.sleep(1000);
                    return NoResult.value;
                },
                NoResult.class,
                result -> Log.v(TAG, "success"),
                error -> Log.v(TAG, "error")
        )
        .addDependency(AndroidTask.create(taskDependencyResult -> {
                Log.v(TAG, "dep1");
                Thread.sleep(1000);
                //throw new RuntimeException("ggg");
                return NoResult.value;
            }, NoResult.class))
        .addDependency(AndroidTask.create(taskDependencyResult -> {
                Log.v(TAG, "dep2");
                Thread.sleep(1000);
                return NoResult.value;
            }, NoResult.class).addDependency(AndroidTask.create(new TaskFunction<NoResult>() {
            @Override
            public NoResult execute(List<Task> dependencies) throws Throwable {
                Log.v(TAG, "dep of dep2");
                Thread.sleep(1000);
                return NoResult.value;
            }
        }, NoResult.class).addDependency(testUITask)))
        );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testUITask.execute();
            }
        }, 10000);
/*
        scheduler.post(AndroidTask.create(taskDependencyResult -> {
                    Log.v(TAG, "main_2");
                    Thread.sleep(1000);
                    return NoResult.value;
                },
                NoResult.class,
                result -> Log.v(TAG, "success"),
                error -> Log.v(TAG, "error")
        )
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v(TAG, "dep1_2");
                    Thread.sleep(1000);
                    return NoResult.value;
                }, NoResult.class))
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v(TAG, "dep2_2");
                    Thread.sleep(1000);
                    return NoResult.value;
                }, NoResult.class)));

        scheduler.post(AndroidTask.create(taskDependencyResult -> {
                    int sum = 0;
                    for(Task task: taskDependencyResult) {
                        sum += (Integer)task.getResult();
                    }
                    return sum;
                },
                Integer.class,
                result -> Log.v(TAG, "success: " + result),
                error -> Log.v(TAG, "error")
        )
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v(TAG, "calc1");
                    Thread.sleep(1000);
                    return 1;
                }, Integer.class))
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v(TAG, "calc2");
                    Thread.sleep(1000);
                    return 3;
                }, Integer.class))
                .setPriority(10)
        );
        */
    }
}
