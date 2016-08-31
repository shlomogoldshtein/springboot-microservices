package com.tsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
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
 *
 *this project will demonstrate: 
 *1) how to use spring security to authenticate remote calls using http basic authentication
 *3) how to initiate Hazelcast distributed cache, and how to use it
 *4) how to override spring security get user method to retrieve user data from distributed cache
 *5) how to make our microservice to register it self with the service registry, so other services will find it
 *it will also expose one REST api, that the SAML microservice will call after it authenticated the user, 
 *and only if user was authenticated with SAML it will call this service
 *this service will check if the SAML service is allowed to call it using our internal security
 *
 *our internal security based on distributed cache, each service o start, creates a security token, and put it inside a distributed cache Map
 *on every internal REST call, this token is sent using HTTP Basic authentication to the other service
 *the other service will look in the cache if it's a known registered token, and if so will allow the call
 *@see com.tsg.conf.HazelcastConfig how we intiate Hazelcast instance, creeating security token, and registering it
 *@see com.tsg.conf.SecurityConfig how we set security for this service
 *
 *---IMPORTANT!!! before running this service, you need to run the service registry first, else it will throw not able to connect exception
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
public class SecureService {
	/*
	 * this is spring boot main class, annotating it with "@SpringBootApplication" do the work, and then in main calling SpringApplication.run()
	 * the "@EnableAutoConfiguration" tell spring boot try to do as much configuration needed automatically as possible
	 * spring boot will analyze the code and dependencies used by this service, and will analyze what it needs
	 * in addition you can add your configuration in application.yml (you can also use instead application.properties)
	 * and will also look for classes annotated with @Configuration, see class at com.tsg.conf package
	 * 
	 * the "@EnableDiscoveryClient" annotation is all you need to add, to make this service register him self  with the service registry
	 * it will read the URL of the service registry from the application.yml file
	 * see comments in this file for more details 
	 * 
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecureService.class, args);
	}
}
