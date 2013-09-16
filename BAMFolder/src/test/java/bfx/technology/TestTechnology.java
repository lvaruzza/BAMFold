package bfx.technology;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestTechnology {

	
	@Test
	public void testGet() {
		Technology solid = Technology.get("solid");
		assertNotNull(solid);
	}
	
	@Test
	public void testSOLiD() {
		String f3="520_1838_903_F3";
		String r3="499_1693_895_R3";
		Technology solid = Technology.get("solid");
		
		assertTrue(solid.samePair(f3, r3));
	}
	
}
