package org.mickael.librarymsloan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("org.mickael.librarymsloan")
@EnableDiscoveryClient
public class LibraryMsLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryMsLoanApplication.class, args);
	}

}
