package com.tsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Shlomo Goldshtein
 * this project is part of working set of 4 project intend to demonstrate microservice system
 * Including: 
 * 1) Edge server (or proxy server) that routes external REST calls to the right microservice, 
 *     see secure-microservice-api-proxy a  spring cloud edge server using Zuul.
 * 2) Service registry server, where every microservice register itself with it, 
 *     and can use it to find other services. see secure-microsrevice-registry - spring cloude service registry using Eureka
 * 3) an example of microservice that implement spring SAML single sign on implementation. 
 *     see secure-saml-microservice - using spring security with SAML protocol for SSO (single sighn on)
 * 4) A service that implement spring security to authenticate calls from different servicses
 *     and uses hazelcast distributed cache as it's user data center.
 *     see secure-rest-microservice-hazelcast - using spring security to authenticate REST call using basic http and using distributed cache as users data center
 *     
 * this is an Edge server (sometime also called API server, or proxy server).
 * we are using spring cloud for it and it uses Netflix contribution to the project called Zuul (like the name of the gate keeper in the movie ghost buster)
 * the main purpose of this server is to make unify front for services and clients, that need not to know
 * whats the ip or even port of the different services, they connect only to this edge server
 * and based on the URL path it will redirect them to the correct service
 * in this example this server also don't need to know the ip and port of the service, it uses the Eureka service registry
 * that we defined in the microservice-registry project to get the service details by it's id 
 *
 * just add the "@EnableZuulProxy" annotation to the class define the routs and the Eureka url in the application.yml (or property, what you prefer)
 * and thats it, the proxy server is ready to go
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableZuulProxy
public class ApiGateway {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiGateway.class, args);

	}
	/**
	 * adding this cors filter to enable cross origin 
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("OPTIONS");
	    config.addAllowedMethod("HEAD");
	    config.addAllowedMethod("GET");
	    config.addAllowedMethod("PUT");
	    config.addAllowedMethod("POST");
	    config.addAllowedMethod("DELETE");
	    config.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}	

}
