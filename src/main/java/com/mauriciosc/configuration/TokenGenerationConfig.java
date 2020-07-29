package com.mauriciosc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mauriciosc.core.interceptor.TokenGeneratorInterceptor;
import com.mauriciosc.core.model.response.TokenModelResponse;

/**
 * Defines the configurations of beans that are related to the token generation
 * process.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-25
 *
 */
@Configuration
public class TokenGenerationConfig implements WebMvcConfigurer {
	
	@Autowired
	private TokenGeneratorInterceptor tokenGeneratorInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenGeneratorInterceptor);
	}
	
	/**
	 * Defines an instance bean for the {@link TokenModelResponse} class.
	 * 
	 * @return {@code TokenModelResponse} - bean instance.
	 */
	@Bean
	@Scope("singleton")
	public TokenModelResponse getTokenModelResponse() {
		return new TokenModelResponse();
	}
	
	/**
	 * Defines an instance bean for the {@link RestTemplate} class.
	 * 
	 * @return {@code RestTemplate} - bean instance.
	 */
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}