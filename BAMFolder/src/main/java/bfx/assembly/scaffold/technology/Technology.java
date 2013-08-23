package bfx.assembly.scaffold.technology;


public interface Technology {
	public boolean samePair(String read1,String read2);
	public boolean validOrientation(boolean forward1,boolean forward2);
}
