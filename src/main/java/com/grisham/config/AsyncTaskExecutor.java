package com.grisham.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableAsync
public class AsyncTaskExecutor {

    @Value("${price.pool.core-size:3}")
    private int corePoolSize;

    @Value("${price.pool.max-size:6}")
    private int maxPoolSize;

    @Value("${price.pool.queue-capacity:100}")
    private int queueCapacity;

    @Value("${price.pool.thread-name-prefix:price-fetch-}")
    private String threadNamePrefix;

    @Value("${vendors.cache.max-size:1000}")
    private int cacheMaxSize;

    @Value("${vendors.cache.expire-after-write-minutes:10}")
    private int cacheExpireMinutes;

    @Bean("priceTaskExecutor")
    public Executor priceTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setThreadFactory(new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, threadNamePrefix + counter.getAndIncrement());
                thread.setDaemon(false);
                return thread;
            }
        });
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }
}
