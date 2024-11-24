package co.ucentral.VotosSmart;

import co.ucentral.VotosSmart.servicios.AdministradorServicio;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class VotosSmartApplication {

	private final AdministradorServicio administradorServicio;

	public VotosSmartApplication(AdministradorServicio administradorServicio) {
		this.administradorServicio = administradorServicio;
	}

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("URLBD", dotenv.get("URLBD"));
		System.setProperty("USERNAMEBD", dotenv.get("USERNAMEBD"));
		System.setProperty("PASSWORDBD", dotenv.get("PASSWORDBD"));
		System.setProperty("ADMIN_USERNAME", dotenv.get("ADMIN_USERNAME"));
		System.setProperty("ADMIN_PASSWORD", dotenv.get("ADMIN_PASSWORD"));
		SpringApplication.run(VotosSmartApplication.class, args);
	}

	@Bean
	public CommandLineRunner init() {
		return args -> {
			administradorServicio.inicializarAdministrador();
		};
	}


	@Configuration
	public class WebConfig implements WebMvcConfigurer {

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/uploads/**")
					.addResourceLocations("file:uploads/");
		}
	}
}
