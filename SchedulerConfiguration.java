package com.jbhunt.infrastructure.notification.configuration;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.jbhunt.infrastructure.notification.job.ExactTargetOAuthAccessTokenJob;

@Configuration
public class SchedulerConfiguration {

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory,
			@Qualifier("etOAuthAccessTokenJobTrigger") Trigger etOAuthAccessTokenJobTrigger) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setJobFactory(jobFactory);
		return factory;
	}

	@Bean
	public JobDetailFactoryBean etOAuthAccessTokenJobDetail() {
		return createJobDetail(ExactTargetOAuthAccessTokenJob.class);
	}

	@Bean
	public SimpleTriggerFactoryBean etOAuthAccessTokenJobTrigger(
			@Qualifier("etOAuthAccessTokenJobDetail") JobDetail etOAuthAccessTokenJobDetail) {
		return createTrigger(etOAuthAccessTokenJobDetail);
	}

	private JobDetailFactoryBean createJobDetail(Class<?> jobClass) {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setJobClass(jobClass);
		factoryBean.setDurability(true);
		return factoryBean;
	}

	private SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail) {
		SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
		factoryBean.setJobDetail(jobDetail);
		return factoryBean;
	}

}
