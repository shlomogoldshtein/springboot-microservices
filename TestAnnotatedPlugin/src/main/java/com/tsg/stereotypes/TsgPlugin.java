package com.tsg.stereotypes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
/**
 * Shlomo Goldshtein
 * this is a demo for our custom annotation, the @interface defines it as annotation declaration
 * developers can add it to there classes to make our application 
 * load it on startup as plugin and initiate it
 * the RetentionPolicy.RUNTIME mark it as somthing used on runtime
 * and the ElementType.TYPE_USE say it's a class level annotation (you can define also for methods or parameters)
 * we also added two atrributes, what type of plugin is it, by default it will be a parser
 * and what types of protocls it can parse
 * @see com.tsg.plugins.ParseProtoclA for example how it use
 *
 */
@Retention(RetentionPolicy.RUNTIME )
@Target(ElementType.TYPE_USE )
public @interface TsgPlugin {
	String pluginType() default "parser";
	String protocolType() default "none";
}
