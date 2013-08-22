package bfx.assembly.scaffold;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMSequenceRecord;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BAMEdgeReader {
	public static interface EdgeConsumer {
		public void callback(SAMRecord aln,Map<String,Integer> seqs);
	}
	
	private static Logger log = LoggerFactory.getLogger(BAMEdgeReader.class);

	private File input;
	private DescriptiveStatistics insertStats = new DescriptiveStatistics();
	private double inferedInsertMedian = 0;
	private double inferedInsertIQD = 0;

	public BAMEdgeReader(String filename) {
		input = new File(filename);
	}
	

	private void calculateInsertSize(SAMRecord align) {
		insertStats.addValue(Math.abs(align.getInferredInsertSize()));
	}

	private Map<String,Integer> makeSeqMap(List<SAMSequenceRecord> seqs) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(SAMSequenceRecord seq:seqs) {
			map.put(seq.getSequenceName(), seq.getSequenceLength());
		}
		return map;
	}
	
	void read(EdgeConsumer consumer) {
		SAMFileReader reader = new SAMFileReader(input);
		long edges=0;
		SAMFileHeader header = reader.getFileHeader();
		Map<String,Integer> seqs = makeSeqMap(header.getSequenceDictionary().getSequences());

		for (SAMRecord aln:reader) {
			if(!aln.getDuplicateReadFlag() && 
			   !aln.getReadUnmappedFlag()) {
				//out.println(it);
				if (aln.getMateReferenceName().equals(aln.getReferenceName())) {
					//out.println(aln);
					calculateInsertSize(aln);
				} else {
					consumer.callback(aln,seqs);
					edges++;
				}
			}
		}
		reader.close();
		log.info("Finished Reading BAM");
		double q1 = insertStats.getPercentile(25);
		double q3 = insertStats.getPercentile(75);
		inferedInsertMedian = insertStats.getPercentile(50);
		inferedInsertIQD = q3-q1;
		log.info(String.format("insertStats: N=%,d median=%.1f IQD=%.1f",insertStats.getN(), 
				inferedInsertMedian,inferedInsertIQD));
		log.info(String.format("Edges: %,d",edges));
		
	}

}
