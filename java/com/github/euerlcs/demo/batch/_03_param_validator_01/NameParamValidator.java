package com.github.euerlcs.demo.batch._03_param_validator_01;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class NameParamValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {

		String name = parameters.getString("name");
		if (!StringUtils.hasText(name)) {
			throw new JobParametersInvalidException("name can not null");
		}
	}
}
