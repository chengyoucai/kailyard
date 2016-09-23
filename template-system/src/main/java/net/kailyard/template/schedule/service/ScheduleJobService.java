package net.kailyard.template.schedule.service;


import com.google.common.collect.Maps;
import net.kailyard.template.common.exception.ApplicationRuntimeException;
import net.kailyard.template.common.persistence.SearchFilter;
import net.kailyard.template.common.security.SecurityUtil;
import net.kailyard.template.common.service.BaseService;
import net.kailyard.template.schedule.entity.ScheduleJob;
import net.kailyard.template.schedule.utils.ScheduleJobProxy;
import net.kailyard.template.utils.Constants;
import net.kailyard.template.schedule.utils.ScheduleJobUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Lazy(false)
@Service
@Transactional
public class ScheduleJobService extends BaseService<ScheduleJob, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobService.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    private void init() throws SchedulerException {
        Map<String, Object> param = Maps.newHashMap();
        param.put(SearchFilter.Operator.EQ + Constants.SEP_UNDERLINE + "jobState", ScheduleJobUtils.JobStatus.RUNNING.toString());
        List<ScheduleJob> scheduleJobList = findAll(param);

        LOGGER.info("begin init total:[{}] schedule.", scheduleJobList.size());

        for (ScheduleJob scheduleJob : scheduleJobList) {
            scheduleJob(scheduleJob);
        }
        LOGGER.info("end init total:[{}] schedule.");
    }

    public void scheduleJob(Long jobId) throws SchedulerException {
        ScheduleJob scheduleJob = findOne(jobId);
        if(scheduleJob==null){
            throw new ApplicationRuntimeException("未发现此任务。id=" + jobId);
        }

        scheduleJob.setJobState(ScheduleJobUtils.JobStatus.RUNNING.toString());
        scheduleJob.setUpdateUserId(SecurityUtil.getCurrentUserId());
        scheduleJob.setUpdateTime(new Date());
        //修改状态保存
        ScheduleJob savedScheduleJob = save(scheduleJob);
        //启动任务
        scheduleJob(savedScheduleJob);
    }

    public void unScheduleJob(Long jobId) {
        ScheduleJob scheduleJob = findOne(jobId);
        if(scheduleJob==null){
            throw new ApplicationRuntimeException("未发现此任务。id=" + jobId);
        }

        scheduleJob.setJobState(ScheduleJobUtils.JobStatus.STOPPED.toString());
        scheduleJob.setUpdateUserId(SecurityUtil.getCurrentUserId());
        scheduleJob.setUpdateTime(new Date());

        //修改状态保存
        ScheduleJob savedScheduleJob = save(scheduleJob);

        //停止任务
        JobKey jobKey = JobKey.jobKey(savedScheduleJob.getName(), savedScheduleJob.getJobGroup());

        try {
            schedulerFactoryBean.getScheduler().deleteJob(jobKey);
        } catch (SchedulerException e) {
            LOGGER.error("can't stop schedule[{}].", savedScheduleJob, e);
            throw new ApplicationRuntimeException("停止此任务失败。name:=" + savedScheduleJob.getName());
        }
    }

    protected void scheduleJob(ScheduleJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getName(), scheduleJob.getJobGroup());
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getJobCron());

        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //检查此定时器任务是否存在
            if (trigger != null) {
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                scheduler.rescheduleJob(triggerKey, trigger);
            } else {
                JobDetail jobDetail = JobBuilder.newJob(ScheduleJobProxy.class)
                        .withIdentity(scheduleJob.getName(), scheduleJob.getJobGroup()).build();

                jobDetail.getJobDataMap().put(Constants.SCHEDULE_JOB_KEY, scheduleJob.getId());

                trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getName(), scheduleJob.getJobGroup())
                        .withSchedule(scheduleBuilder).build();

                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            LOGGER.error("can't start schedule[{}].", scheduleJob, e);
            throw new ApplicationRuntimeException("启动此任务失败。name:=" + scheduleJob.getName());

        }
    }

    public void updateCron(Long jobId, String cron) throws SchedulerException {
        ScheduleJob scheduleJob = findOne(jobId);
        if(scheduleJob==null){
            throw new ApplicationRuntimeException("未发现此任务。id=" + jobId);
        }

        if (cron.equals(scheduleJob.getJobCron())) {
            return;
        }

        scheduleJob.setJobCron(cron);
        scheduleJob.setUpdateUserId(SecurityUtil.getCurrentUserId());
        scheduleJob.setUpdateTime(new Date());
        ScheduleJob savedScheduleJob = save(scheduleJob);

        if (ScheduleJobUtils.JobStatus.RUNNING.toString().equals(savedScheduleJob.getJobState())) {
            scheduleJob(savedScheduleJob);
        }
    }
}
