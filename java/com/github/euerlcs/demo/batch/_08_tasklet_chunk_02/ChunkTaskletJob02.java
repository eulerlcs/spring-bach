package com.github.euerlcs.demo.batch._08_tasklet_chunk_02;

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
public class ChunkTaskletJob02 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	int timer = 10;

	@Bean
	ItemReader<String> itemReader080102() {
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
	ItemProcessor<String, String> itemProcessor080102() {
		return new ItemProcessor<String, String>() {

			@Override
			public String process(String item) throws Exception {
				System.out.println("----------process---22--------");
				return "process-ret-->" + item;
			}
		};
	}

	@Bean
	ItemWriter<String> itemWriter080102() {
		return new ItemWriter<String>() {

			@Override
			public void write(List<? extends String> items) throws Exception {
				System.out.println(items);

			}
		};
	}

	@Bean
	Step step080102() {
		return stepBuilderFactory.get("step080102").<String, String>chunk(3).reader(itemReader080102())
				.processor(itemProcessor080102()).writer(itemWriter080102()).build();
	}

	@Bean
	Job job080102() {
		return jobBuilderFactory.get("tasklet-simple-job-01").start(step080102()).incrementer(new RunIdIncrementer())
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ChunkTaskletJob02.class, args);
	}

}
