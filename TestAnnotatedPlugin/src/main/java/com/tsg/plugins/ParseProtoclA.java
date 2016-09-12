package com.tsg.plugins;

import com.tsg.plugins.interfaces.TsgParser;
import com.tsg.plugins.interfaces.TsgPluginIntr;
import com.tsg.stereotypes.TsgPlugin;

@TsgPlugin(pluginType="parser", protocolType="xml")
public class ParseProtoclA  implements TsgParser,TsgPluginIntr{

	public String parse(String source){
		return source;
	}
}
