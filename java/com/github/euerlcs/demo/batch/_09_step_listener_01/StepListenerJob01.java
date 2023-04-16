package com.github.euerlcs.demo.batch._09_step_listener_01;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class StepListenerJob01 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	int timer = 10;

	@Bean
	MyStepListener myStepListener() {
		return new MyStepListener();
	}

	@Bean
	ItemReader<String> itemReader090101() {
		return new ItemReader<String>() {
			@Override
			public String read()
					throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				System.out.println("----------read----11-------");
				if (timer > 0) {
					timer--;
					return "read-ret" + timer;
				} else {
					return null;
				}
			}

		};
	}

	@Bean
	ItemProcessor<String, String> itemProcessor090101() {
		return new ItemProcessor<String, String>() {

			@Override
			public String process(String item) throws Exception {
				System.out.println("----------process---22--------");
				return "process-ret-->" + item;
			}
		};
	}

	@Bean
	ItemWriter<String> itemWriter090101() {
		return new ItemWriter<String>() {

			@Override
			public void write(List<? extends String> items) throws Exception {
				System.out.println(items);

			}
		};
	}

	@Bean
	Step step090101() {

		return stepBuilderFactory.get("step090101").<String, String>chunk(3).reader(itemReader090101())
				.processor(itemProcessor090101()).writer(itemWriter090101())
				.listener(myStepListener()).build();
	}

	@Bean
	Job job090101() {
		return jobBuilderFactory.get("step-listener-job-01").start(step090101()).incrementer(new RunIdIncrementer())
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(StepListenerJob01.class, args);
	}

}
