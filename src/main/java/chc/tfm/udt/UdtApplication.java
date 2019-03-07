package chc.tfm.udt;

import chc.tfm.udt.servicio.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UdtApplication implements CommandLineRunner {
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
	}
}
