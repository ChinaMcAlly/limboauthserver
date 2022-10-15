package cn.moonmc.limboauthserver;

import cn.moonmc.limbo.works.menu.InventoryManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LimboauthserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimboauthserverApplication.class, args);
		InventoryManager.run();
	}

}
