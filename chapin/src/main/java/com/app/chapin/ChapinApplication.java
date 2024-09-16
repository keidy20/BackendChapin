package com.app.chapin;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ChapinApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChapinApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("Chapin Spring Boot 3 API")
					.version("1.0")
					.description("API de servicios de la app chapin")
					.termsOfService("https://swagger.io/terms/")
					.license(new License().name("Apache 2.0").url("https://springdoc.org")));
				/*.servers(List.of(
						new Server().url("https://chapin-446813484970.us-central1.run.app/").description("Servidor en Cloud Run") // Configurar https
				));*/
	}

}
