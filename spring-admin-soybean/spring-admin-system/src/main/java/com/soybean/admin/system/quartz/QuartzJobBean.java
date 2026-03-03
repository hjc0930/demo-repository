package com.soybean.admin.system.quartz;

import com.soybean.admin.data.entity.SysJob;
import com.soybean.admin.data.mapper.SysJobMapper;
import com.soybean.admin.data.mapper.SysJobLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Quartz任务执行Bean
 */
@Slf4j
@Component
@RequiredArgsConstructor
@DisallowConcurrentExecution
public class QuartzJobBean implements Job, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        SysJob sysJob = (SysJob) dataMap.get("task");

        if (sysJob == null) {
            log.error("Task not found in job data map");
            return;
        }

        long startTime = System.currentTimeMillis();
        SysJobLogMapper jobLogMapper = applicationContext.getBean(SysJobLogMapper.class);
        SysJobMapper sysJobMapper = applicationContext.getBean(SysJobMapper.class);

        com.soybean.admin.data.entity.SysJobLog jobLog = new com.soybean.admin.data.entity.SysJobLog();
        jobLog.setJobId(sysJob.getJobId());
        jobLog.setJobName(sysJob.getJobName());
        jobLog.setJobGroup(sysJob.getJobGroup());
        jobLog.setInvokeTarget(sysJob.getInvokeTarget());
        jobLog.setJobMessage("任务开始执行");
        jobLog.setStartTime(LocalDateTime.now());
        jobLog.setStatus("0");

        try {
            // 执行任务
            doExecute(sysJob);

            long endTime = System.currentTimeMillis();
            jobLog.setEndTime(LocalDateTime.now());
            jobLog.setJobMessage("任务执行成功");
            jobLog.setStatus("1");
            jobLog.setExecuteTime(String.valueOf(endTime - startTime));

            log.info("Task executed successfully: {}, took {}ms", sysJob.getJobName(), endTime - startTime);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            jobLog.setEndTime(LocalDateTime.now());
            jobLog.setJobMessage("任务执行失败: " + e.getMessage());
            jobLog.setStatus("2");
            jobLog.setExecuteTime(String.valueOf(endTime - startTime));
            jobLog.setExceptionInfo(e.getMessage());

            log.error("Task execution failed: {}, error: {}", sysJob.getJobName(), e.getMessage(), e);

            // 失败重试
            if ("1".equals(sysJob.getMisfirePolicy())) {
                int retryCount = dataMap.getInt("retryCount");
                if (retryCount < 3) {
                    dataMap.put("retryCount", retryCount + 1);
                    log.info("Retrying task: {}, retry count: {}", sysJob.getJobName(), retryCount + 1);

                    // 简单的延迟重试
                    executorService.submit(() -> {
                        try {
                            Thread.sleep(5000);
                            doExecute(sysJob);
                            log.info("Task retry succeeded: {}", sysJob.getJobName());
                        } catch (Exception ex) {
                            log.error("Task retry failed: {}, error: {}", sysJob.getJobName(), ex.getMessage());
                        }
                    });
                }
            }
        } finally {
            jobLogMapper.insert(jobLog);
        }
    }

    /**
     * 执行任务
     */
    private void doExecute(SysJob sysJob) throws Exception {
        String invokeTarget = sysJob.getInvokeTarget();

        if (invokeTarget.startsWith("bean:")) {
            // Spring Bean调用方式: bean:beanName.methodName
            String beanCall = invokeTarget.substring(5);
            String[] parts = beanCall.split("\\.");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid bean call format: " + invokeTarget);
            }

            String beanName = parts[0];
            String methodName = parts[1];

            Object bean = applicationContext.getBean(beanName);
            java.lang.reflect.Method method = bean.getClass().getMethod(methodName);
            method.invoke(bean);
        } else if (invokeTarget.startsWith("class:")) {
            // 类调用方式: class:com.example.TaskClass.methodName
            String classCall = invokeTarget.substring(6);
            int lastDotIndex = classCall.lastIndexOf('.');
            if (lastDotIndex == -1) {
                throw new IllegalArgumentException("Invalid class call format: " + invokeTarget);
            }

            String className = classCall.substring(0, lastDotIndex);
            String methodName = classCall.substring(lastDotIndex + 1);

            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method method = clazz.getMethod(methodName);
            method.invoke(instance);
        } else if (invokeTarget.startsWith("url:")) {
            // HTTP调用方式: url:http://example.com/api/task
            String url = invokeTarget.substring(4);
            // TODO: 实现HTTP调用
            log.info("HTTP call to: {}", url);
        } else {
            throw new IllegalArgumentException("Unsupported invoke target: " + invokeTarget);
        }
    }
}
