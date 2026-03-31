package com.chh_shu.banking_app;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.chh_shu.banking_app")
@EnableJpaRepositories(basePackages = {"com.chh_shu.banking_app.Entity", "com.chh_shu.banking_app.DAO", "com.chh_shu.banking_app.Service"})
public class BankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

}
