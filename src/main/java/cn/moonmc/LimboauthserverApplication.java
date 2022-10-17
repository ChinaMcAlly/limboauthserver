package cn.moonmc;

import cn.moonmc.ability.login.Login;
import cn.moonmc.limboAdd.NanoLimbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LimboauthserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimboauthserverApplication.class, args);
		//启动NanoLimbo
		NanoLimbo.run();
		//启动Login
		Login.run();
	}

}
