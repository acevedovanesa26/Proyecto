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
		try {
			// Intenta cargar el archivo .env desde el classpath EN ESTA MONDA
			Dotenv dotenv = Dotenv.configure()
					.directory("target/classes") // Ruta al classpath
					.filename(".env")
					.ignoreIfMalformed()
					.ignoreIfMissing()
					.load();

			String urlBd = dotenv.get("URLBD", "jdbc:postgresql://localhost:5432/defaultdb");
			String usernameBd = dotenv.get("USERNAMEBD", "defaultuser");
			String passwordBd = dotenv.get("PASSWORDBD", "defaultpass");
			String adminUsername = dotenv.get("ADMIN_USERNAME", "admin");
			String adminPassword = dotenv.get("ADMIN_PASSWORD", "admin");

			System.setProperty("URLBD", urlBd);
			System.setProperty("USERNAMEBD", usernameBd);
			System.setProperty("PASSWORDBD", passwordBd);
			System.setProperty("ADMIN_USERNAME", adminUsername);
			System.setProperty("ADMIN_PASSWORD", adminPassword);

			System.out.println("Archivo .env cargado correctamente.");
		} catch (Exception e) {
			System.err.println("Error al cargar el archivo .env: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

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
			// Manejador para las imágenes
			registry.addResourceHandler("/imagenes/**")
					.addResourceLocations("file:./imagenes/");

			// Manejador para otras rutas estáticas, si ya está configurado
			registry.addResourceHandler("/uploads/**")
					.addResourceLocations("file:uploads/");
		}
	}
}
