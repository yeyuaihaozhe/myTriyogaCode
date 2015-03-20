package com.sd.wq.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;



public class ScheduleUtil {
	private static Logger logger=Logger.getLogger(ScheduleUtil.class);
	private static Map<String,Scheduler> map=new HashMap<String,Scheduler>();
	
	private static String getSchedulerKey(String jobName,String group){
		return group+jobName;
	}
	/**\
	 * 添加永久重复的任务
	 * @param name
	 * @param group
	 * @param startTime
	 * @param intervalInSeconds
	 * @param class1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean addJobRepeatForever(String jobName,String group,Timestamp startTime,int intervalInSeconds,Class class1){
		try{
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();

			JobDetail job = newJob(class1).withIdentity(jobName,group).build();
			SimpleScheduleBuilder simpleSchedule = simpleSchedule();
			simpleSchedule = simpleSchedule.withIntervalInSeconds(intervalInSeconds);
			simpleSchedule = simpleSchedule.repeatForever();
			
			TriggerBuilder<Trigger> newTrigger = newTrigger();
			newTrigger = newTrigger.withIdentity(jobName+"trigger", group);
			newTrigger = newTrigger.startAt(startTime);
			SimpleTrigger trigger = newTrigger.withSchedule(simpleSchedule).build();
			
			Date ft = sched.scheduleJob(job, trigger);
			logger.info(job.getKey() + " will run at: " + ft + " and repeat: "
					+ trigger.getRepeatCount() + " times, every "
					+ trigger.getRepeatInterval() / 1000 + " seconds");
			
			sched.start();
			map.put(getSchedulerKey(jobName, group), sched);
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return false;
	}
	public static void shutdown(String jobName,String group){
		try {
			Scheduler sched =map.get(getSchedulerKey(jobName, group));
			if(null!=sched&&sched.isStarted()){
				sched.shutdown();
			}
		} catch (SchedulerException e) {
			logger.error(e.getMessage(),e);
		}
	}
//	public static void main(String[] args){
//		Timestamp startTime=Util.toParseTimestamp("2012-01-01 1:1:1.0");
//		System.out.println(startTime);
//		String name="job1";
//		String group="group1";
//		Timestamp startTime=new Timestamp(System.currentTimeMillis());
//		ScheduleUtil.addJobRepeatForever(name, group, startTime,10,SimpleJob.class);
//		name="job2";
//		group="group1";
//		ScheduleUtil.addJobRepeatForever(name, group, startTime,5,SimpleJob.class);
//	}
}
