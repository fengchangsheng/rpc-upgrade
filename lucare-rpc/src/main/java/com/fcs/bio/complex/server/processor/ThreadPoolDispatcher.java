package com.fcs.bio.complex.server.processor;

import com.fcs.bio.complex.threadpool.TaskQueue;
import com.fcs.bio.complex.threadpool.TaskThreadFactory;
import com.fcs.bio.complex.threadpool.TaskThreadPoolExecutor;

import java.util.concurrent.*;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class ThreadPoolDispatcher {

    private static final int minThreadPoolSize = 20;
    private static final int maxThreadPoolSize = 100;
    private static final int threadPoolQueueSize = 2000;
    private final static ConcurrentHashMap<String, FutureTask<Executor>> executorPools = new ConcurrentHashMap<String, FutureTask<Executor>>();


    private static Executor getExecutor(final String key) {
        try {
            FutureTask<Executor> executorTask = executorPools.get(key);
            if (null != executorTask) {
                return executorTask.get();
            } else {
                Callable<Executor> creator = new Callable<Executor>() {
                    @Override
                    public Executor call() throws Exception {
                        TaskQueue taskQueue = new TaskQueue(threadPoolQueueSize);
                        TaskThreadPoolExecutor pool = new TaskThreadPoolExecutor(minThreadPoolSize, maxThreadPoolSize, 120, TimeUnit.SECONDS, taskQueue, new TaskThreadFactory("rpc-threadpool-exec-"));
                        taskQueue.setParent(pool);
                        return pool;
                    }
                };

                FutureTask<Executor> newTask = new FutureTask<Executor>(creator);
                executorTask = executorPools.putIfAbsent(key, newTask);
                if (null == executorTask) {
                    executorTask = newTask;
                    executorTask.run();
                }
                return executorTask.get();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
