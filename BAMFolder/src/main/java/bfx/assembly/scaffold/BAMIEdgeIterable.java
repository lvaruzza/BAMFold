package bfx.assembly.scaffold;

import java.io.File;
import java.util.Iterator;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;

public class BAMIEdgeIterable implements Iterable<SAMRecord> {

	private File input;
	private SAMFileReader reader;
	
	public BAMIEdgeIterable(String filename) {
		this(new File(filename));
	}

	public BAMIEdgeIterable(File input) {
		this.input = input;
		reader = new SAMFileReader(input);
	}
	
	public static class IteratorDecorator implements Iterator<SAMRecord> {
		private Iterator<SAMRecord> inner;
		
		public IteratorDecorator(Iterator<SAMRecord> inner) {
			this.inner = inner;
		}

		@Override
		public boolean hasNext() {
			return inner.hasNext();
		}

		@Override
		public SAMRecord next() {
			return null;
		}

		@Override
		public void remove() {
			throw new RuntimeException("Unimplemented");
		}
	}
	@Override
	public Iterator<SAMRecord> iterator() {
		// TODO Auto-generated method stub
		return new IteratorDecorator(reader.iterator());
	}

}
