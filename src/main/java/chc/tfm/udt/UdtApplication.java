package chc.tfm.udt;

import chc.tfm.udt.servicio.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UdtApplication implements CommandLineRunner {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	IUploadFileService uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(UdtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*	vamos a utilizar estos metodos para desarrollo de la aplicación
			no dejar residuos cada vez que iniciemos la app
		  */
		uploadFileService.deleteAll();
		uploadFileService.init();
		/**
		 * Vamos a crear 1 encoder para nuestras constraseñas 1234 , con el bucle vamos a crear 2 contraseñas del mismo
		 * String , BCrypt es tan potente porque nos permite generar contraseñas distintas con 1 mismo string.
		 */
		String password = "1234";
		for (int i = 0; i<2; i++){
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
	}
}
