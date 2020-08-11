package ru.pavlov.yandex.disk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class yandexDiskConfig {

	@Value("${yandex.disk.token}")
	private String token;
	
	@Bean
	public YandexDiskConnector yandexDiskConnector() {
		YandexDiskConnector ydConnector =	new YandexDiskConnector();
		ydConnector.setToken(token);
		return ydConnector;
	}
	
}
