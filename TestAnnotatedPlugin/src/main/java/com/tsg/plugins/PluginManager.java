package com.tsg.plugins;

import java.util.Hashtable;

import com.tsg.plugins.interfaces.TsgParser;

public class PluginManager {
	Hashtable<String,TsgParser> parsersPlugin = new Hashtable<String,TsgParser>();
	
	public void addParserPlugin(String parserName, TsgParser parser){
		parsersPlugin.put(parserName, parser);
		
	}
	public TsgParser getParserForProtocl(String parserName){
		return parsersPlugin.get(parserName);
	}
}
