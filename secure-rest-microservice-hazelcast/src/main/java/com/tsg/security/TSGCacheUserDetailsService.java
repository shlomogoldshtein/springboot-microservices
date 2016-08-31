package com.tsg.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.tsg.conf.HazelcastConfig;
import com.tsg.dao.UsersDAO;
import com.tsg.domain.BDAUser;
/**
 * @author Shlomo Goldshtein
 * 
 * this is our custom  user detail service, that will be used by spring security to authenticate
 * REST call and sand validate they are authorized
 * 
 *event that we are not using Database I still use DAO (Data Accesses Objects) to accesses the cache
 *in the end it's not part of the business logic to know where the data is
 *it's the DAO objects to know, they just expose a method for getting it, decoupling the data access from the business logic
 *in fact in other cache solutions, it's good to let the DAO to decide if it get data from Cache or fresh data from DB
 *based on the validity of the data
 */
@Service
public class TSGCacheUserDetailsService implements UserDetailsService{
	//so we ask spring to inject our DAO that handle users data
	@Autowired
	UsersDAO usersRepository;
	//init logger
		private static final Logger logger = LogManager.getLogger(TSGCacheUserDetailsService.class);
	/**
	 * here we override spring UserDetailsService with our business logic
	 * as you remember in http basic authentication, the caller set username and password in the request header
	 * in our case it's the security token
	 * don't worry about the need to read this from the request header your self
	 * spring magic to the rescue again, it will inject the usertoken to the method for you
	 */
	@Override
	public UserDetails loadUserByUsername(String usertoken) throws UsernameNotFoundException {
		//now we just need to call our DAO and ask him to get from users repository (we don't care if its DB or Cache)
		//this user details
		BDAUser bdauser = usersRepository.findUserByToken(usertoken);
	if (bdauser == null) {
		//if we can't find it, this is not authenticated user/service
		logger.warn(" user {} is not autenticated",usertoken);
		throw new UsernameNotFoundException("Username " + usertoken + " not found");
	}
		//if we found the user/service it's authenticated
		//so will create spring security User object and return it
		//for this demo this is the only check I do, I could for example check it's role and if it's not a service to reject it
		logger.debug(" user {} is  autenticated, and have the roles of ",usertoken,bdauser.getAuthorities());
		return new User(usertoken, usertoken, bdauser.getAuthorities());
	}
}
