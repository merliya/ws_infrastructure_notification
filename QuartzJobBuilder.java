package com.jbhunt.infrastructure.notification.job.builder;

import static org.quartz.JobBuilder.newJob;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import com.jbhunt.infrastructure.notification.job.ExactTargetOAuthAccessTokenJob;
import com.jbhunt.infrastructure.notification.service.ExactTargetOAuthAccessTokenService;

/**
 * QuartzJobBuilder provides method to build job and build trigger for exact
 * target access token refresh.
 * 
 * @author rcon335
 *
 */
@Component
public class QuartzJobBuilder {

	/**
	 * buildJob method is used to build a job for exact target access token
	 * refresh.
	 * 
	 * @param jobName
	 * @param applicationName
	 * @param etOAuthAccessTokenService
	 * @return
	 */
	public JobDetail buildJob(String jobName, String applicationName,
			ExactTargetOAuthAccessTokenService etOAuthAccessTokenService) {
		JobDetail job = newJob(ExactTargetOAuthAccessTokenJob.class).withIdentity(applicationName, jobName)
				.usingJobData("applicationName", applicationName).usingJobData("jobName", jobName).build();
		job.getJobDataMap().put("etOAuthAccessTokenService", etOAuthAccessTokenService);
		return job;
	}

	/**
	 * buildTrigger method is used to build a trigger for exact target access
	 * token refresh.
	 * 
	 * @param jobName
	 * @param applicationName
	 * @param triggerStartTime
	 * @return
	 */
	public Trigger buildTrigger(String jobName, String applicationName, Date triggerStartTime) {
		return TriggerBuilder.newTrigger().withIdentity(applicationName, jobName).startAt(triggerStartTime).build();
	}

}
