package com.tsg.domain;

/**
 * Domains are java applications Data models, they are POJO (Plain Old Java Objects) and represent a data moving in the system
 * it can be the M from MVC sent to the server REST API, and it could be the O from ORM (Object Relation Mapping) used in our case by mybatis
 * by definition a POJO contain set of fields, and a setters and getters method to retrieve or set the data.
 *  in this case I also added the toString() method overriding the java objects toString() that print the object pointer
 *  to print the objects data instead
 *  another thing I like to to do to POJO is overriding  java Object  hashCode() and  equals(Object obj) methods
 *  this allow me to compare two objects by there data and not by there pointer address
 * what this mean is if two different domains instance contain exactly the same   data
 * you can do something like that 
 * User user1 = new User("shlomo") ;
 * User user2 = new User("shlomo");
 * User if(user1.equals(user2)){.....} 
 * the result will be true
 * and it also mean you can now use it as key when setting 
 * -- small tip the annotation "@Override" is not  mandatory when you override, but it have two important aspect:
 *    1) it makes the coded more readable if someone look on it he knows it came from parent
 *    2) more important if the future the parent will be changed, if you don't have the "@Override the code will continue to compile hence cause error, where with the annotation it will set error on compile time
 *    
 *  -- important - the setter and getter methods must reflect the fields names exactly with the set or get prefix 
 *     else mybatis will not recognize them 
 *  -- important, for fields type always use objects like Integer or Boolean and never primitives like int or boolean
 *     and thats because databases columns can sometime contain null, if mybatis will try to set null to primitive it will cause unexpected results and it's not valid
 *     where for object it perfectly valid result
 * @author Shlomo Goldshtein
 *
 */
public class BigDataDO {


	public Integer id ; 
	public String txt1; 
	public String txt2; 
	public String txt3;
	
	public BigDataDO(){} 
	/**
	 * i added constructor the enable to populate the POJO in one line, it's not a must
	 * but notice if you add such constructor, you must also add empty contractor
	 * else mybatis will fail initiate it lets say when it return result, since it's uses just the setter and getters method
	 * @param txt1
	 * @param txt2
	 * @param txt3
	 */
	public BigDataDO(String txt1, String txt2, String txt3) {
		super();
		this.txt1 = txt1;
		this.txt2 = txt2;
		this.txt3 = txt3;
	}
	/**
	 * getter for the filed id
	 * @return
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * setter for the field id
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 
	 * @return
	 */
	public String getTxt1() {
		return txt1;
	}
	/**
	 * 
	 * @param txt1
	 */
	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}
	/**
	 * 
	 * @return
	 */
	public String getTxt2() {
		return txt2;
	}
	/**
	 * 
	 * @param txt2
	 */
	public void setTxt2(String txt2) {
		this.txt2 = txt2;
	}
	/**
	 * 
	 * @return
	 */
	public String getTxt3() {
		return txt3;
	}
	/**
	 * 
	 * @param txt3
	 */
	public void setTxt3(String txt3) {
		this.txt3 = txt3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((txt1 == null) ? 0 : txt1.hashCode());
		result = prime * result + ((txt2 == null) ? 0 : txt2.hashCode());
		result = prime * result + ((txt3 == null) ? 0 : txt3.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigDataDO other = (BigDataDO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (txt1 == null) {
			if (other.txt1 != null)
				return false;
		} else if (!txt1.equals(other.txt1))
			return false;
		if (txt2 == null) {
			if (other.txt2 != null)
				return false;
		} else if (!txt2.equals(other.txt2))
			return false;
		if (txt3 == null) {
			if (other.txt3 != null)
				return false;
		} else if (!txt3.equals(other.txt3))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BigDataDO [id=" + id + ", txt1=" + txt1 + ", txt2=" + txt2 + ", txt3=" + txt3 + "]";
	}
}
