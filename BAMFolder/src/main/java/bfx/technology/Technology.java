package bfx.technology;


public abstract class Technology {
	public static Technology get(String name) {
		return null;
	}
	
	public abstract boolean samePair(String read1,String read2);
	public abstract boolean validOrientation(boolean forward1,boolean forward2);
	
	public abstract String[] getNames();
	
	public String getName() {
		return getNames()[0];
	}
}
