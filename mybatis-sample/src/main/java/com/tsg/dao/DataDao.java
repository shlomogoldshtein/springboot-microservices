package com.tsg.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tsg.domain.BigDataDO;
import com.tsg.mybatis.mappers.BigDataMapper;


/**
 * this is a DAO (Data Accesses Object) , inject this class using spring "@Autowired" annotation
 * to every class in the database that needs to work in DB, and expose methods to do different operations
 * the one that actually doing the DB work is the mybatis interface, as you can see we injected the BigDataMapper to this class
 * some may claim that the mybatis interfaces, are already dao and you can inject them to the different classes
 * but I tend to add the DAO objects in between, because some time you want to add more logic
 * for example convert JSON to the domain object, or reciving several domains and call two different mappers 
 * or handling exception (for example if insert of list fail, start running row by row and insert separately)
 * the DAO allow to separate the logic of DB processing completly from the other business logic
 * as you can see I added to the class the annotation @Repository this is just one of the annotations like @Component or @Service
 * that are scanned automatically by spring, and telling it it's a Bean
 * it means spring will initiate it on application start, and can inject it to other beans, and by default it's singleton
 * 
 * @author Shlomo Goldshtein
 *
 */
@Repository
public class DataDao {
	/**
	 * inject mybatis mapper
	 */
	@Autowired
	private BigDataMapper bigDataMapper;
	
	/**
	 * call mybatis select one record by the given id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BigDataDO selectBigDataById(int id) throws Exception{
		return this.bigDataMapper.selectBigDataById(id);
	}
	/**
	 * call mybatis to insert one row
	 * @param dataDO
	 * @return
	 * @throws Exception
	 */
	public int insertNewRecord(BigDataDO dataDO) throws Exception{
		return this.bigDataMapper.insertNewRecord(dataDO);
	}

}
