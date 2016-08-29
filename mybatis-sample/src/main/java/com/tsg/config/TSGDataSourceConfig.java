package com.tsg.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * since I don't want to use boot default DB connection pulling (tomcat connection pulling) 
 * but the third party c3p0, I will not use the boot default configuration in application.properties
 * but will write configuration class for c3p0, with my properties, and the admin can set later the values of the them
 * in the properties file
 * spring boot allow you to add configuration class by just adding the "@Configuration" annotation
 * on start up spring boot will scan all classes with this annotation and read the configuration
 * I also demonstrate how to read values from property file into java class, in this case
 * I created a file called jdbc-config.properties where the user can set the Database connection parameters
 * and the database connection pull configuration
 * I will use them to create c3p0 data source, this annotation tells spring Environment object to which property file to read from
 * @author Shlomo Goldshtein
 *
 */
@Configuration
@PropertySource("classpath:jdbc-config.properties")
public class TSGDataSourceConfig {
	//tell spring to Inject an instance of it's Environment bean, a utility that makes reading 
	//propery file easy
    @Autowired
    private Environment env;
    

	    @Bean
	    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	        return new PropertySourcesPlaceholderConfigurer();
	    } 
	    /**
	     * we overriding spring boot defualt tomcat jdbc connection pull and returning 
	     * our preference of of using c3P0
	     * the dataSource bean in spring helps us decouple the logic of the source of the data source
	     * from the business logic we can create which dataSource bean that we want, and spring will inject it to whom need it
	     * in our case to mybatis
	     * @return
	     * @throws PropertyVetoException
	     */
	    @Bean
	    public ComboPooledDataSource dataSource() throws PropertyVetoException {
	    	//read from jdbc-config.properties file the values
		    int maxSize =new Integer(this.env.getProperty("tsg.datasource.c3p0.max_size")).intValue();
			 //connection pull min size
			    int minSize =new Integer(this.env.getProperty("tsg.datasource.c3p0.min_size")).intValue();
			  //connection pull how many connections to open at each time
			    int acquireIncrement =new Integer(this.env.getProperty("tsg.datasource.c3p0.acquire_increment")).intValue();
			  //connection pull how often to check connections ok
			    int idleTestPeriod=new Integer(this.env.getProperty("tsg.datasource.c3p0.idle_test_period")).intValue();
			    //connection pull max statments to open on start
			    int maxStatements=new Integer(this.env.getProperty("tsg.datasource.c3p0.max_statements")).intValue();
			  //connection pull how long to keep connection that are in idel state
			    int maxIdleTime=new Integer(this.env.getProperty("tsg.datasource.c3p0.max_idle_time")).intValue();
			 //data base url
			    String url=this.env.getProperty("tsg.datasource.c3p0.url");
			 //database username
			    String username=this.env.getProperty("tsg.datasource.c3p0.username");
			 //database password
			    String password=this.env.getProperty("tsg.datasource.c3p0.password");
			 //database driver class
			    String driverClassName=this.env.getProperty("tsg.datasource.c3p0.driverClassName");
			    //create the c3p0 data source bean, and thats it
	        ComboPooledDataSource dataSource = new ComboPooledDataSource();
	        dataSource.setMaxPoolSize(maxSize);
	        dataSource.setMinPoolSize(minSize);
	        dataSource.setAcquireIncrement(acquireIncrement);
	        dataSource.setIdleConnectionTestPeriod(idleTestPeriod);
	        dataSource.setMaxStatements(maxStatements);
	        dataSource.setMaxIdleTime(maxIdleTime);
	        dataSource.setJdbcUrl(url);
	        dataSource.setPassword(password);
	        dataSource.setUser(username);
	        dataSource.setDriverClass(driverClassName);
	        return dataSource;
	    }
	    /**
	     * this is not needed since spring boot automatically will read any file called schemma.sql and run it on start up to intilize the database
	     * I added just as example that you overide all
	     * @param dataSource
	     * @return
	     */
	    @Bean
	    public DataSourceInitializer dataSourceInitializer(DataSource dataSource)
	    {
	    	String schemInitfile=this.env.getProperty("tsg.datasource.schema");
	    	String initDatabase=this.env.getProperty("tsg.datasource.initializ");
	        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();    
	        dataSourceInitializer.setDataSource(dataSource);
	        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
	        databasePopulator.addScript(new ClassPathResource(schemInitfile));
	        dataSourceInitializer.setDatabasePopulator(databasePopulator);
	        dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
	        return dataSourceInitializer;
	    }
}
