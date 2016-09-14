package com.tsg;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  @author Shlomo Goldshtein
 * this POC investigate the new Stream in java 8 
 * we test how we can process collections as stream , doing map reduce sorting searching and more
 *
 */
public class RunPoc {
	
	/**
	 * demo how to filter list, find all elements starts with C 
	 * change to upper case sort them and print
	 */
	public void basicDemo(){
		List<String> myList =
			    Arrays.asList("a1", "a2", "b1", "c2", "c1");
			//as you can see in java 8 we can get Stream object from collection
			//then we can chain a set of operation on it, and we can do it because 
			//Stream have two type of operations
			//intermediate operations, like on this example filters() map() sorted() this operations 
			//all return Stream object, meaning you can now call another method exposed by Stream
			//the other type of operations are terminal operations, in this example the forEach() is terminal 
			//operation, terminal operations returns void or non Stream results
			myList
			    .stream()
			    .filter(s -> s.startsWith("c"))
			    .map(String::toUpperCase)
			    .sorted()
			    .forEach(System.out::println);	
	}
	/**
	 * this demo different uses of the map , to manipulate the data on stream
	 * notice that stream don't change the values of the original collections
	 * it just manipulate the data on the stream to enable computation
	 */
	public void demoMap(){
		//in this demo you can see we "map" the value in the array to math operation
		//and then we are doing aggregation on the new values calculated
		//you can also see we can create just a stream on the fly with out defining a collection first
		System.out.print("take the numbers, 1,2,3 for each * by 2 and 1, and then add 1, than calculate avarge, the result is: ");
		Arrays.stream(new int[] {1, 2, 3})
	    .map(n -> 2 * n + 1)
	    .average()
	    .ifPresent(System.out::println);
		//another example how we create stream on the fly
		//this time the map will do string manipulation, removing the first char, then we use 
		//mapToInt to convert the string to int stream, than will find the max number
		System.out.print("take the Strings a1,a2,a3 remove the a convert it to int stream and find max number, result is: ");
		Stream.of("a1", "a2", "a3")
	    .map(s -> s.substring(1))
	    .mapToInt(Integer::parseInt)
	    .max()
	    .ifPresent(System.out::println);
	}
	/**
	 * collect is a useful stream termination method, to convert the stream to other type like list or set
	 */
	public void demoCollect(){
		//for this demo will use a list of Objects we defined called Person
		//that contain person name and age, i will add 4 persons to a List
		System.out.println("following demo of collect, is on a List of Object Person with for elements in it: [Max, 18],[Peter, 23],[Pamela, 23],[David, 12]"); 
		List<Person> persons =
			    Arrays.asList(
			        new Person("Max", 18),
			        new Person("Peter", 23),
			        new Person("Pamela", 23),
			        new Person("David", 12));
		
		//collect all that start with p
		List<Person> filtered =
			    persons
			        .stream()
			        .filter(p -> p.name.startsWith("P"))
			        .collect(Collectors.toList());
		System.out.println("demo to collect to new list all people that their name starts with P: "+filtered);
		
		//example how to do sql like group by age 
		System.out.println(" now lets group the list by age");
		Map<Integer, List<Person>> personsByAge = persons.stream()
			    .collect(Collectors.groupingBy(p -> p.age));
			personsByAge
			    .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
		//so why not do aggregation, and find the avarage age
			System.out.print(" we can also use collect to do aggregation, lets do avarage, and the result is: ");
			Double averageAge = persons
				    .stream()
				    .collect(Collectors.averagingInt(p -> p.age));

				System.out.println(averageAge);
		//but I don't really need to do aggregation one by one, i ca use the statistics collector
				System.out.print("i can also use the statistics to collect all aggregation on age: ");
				IntSummaryStatistics ageSummary =
					    persons
					        .stream()
					        .collect(Collectors.summarizingInt(p -> p.age));

					System.out.println(ageSummary);
	}
	/**
	 * the main method of the class
	 * @param args
	 */
	public static void main(String[] args) {
		RunPoc poc= new RunPoc();
		System.out.println("===========================================================");
		System.out.println("first test basic demo, find all alments starting with c, sort them, and print");
		poc.basicDemo();
		System.out.println("===========================================================");
		System.out.println("===========================================================");
		System.out.println("demo the use of map to change or calculate the collection values");
		poc.demoMap();
		System.out.println("===========================================================");
		System.out.println("===========================================================");
		System.out.println("demo the use of collect");
		poc.demoCollect();
		System.out.println("===========================================================");

	}

}
