package com.tsg.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.tsg.domain.BigDataDO;
/**
 * @author Shlomo Goldshtein
 * this is a mybatis mapper, it's an interface and by injecting it to the different class 
 * (but mainly to DAO object, see my comments over there) you can do database operations
 * for each such java mapper, there is an equivelent XML file with the sql itself 
 * you can look for the XML under the src/main/resources and the full package name as this interface
 * -- important the xml file and the interface file must be located under the same folder structure in our case for example com.tsg.mybatis.mappers
 *
 *in the old spring configuration you had to configure the mappers location in the spring XML configuration file
 *in spring boot you can use the "@MapperScan" annotation, mybatis on startup will find all interfaces
 * with this annotation and map them immediately to there XML files
 * 
 * --notice, I in this example prefered to use the XML method to define the queries
 *   mybatis support also defining sql queries in java with annotations like @insert above the methods
 *   but i find it less readable the looking on the SQL as string inside the XML
 */
@MapperScan
public interface BigDataMapper {
	/**
	 * demonstrate calling sql Select since it have filter (where) we expect on row to be returned
	 * where the domain BigDataDO will contain this row data, if it was for example select *
	 * we had to change the method return type  to List<BigDataDO> 
	 * using the @Param enables you not to define parameterType in the xml file
	 * @param id the unique id
	 * @return the domain containing the selected row data
	 */
	BigDataDO selectBigDataById(@Param("id") int id)throws Exception;
	/**
	 * demonstrate insert command where the row to insert is sent as domain object, in this case 
	 * this table have also auto sequence column called id, so you don't need to populate the id field
	 * inside BigDataDO object, after the call to DB is done, mybatis will fill in the domain object
	 * the id field with the value the database generated for it
	 * @param dataDO
	 * @return int  inserts return int, the number of rows inserted
	 * @throws Exception
	 */
	int insertNewRecord(BigDataDO dataDO) throws Exception;
	/**
	 * this demo how to insert several rows at once sending list of domains each one is a row
	 * @param rows
	 * @throws Exception
	 */
	void insertListToBigData(@Param("MyList")List<BigDataDO> rows) throws Exception;
	/**
	 * Demonstrate update command, if you look on the xml, you will see a demo how to do conditional update
	 * meaning you can fill in the domain BigDataDO just one field and keep the rest null
	 * the update will update only columns that the domain fields are not null
	 * @param dataDO
	 * @return
	 * @throws Exception
	 */
	int updateBigData(BigDataDO dataDO)throws Exception;
	/**
	 * Demonstrate sql delete, in this method I didn't used  the @Param so on the xml file i need to
	 * use the parameterType atribute
	 * @param id
	 * @return int the number of rows deleted
	 */
	int deleteFromBigData(int id)throws Exception;
	/**
	 * Demonstrate creating new MySQL table sql command
	 * @throws Exception
	 */
	void createDemoTable()throws Exception;
	/**
	 * Demonstrate injecting all sql to mybatis, for example sqlStr = "update my_table set ...."
	 * notice you need to know what type of sql you inject since delete, update, insert return int
	 * and select return object
	 * @param sqlStr
	 * @return
	 * @throws Exception
	 */
	public int runUpdate(@Param("sqlStr") String sqlStr) throws Exception;
}
