package com.tsg.conf;

import java.security.SecureRandom;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.SocketUtils;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.tsg.common.Constants;
import com.tsg.domain.BDAUser;
import com.tsg.services.LocalStorage;
/**
 * @author Shlomo Goldshtein
 * the "@Configuration" tell spring boot it's a configuration class, it will call it on start
 * in old spring XML configuration, we used to define the beans in the XML
 * Here we can define them  using the "@Bean" annotation and coding the constructor that return the bean instance
 * 
 *in this class we are going to initiate Hazelcast (distributed cache)instance
 *will set a port for it to listen on (so other hazelcast nodes can find it)
 *then will generate a security token this service will  use to authenticate it self when calling other services
 *last will use the distributed cache to create a new Map (or get reference to existing one) that will hold the tokens of all registered services
 *and will add our token to it
 */
@Configuration
public class HazelcastConfig {
	//init logger
	private static final Logger logger = LogManager.getLogger(HazelcastConfig.class);
	//I just created local in memory bean to save things for this service
	//in this case to store this temporary security token, so I can use it later 
	//when calling other services 
	@Autowired
	LocalStorage localstorage;
	
	@Bean(destroyMethod = "shutdown")
	public HazelcastInstance hazelcastInstance() {
		//lets create configuration for the hazel cast
		com.hazelcast.config.Config cfg = new com.hazelcast.config.Config();
		//lets create its network configuration
		NetworkConfig netConfig = new NetworkConfig();
		//we need to set it's listening port, so other Hazelcast cluster node can comunicate with it
		//for this demo will make it simple and get the first available port
		//in real life, we want to control the ports each port open, since we need to tell the IT which ports to open on fire wall
		netConfig.setPort(SocketUtils.findAvailableTcpPort());
		logger.info("Hazelcast port #: {}" ,netConfig.getPort());
		//for this demo will finish configuration by just setting port
		cfg.setNetworkConfig(netConfig);
		//create the new cache instance
		HazelcastInstance instance=Hazelcast.newHazelcastInstance(cfg);
		
		//the cache now is alive, lets start our security part
		//we want to create this service security token and store it a Map call regsitered_servicses
		//this Map will have the security token as the key, and a User domain as value
		//Hazelcast getMap() method will get a reference to this Map, in case the Map is not exist, it will create one
		//in other worlds, the first service to call it will create it, the rest will get reference to it
		//another good practice, not to write the map name as hard coded, but use some constant class
		//to hold all maps name, this way, if you want to change name, you just to change it in constant class
		//it also help avoid typo, since if you will write map name incorrectly, you wont get error
		//but you will get anew map, but not the map you ment to.
		//for this demo, for simplicity, added the constant to class in this project
		//but since the map names and also some of the domains are shared between many services
		//its best to create a common jar project, holding all shared constants and domains, and distribute this jar to all services
		Map<String, BDAUser> registered=instance.getMap(Constants.CACHE_REGISTERED_SERVICES);
		
		  //will use java security utility class to create a security token
		  SecureRandom random = new SecureRandom();
		    byte bytes[] = new byte[20];
		    random.nextBytes(bytes);
		    //thats it the token was created
		    String token = bytes.toString();
		    logger.debug("security token created: {}",token);
		    // now lets create a domain to hold this service information
		    //in this case I made it simple, it will hold the token, 
		    //i added also password and username that are not relevant to service, but maybe to user so will just put the token in all
		    //later you will see that creating spring security User object needs them 
			BDAUser bdauser= new BDAUser();
			bdauser.setToken(token);
			bdauser.setPassword(token);
			bdauser.setUsername(token);
			//this part is important, one thing spring security enable you is to
			//give allow different roles access to different URLS
			//so for this we create a new role called service
			bdauser.setAuthorities(AuthorityUtils.createAuthorityList(Constants.ROLE_SERVICE));
			//sore our token and domain in the memory shared map
			registered.put(token, bdauser);
			//save locally the security token for later use
			//for this case I store just the token, since if will need the domain, we can always take it from cache using this token
			localstorage.setSecurityToken(token);
		return instance;
	}
}
