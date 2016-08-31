package com.tsg.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.tsg.security.TSGCacheUserDetailsService;
/**
 * @author Shlomo Goldshtein
 * in this class we intialize the spring security
 * we define which URLS are accessible and for which role, in this example for simplicity
 * we set all URL as protected and using http basic authentication (username password are set on request header, and hashed)
 * on production will also want to add TLS (SSL) calls
 *
 *usually on basic authentication, you read the user name password from header
 *then you run loadUserByUsername from DB and check it's credentials and permeations
 *in our case we want the users (in our case the users are services) to be stored in memory, on distributed cache Map
 *instead of sending username password on header, will send the security token (that we created on HazelcastConfig)as username and as password
 *so I demonstrate on this class how I use our class TSGCacheUserDetailsService that override spring security default UserDetailsService
 *to validate the user, it read from header the security token, and look in  cache users table and return it's details if found or else deny
 *
 *I use two annotation on class
 *"@Configuration" to tell boot it's a configuration file and it need to run it on start
 *"@EnableWebSecurity" tells boot to activate the security roles
 *in this class will use just very basic abilities of the spring security arsenal
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig   extends WebSecurityConfigurerAdapter{
	//will use our custom user service to authenticate call 
	//from tokens authorized in distributed cache, so lets ask spring to inject a referance to this service
	@Autowired
	private TSGCacheUserDetailsService userDetailsService;
	
	/**
	 * I am overriding spring WebSecurityConfigurerAdapter to return our user detail service instead of the default
	 */
   @Override  
    public UserDetailsService userDetailsServiceBean() throws Exception {  
        return userDetailsService;  
    }
   /**
    * here I define which URL and calls are protected and also other definitions
    */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//for this demo i defines that all calls to this service most be by authorized user or service
		http.authorizeRequests().anyRequest().fullyAuthenticated();
		//I tell it to use http basic authentication
		http.httpBasic();
		//for development i disable cross scripting attack defense
		//better to add to external property file a property enable.csrf=false than read it here
		//and do if (false){http.csrf().disable();}
		//so on production, will not need to go to code to eliminate
		http.csrf().disable();
	}
	/**
	 * also override for the default AuthenticationManagerBuilder the user details service
	 * with our service
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
}
