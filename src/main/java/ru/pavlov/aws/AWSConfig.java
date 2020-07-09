package ru.pavlov.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

	@Value("${aws.accesskey}")
	private String accesskey;
	
	@Value("${aws.secretkey}")
	private String secretkey;
	
	@Bean
	public AWSConnector getAWSConnector() {
		AWSConnector connector = new AWSConnector();
		connector.setCredentials(accesskey, secretkey);
		return connector;
	}
}
