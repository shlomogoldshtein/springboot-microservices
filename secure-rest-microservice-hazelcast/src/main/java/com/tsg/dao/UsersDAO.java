package com.tsg.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hazelcast.core.HazelcastInstance;
import com.tsg.common.Constants;
import com.tsg.domain.BDAUser;
/**
 * @author Shlomo Goldshtein
 * This is a DAO (data acess object) we added to look for user or service
 * in our user data center, in this case we store the users Hazelcast distributed cache
 * but we could have implemented other logic to get the data from DB or maybe from other service
 * we use the spring "@Repository" annotation to tell spring it's a bean, spring will initiate it and run it as singleton by default
 */
@Repository
public class UsersDAO {
	//we ask spring to inject the referance to our Hazel casst instace
	@Autowired
	HazelcastInstance hazelcastInstance;
	/**
	 * will be used by security system to see if user is registered
	 * @see com.tsg.security.TSGCacheUserDetailsService;
	 * @param token - the security token the caller send as part of the http basic autentication
	 * @return
	 */
	public BDAUser findUserByToken(String token){
		//get from cache the reference to our users Map
		Map<String, BDAUser> registered=hazelcastInstance.getMap(Constants.CACHE_REGISTERED_SERVICES);
		//if we found the user return the domain with it's details
		if(registered.containsKey(token)){
			return registered.get(token);
		}else{
			//else return null
			return null;
		}		
	}
}
