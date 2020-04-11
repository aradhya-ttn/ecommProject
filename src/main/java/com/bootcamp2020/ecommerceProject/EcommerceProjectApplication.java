package com.bootcamp2020.ecommerceProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;


@SpringBootApplication
public class EcommerceProjectApplication {

	public static void main(String[] args) { SpringApplication.run(EcommerceProjectApplication.class, args);
	}


	@Bean
	public LocaleResolver localeResolver(){
		SessionLocaleResolver localeResolver= new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return  localeResolver;
	}
	@Bean
	ResourceBundleMessageSource messageSource(){
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return  messageSource;
	}

}
