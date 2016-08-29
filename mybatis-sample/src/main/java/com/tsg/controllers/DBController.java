package com.tsg.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsg.dao.DataDao;
import com.tsg.domain.BigDataDO;
import com.tsg.mybatis.mappers.BigDataMapper;


/**
 * @author Shlomo Goldshtein
 * Controllers are the C part of MVC it's the server beck end part of web calls
 * this used to be in the past the Servlet part (behind the scene it still a servlet)
 * in this specific case we are implementing REST calls, with spring boot no need today
 * to work with jersey or other REST implementations, spring boot way
 * just add "@RestController" to the class and thats it, you have REST controllers.
 * REST calls support different HTTM methods like GET,POST,PUT,DELETE 
 * in bellow example I implemented a GET method that sends parameters on the url called nume_records
 * and on REST path populatedDB, when it arrive, 
 * the method will read the value and do some loop and populate  the database with number of rows sent on request 
 * for example http://localhost:8081/populatedDB?num_records=10 will insert 10 rows to DB
 *
 */
@RestController
public class DBController {

	//tell spring to inject our DAO class so we will use it to insert rows to DB
	@Autowired
	private DataDao dao;
	
	/**
	 * this all the spring magic happen, the "@RequestMapping" annotations, enables us to map method to URL path
	 * in this case it will map  http://localhost:8081/populatedDB calls to this method
	 * the "@RequestParam" annotation will tell spring to read from the URL the paramter by it's name and set the value to method 
	 * in this case http://localhost:8081/populatedDB?num_records=10 reading the num_records from URL will send to the method the value 10
	 * 
	 * @param num_records
	 * @return
	 */
	@RequestMapping("populatedDB")
	public ResponseEntity<String> getNewTarget(@RequestParam(value = "num_records",required = false,
	                                                    defaultValue = "0") Integer num_records) {
		int num_record_created=0;
		int count=num_records.intValue();
		for(int i=0;i<count;i++){
			BigDataDO dataDo = new BigDataDO("txt"+i,"txt"+i+1,"txt"+i+2);
			try {
				//call the dao to insert data
				dao.insertNewRecord(dataDo);
				num_record_created=i+1;
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		//ResponseEntity it's a spring object that helps us to work easly with web response
		//you can use for example HttpStatus.OK that will set the reponse header to 200 mean all is ok or any other
		//you can also return data (the Model from MVC) to the client, it van be JSON in this case I just retuned simple string 
		 return new ResponseEntity<String>("record created: "+Integer.toString(num_record_created), HttpStatus.OK);
	}
}
