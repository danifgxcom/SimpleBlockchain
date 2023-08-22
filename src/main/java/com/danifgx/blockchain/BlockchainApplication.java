package com.danifgx.blockchain;

import com.danifgx.blockchain.model.Blockchain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.Security;

@SpringBootApplication
public class BlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainApplication.class, args);
	}
	@Bean
	public CommandLineRunner demo(Blockchain blockchain) {
		return args -> {
			Security.addProvider(new BouncyCastleProvider());
			File file = new File("blockchain.data");
			if (!file.exists()) {
				file.createNewFile();
			}
			String filename = "blockchain.data";
			blockchain.loadBlockchain(filename);

			MainMenu mainMenu = new MainMenu(blockchain);
			mainMenu.start();


			blockchain.saveBlockchain(blockchain.getChain(), filename);

			if (blockchain.isValid()) {
				System.out.println("La cadena de bloques es válida.");
			} else {
				System.out.println("La cadena de bloques no es válida.");
			}
		};
	}

}
