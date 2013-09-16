package bfx.technology;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SOLiD extends Technology {

	private static Pattern nameRegexp = Pattern.compile("(.*?)_(F3|R3|F5|F5-BC)");
	
	@Override
	public boolean samePair(String read1, String read2) {
		Matcher m1 = nameRegexp.matcher(read1);
		Matcher m2 = nameRegexp.matcher(read1);
		
		if (m1.matches() && m2.matches()) {
			if (m1.group(1).equals(m2.group(1))) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new RuntimeException(String.format("Invalid readnames %s %s",read1,read2));
		}
	}

	@Override
	public boolean validOrientation(boolean forward1, boolean forward2) {
		return forward1 ^ forward2;
	}

	@Override
	public String[] getNames() {
		return new String[] {"solid","5500"};
	}

	
}
