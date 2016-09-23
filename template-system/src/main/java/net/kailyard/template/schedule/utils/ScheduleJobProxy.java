package net.kailyard.template.schedule.utils;

import net.kailyard.template.utils.Constants;
import net.kailyard.template.utils.SpringContextHelper;
import net.kailyard.template.schedule.entity.ScheduleJob;
import net.kailyard.template.schedule.service.ScheduleJobService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

@DisallowConcurrentExecution
public class ScheduleJobProxy implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobProxy.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long jobId = (Long) context.getMergedJobDataMap().get(Constants.SCHEDULE_JOB_KEY);

        ScheduleJobService scheduleJobService = SpringContextHelper.getBean(ScheduleJobService.class);
        ScheduleJob scheduleJob = scheduleJobService.findOne(jobId);
        if(scheduleJob==null){
            LOGGER.warn("can't find the id[{}] schedule.", jobId);
        }

        Date lastRunTime = scheduleJob.getLastRunTime();
        if (null != context.getPreviousFireTime() && lastRunTime.compareTo(context.getPreviousFireTime()) > 0) {
            LOGGER.info("schedule:[{}] skipped.", scheduleJob);
            return;
        }

        try {
            LOGGER.info("begin schedule:[{}].", scheduleJob);

            Class<?> clazz = Class.forName(scheduleJob.getJobClass());

            Object instance = SpringContextHelper.getBean(clazz);
            Method method = clazz.getDeclaredMethod(scheduleJob.getJobMethod());
            method.invoke(instance);

            scheduleJob.setLastRunTime(context.getFireTime());
            scheduleJobService.save(scheduleJob);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            LOGGER.error("schedule:[{}] start failed.", scheduleJob, e);
        } catch (Exception e){
            LOGGER.error("schedule:[{}] start failed.", scheduleJob, e);
        }
        LOGGER.info("end schedule:[{}].", scheduleJob);
    }
}
