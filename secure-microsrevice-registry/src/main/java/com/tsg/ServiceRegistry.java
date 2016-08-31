package com.tsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * @author Shlomo Goldshtein
 * this project is part of working set of 4 project intend to demonstrate microservice system
 * includin: 
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
 *  this spring boot application is used as service registry, all other microservices on start up will register them self
 *  with this service, you can than look on this service dashboard to see the status of the service
 *  the dashboard can be accessed <ip>:<port> on this case we set on service-registry.yml for this application to run on port 1111 so it will be localhost:1111
 *   
 *   the "@SpringBootApplication" annotation makes this application automatically a spring boot app
 *   and you only need to call the SpringApplication.run() to start the server
 *   
 *   now spring boot magic comes to life just by adding the "@EnableEurekaServer" annotation
 *   we made it an Eureka service registry, thats it, we finished coding
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistry {
	public static void main(String[] args) {
	   // Tell Boot to look for service-registry.yml
		System.setProperty("spring.config.name", "service-registry");
		SpringApplication.run(ServiceRegistry.class, args);
	}
}
