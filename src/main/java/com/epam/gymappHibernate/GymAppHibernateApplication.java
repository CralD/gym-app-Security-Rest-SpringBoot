package com.epam.gymappHibernate;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class GymAppHibernateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymAppHibernateApplication.class, args);
	}

}
