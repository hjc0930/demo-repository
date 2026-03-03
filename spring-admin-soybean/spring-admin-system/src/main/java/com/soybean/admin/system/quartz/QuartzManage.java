package com.soybean.admin.system.quartz;

import com.soybean.admin.data.entity.SysJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Quartz定时任务管理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzManage {

    private final Scheduler scheduler;

    /**
     * 创建或更新定时任务
     */
    public void createJob(SysJob job) throws SchedulerException {
        Class<? extends Job> jobClass = QuartzJobBean.class;
        String jobName = job.getJobId().toString();
        String jobGroup = job.getJobGroup();

        // 构建JobDetail
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
            .withIdentity(jobName, jobGroup)
            .withDescription(job.getJobName())
            .build();

        // 设置任务参数
        jobDetail.getJobDataMap().put("task", job);

        // 构建触发器
        CronTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(jobName, jobGroup)
            .withDescription(job.getJobName())
            .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
            .build();

        // 判断是否并发执行
        if ("0".equals(job.getConcurrent())) {
            // 禁止并发
            trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withDescription(job.getJobName())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing())
                .build();
        } else {
            // 允许并发
            trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withDescription(job.getJobName())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())
                    .withMisfireHandlingInstructionFireAndProceed())
                .build();
        }

        // 检查任务是否已存在
        if (scheduler.checkExists(new JobKey(jobName, jobGroup))) {
            // 删除旧任务
            scheduler.deleteJob(new JobKey(jobName, jobGroup));
        }

        // 创建任务
        scheduler.scheduleJob(jobDetail, trigger);

        // 如果任务状态为暂停，则暂停任务
        if ("1".equals(job.getStatus())) {
            pauseJob(jobName, jobGroup);
        }

        log.info("Job created: {} - {}", jobGroup, jobName);
    }

    /**
     * 暂停定时任务
     */
    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
        log.info("Job paused: {} - {}", jobGroup, jobName);
    }

    /**
     * 恢复定时任务
     */
    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
        log.info("Job resumed: {} - {}", jobGroup, jobName);
    }

    /**
     * 立即执行定时任务
     */
    public void executeJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.triggerJob(jobKey);
        log.info("Job executed immediately: {} - {}", jobGroup, jobName);
    }

    /**
     * 删除定时任务
     */
    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
        log.info("Job deleted: {} - {}", jobGroup, jobName);
    }

    /**
     * 检查任务是否存在
     */
    public boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        return scheduler.checkExists(new JobKey(jobName, jobGroup));
    }

    /**
     * 启动所有任务
     */
    public void startAllJobs() throws SchedulerException {
        scheduler.resumeAll();
        log.info("All jobs resumed");
    }

    /**
     * 暂停所有任务
     */
    public void pauseAllJobs() throws SchedulerException {
        scheduler.pauseAll();
        log.info("All jobs paused");
    }

    /**
     * 关闭调度器
     */
    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
        log.info("Scheduler shutdown");
    }

    /**
     * 根据Cron表达式获取下次执行时间
     */
    public String getNextCronTime(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            Date nextTime = cron.getNextValidTimeAfter(new Date());
            if (nextTime != null) {
                return nextTime.toString();
            }
        } catch (Exception e) {
            log.error("Failed to parse cron expression: {}", cronExpression, e);
        }
        return null;
    }
}
