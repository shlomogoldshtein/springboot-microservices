package com.tsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;


import org.springframework.beans.factory.config.BeanDefinition;

import com.tsg.plugins.PluginManager;
import com.tsg.plugins.interfaces.TsgParser;
import com.tsg.stereotypes.TsgPlugin;

/**
 * @author Shlomo Goldshtein
 * This boot application demo how to write our own Annotation
 * for the example lets simulate a case I want to supply SDK for my customers. I want to allow them to write plugins
 * to extend our server to parse different types of protocols, all I want them is to be  able to add annotation
 * @TsgPlugin to the class they write, implement an interface with one method called parse and then they add there logic to it
 * after they done, they just need to add the class/jar in the application class path, and thats it
 * on start up, our application will scan all classpath for classes with annotation @TsgPlugin will use java reflaction
 * to create the plugin and store it in our plugin manger as a parser and what type it parse
 * so other classes when needed will just ask the manger give parser for spesific type, and it will return it
 * to do this will define new annotation 
 * @see com.tsg.stereotypes.TsgPlugin
 * it will have 2 properties what type of plugin is it (in the demo = parser) and what protocol it parse (in example = xml)
 * @see com.tsg.plugins.ParseProtoclA for example how it use  on a class
 * @see this class main method how we on application start looking for class with the TsgPlugin annotation
 * and using java reflaction to create them and store them in our plugin manager
 *
 */
@SpringBootApplication
public class RunTest {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		//this is the standard spring boot intiating method
		SpringApplication.run(RunTest.class, args);
		
		//we want on application start to find all classes with annotation @TsgPlugin
		//then we want to use reflection to create them dynamically
		//then will read from the annotation property what type of plugin is it (for the demo we implemented just type parsers)
		//and we want to read from annotation what protocol this parser read, and will store the new plugin 
		//with this information on our PluginManger, so other classes can get the parser when they need to parse XML
		
		//will use spring helper object to find all classes on class path that have annotation of type @TsgPlugin
		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(TsgPlugin.class));
		 //lets create our plugin manager so when will find classes with our plugin annotation will create
		//it and store it
		PluginManager pluginManager = new PluginManager();
		System.out.println("========================================================\n");
		System.out.println("found the folowing classes with annotation @TsgPlugin\n");
		//loop on all classes we found with annotation @TsgPlugin
		//will scan all classes under com.tsg package
		for (BeanDefinition bd : scanner.findCandidateComponents("com.tsg")){
			//print the class name we found
		    System.out.println(bd.getBeanClassName()+"\n");
		    //create java reflaction Class for this class name
		    Class<?>  claz= Class.forName(bd.getBeanClassName());
		    //lets get the annotation information from this class
		    TsgPlugin annotation=claz.getAnnotation( TsgPlugin.class );
		    System.out.println( annotation );
		    //now lets read the annotation values I defined for this plugin
		    String pluginType=annotation.pluginType();
		    String protocolType=annotation.protocolType();
		    System.out.println("this plug is of type: "+pluginType+" and protocl type:"+protocolType+" \n");
		    //now some java reflaction magic, create object from class
		    //for demo will assume I have only plugins of type parser
		   
		    if(pluginType.equalsIgnoreCase("parser")){
		    	TsgParser newInstance = (TsgParser) claz.newInstance();
		    	pluginManager.addParserPlugin(protocolType, newInstance);
		    }//if other type OtherType newInstance = (OtherType) claz.newInstance();
		}
	   System.out.println("========================================================");
	   //thats it we created plugins automatically
	   //.....
	   //now lets assume I have a class that received a data from type XML and need to get the right type of parser
	   TsgParser  pluggedParser=pluginManager.getParserForProtocl("xml");
	   String parsed=pluggedParser.parse("<tag>Hello world</tag>");
	   System.out.println("the plugin parsed the xml and result is: "+parsed+"/n");
	   System.out.println("========================================================");
	   
	}

}
