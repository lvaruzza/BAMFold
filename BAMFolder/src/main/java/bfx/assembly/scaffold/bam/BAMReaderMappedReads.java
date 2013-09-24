package bfx.assembly.scaffold.bam;

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

public class BAMReaderMappedReads {
	private static Logger log = LoggerFactory.getLogger(BAMReaderMappedReads.class);

	private File input;
	private long filtered = 0;
	private long consumed = 0;
	
	public BAMReaderMappedReads(String filename) {
		input = new File(filename);
	}
	

	private Map<String,Integer> makeSeqMap(List<SAMSequenceRecord> seqs) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(SAMSequenceRecord seq:seqs) {
			map.put(seq.getSequenceName(), seq.getSequenceLength());
		}
		return map;
	}
	
	public void read(AlignConsumer consumer) {
		SAMFileReader reader = new SAMFileReader(input);
		SAMFileHeader header = reader.getFileHeader();
		Map<String,Integer> seqs = makeSeqMap(header.getSequenceDictionary().getSequences());
		consumer.setSequences(seqs);
		consumer.start();
		for (SAMRecord aln:reader) {
			if(!aln.getDuplicateReadFlag() && 
			   !aln.getReadUnmappedFlag()) {
				consumer.callback(aln);
				consumed++;
			} else {
				filtered++;
			}
		}
		reader.close();
		log.info("Finished Reading BAM");
		double f = 100.0/(filtered+consumed);
		log.info(String.format("Consumed alignments: %d (%.2f)",consumed,consumed*f));
		log.info(String.format("Filtered alignments: %d (%.2f)",filtered,filtered*f));
		consumer.finish();		
	}

}
