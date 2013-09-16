package bfx.technology;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


public abstract class Technology {
	private static Map<String,Technology> map = null;
	
	private static void loadMap() {
		map = new HashMap<String,Technology>();
		ServiceLoader<Technology> sloader = ServiceLoader.load(Technology.class);
		for (Technology tech: sloader) {
			for(String name: tech.getNames()) {
				map.put(name.toLowerCase(), tech);
			}
		}
	}
	public static Technology get(String name) {
		if (map == null) loadMap();
		return map.get(name.toLowerCase());
	}
	
	public abstract boolean samePair(String read1,String read2);
	public abstract boolean validOrientation(boolean forward1,boolean forward2);
	
	public abstract String[] getNames();
	
	public String getName() {
		return getNames()[0];
	}
}
