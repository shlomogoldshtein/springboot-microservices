package com.tsg;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.tsg.dao.DataDao;
import com.tsg.domain.BigDataDO;
import com.tsg.mybatis.mappers.BigDataMapper;
/**
 * @author Shlomo Goldshtein
 * before starting to examine the difrent code, I suggest starting with maven pom.xml
 * and see my different comments on how to set spring boot starters 
 * this application demonstrate few things:
 * 1) using  spring boot with embedded tomcat, including demonstration of application configuration, for example 
 *    server listening port inside application.properties , notice that spring boot look for predefine file names in classpath
 *    in our case every file under src/main/resource  it looks for:
 *    - application.properties for the well the application configuration (it also support .yml extention if you prefer to configure in YAML instead of properties)
 *    - schema.sql if you want to build your DB tables on application start up, you can than set in application.properties the spring.datasource.initialize=false to false so it wont 
 *      create schema again on next restart
 *    - data.sql if you want to insert default data to created tables on startup again like schema play with initialize true or false
 *  I am also demonstrating using "@Configuration" annotation to do annotation based configuration instead of properties file 
 *  @see com.tsg.config.TSGDataSourceConfig
 *  in this class I also demonstarting how to read into java from properties you set on property file, in this case I am reading the database and DB connection pull setting
 *  setting the annotation "@PropertySource("classpath:jdbc-config.properties")" and the Environment class
 * 2) adding REST controller for remote calls
 *   @see com.tsg.controllers.DBController   for full explanation
 * 3) Replacing the spring boot default banner with yours (it may not contribute to app, but its sure fun :-)   ) just by adding to the class path the banner.txt file
 * 4) working with MyBatis as the ORM together with boot, and some samples how to use MyBatis
 *  look on src/main/resources/com/tsg/mybatis/mappers/BigDataMapper.xml and on com/tsg/mybatis/mappers/BigDataMapper.java interface
 *  we also demonstrate how the right way of working with DAO (data access Objects), and how to inject to them the mybatis tappers, using the spring "@Autowired" annotation
 * 5) how to use c3p0 as the DB connection pull instead of boot default tomcat connection pulling
 *   @see com.tsg.config.TSGDataSourceConfig
 * 6) how to work with log4j 2 as the logging system, instead of boot default Logback logger 
 *  look on maven pom.xml file and my comments to see how its done, and on log4j2.xml for examples of configuring  log4j2 for console and for rolling file
 *  look on this class for log4 usage example inside the code
 * 
 *
 *define you main boot class and set the annotation "@SpringBootApplication" on it
 *the implementers CommandLineRunner is not needed, you can added it you want to add a run method
 *and except inputs from users using the command line.
 */
@SpringBootApplication
public class MyBatisApp implements CommandLineRunner{
	//init the log4j logger, you will need to do it in every class
	//from now on don't use the java System.out.println() but logger.<loging level e.g. debug or info>(<message to log>)
	private static final Logger logger = LogManager.getLogger(MyBatisApp.class);
	//use spring "@Autowired" to inject our DAO class that will handle database actions
	@Autowired
	private DataDao dataDo;
	
	/**
	 * inside you main method, is where the boot magic appends, add the 	SpringApplication.run(<your class name>.class, args);
	 * and thats it, you have boot application running, the application will look for 
	 * configuration for example the port to listen on, inside a file named application.properties
	 * or in any class having the "@Configuration" annotation on it 
	 * @param args
	 */
	public static void main(String[] args) {
		//start the spring boot luncher
		SpringApplication.run(MyBatisApp.class, args);
	}
	

	/**
	 * this allow to get inputs using command line, it's not a must just for demo
	 * i use it to call DB action and use logger with out waiting for users input
	 */
	@Override
	public void run(String... args) throws Exception {
		//on start call our DAO to select one record from table with  id =1 
		//this method demo the ORM since it's return a domain (POJO) that reflects the DB table
		BigDataDO bdo=this.dataDo.selectBigDataById(1);
		//print the result using info logging level
		logger.info(bdo);
		//use the new log4j2 place holder {} no need for the  string concatenation "+"
		logger.info("the result for id={}. is:{}",1,bdo);
		
	}
}
