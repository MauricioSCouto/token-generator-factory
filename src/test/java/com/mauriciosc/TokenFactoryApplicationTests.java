package com.mauriciosc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.mauriciosc.configuration.TokenGenerationTestConfig;

/**
 * Tests application bootstrap.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-28
 *
 */
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
@Import(TokenGenerationTestConfig.class)
class TokenFactoryApplicationTests {

	/**
	 * Tests application context load.
	 */
	@Test
	void contextLoads() {
	}

}
