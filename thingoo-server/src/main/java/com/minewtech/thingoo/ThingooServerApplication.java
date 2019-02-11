package com.minewtech.thingoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages ={"com.minewtech.thingoo.repository"})
@EntityScan(basePackages ={ "com.minewtech.thingoo.model"})
@EnableTransactionManagement
@IntegrationComponentScan
public class ThingooServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThingooServerApplication.class, args);
	}
}
