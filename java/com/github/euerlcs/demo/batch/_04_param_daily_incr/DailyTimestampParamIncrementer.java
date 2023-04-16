package com.github.euerlcs.demo.batch._04_param_daily_incr;

import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class DailyTimestampParamIncrementer implements JobParametersIncrementer {

	@Override
	public JobParameters getNext(JobParameters parameters) {
		return new JobParametersBuilder(parameters).addLong("daily", new Date().getTime()).toJobParameters();
	}
}
