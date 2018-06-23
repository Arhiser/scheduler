package com.arhiser.scheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.arhiser.scheduler.implementation.AndroidTask;
import com.arhiser.scheduler.scheduler.Condition;
import com.arhiser.scheduler.scheduler.Scheduler;
import com.arhiser.scheduler.scheduler.Task.NoResult;
import com.arhiser.scheduler.scheduler.Task.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Scheduler<AndroidTask> scheduler = new Scheduler<>(4, new ArrayList<Condition<AndroidTask>>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduler.post(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "main");
                    return NoResult.value;
                },
                NoResult.class,
                result -> Log.v("aaaaaaaaaaaaaaa", "success"),
                error -> Log.v("aaaaaaaaaaaaaaa", "error")
        )
            .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "dep1");
                    throw new RuntimeException("ggg");
                    //return NoResult.value;
                }, NoResult.class))
            .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "dep2");
                    return NoResult.value;
                }, NoResult.class)));

        scheduler.post(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "main_2");
                    return NoResult.value;
                },
                NoResult.class,
                result -> Log.v("aaaaaaaaaaaaaaa", "success"),
                error -> Log.v("aaaaaaaaaaaaaaa", "error")
        )
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "dep1_2");
                    return NoResult.value;
                }, NoResult.class))
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "dep2_2");
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
                result -> Log.v("aaaaaaaaaaaaaaa", "success: " + result),
                error -> Log.v("aaaaaaaaaaaaaaa", "error")
        )
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "calc1");
                    return 1;
                }, Integer.class))
                .addDependency(AndroidTask.create(taskDependencyResult -> {
                    Log.v("aaaaaaaaaaaaaaa", "calc2");
                    return 3;
                }, Integer.class)));
    }
}
