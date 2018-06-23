package com.arhiser.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arhiser.scheduler.implementation.AndroidTask;
import com.arhiser.scheduler.scheduler.Condition;
import com.arhiser.scheduler.scheduler.Scheduler;
import com.arhiser.scheduler.scheduler.Task.NoResult;
import com.arhiser.scheduler.scheduler.Task.TaskDependencyResult;
import com.arhiser.scheduler.scheduler.Task.TaskFunction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Scheduler<AndroidTask> scheduler = new Scheduler<>(4, new ArrayList<Condition<AndroidTask>>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidTask<NoResult> mainTask = new AndroidTask<NoResult>(new TaskFunction<NoResult>() {
            @Override
            public NoResult execute(TaskDependencyResult taskDependencyResult) throws Throwable {
                Log.v("aaaaaaaaaaaaaaa", "main");
                return NoResult.value;
            }
        }) {
            @Override
            public Class<NoResult> getResultClass() {
                return NoResult.class;
            }
        };
        mainTask.addDependency(new AndroidTask<NoResult>(new TaskFunction<NoResult>() {
            @Override
            public NoResult execute(TaskDependencyResult taskDependencyResult) throws Throwable {
                Log.v("aaaaaaaaaaaaaaa", "sec1");
                return NoResult.value;
            }
        }) {
            @Override
            public Class<NoResult> getResultClass() {
                return NoResult.class;
            }
        });
        mainTask.addDependency(new AndroidTask<NoResult>(new TaskFunction<NoResult>() {
            @Override
            public NoResult execute(TaskDependencyResult taskDependencyResult) throws Throwable {
                Log.v("aaaaaaaaaaaaaaa", "sec2");
                return NoResult.value;
            }
        }) {
            @Override
            public Class<NoResult> getResultClass() {
                return NoResult.class;
            }
        });

        scheduler.post(mainTask);
    }
}
