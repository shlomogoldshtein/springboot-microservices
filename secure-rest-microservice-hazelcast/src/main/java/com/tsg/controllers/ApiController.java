package com.tsg.controllers;


import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *  @author Shlomo Goldshtein
 *
 *this is a REST controller, to test our security setting
 *we use spring web "@RestController" annotation, to let spring know it needs to rout web call to it
 *and then we use the @@RequestMapping annotation to tell spring web wich urls (paths) need to be rout to this controller
 *in this example i demonstrate also i can add @RequestMapping to class and to method
 *so if user wants to call samlApi it will need to call localhost:8083\tsgapi\samluser
 *in this example it needs to be a POST call
 */
@RestController
@RequestMapping("/tsgapi")
public class ApiController {

	//init logger
	private static final Logger logger = LogManager.getLogger(ApiController.class);
	
	/**
	 * here for testing i just implement a simple REST POST call
	 * if the user will not be authenticated, the call will never reach here, spring security will block it
	 * if it reached this method it means call was autenticated
	 * @param session
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "samluser", method = RequestMethod.POST)
	public ResponseEntity<String>  samlapi(HttpSession session, String token) {
		logger.info("token is: {}",token);
		 return new ResponseEntity<String>("hello", HttpStatus.OK);
	}	
}
