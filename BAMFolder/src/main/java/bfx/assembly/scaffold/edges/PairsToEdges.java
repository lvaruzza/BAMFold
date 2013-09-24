package bfx.assembly.scaffold.edges;

import net.sf.samtools.SAMRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bfx.assembly.scaffold.bam.AlignConsumer;
import bfx.assembly.scaffold.bam.BAMReaderMappedReads;
import bfx.technology.Technology;

public class PairsToEdges extends AlignConsumer {
	private static Logger log = LoggerFactory.getLogger(BAMReaderMappedReads.class);
	
	private Technology tech;
	private EdgeConsumer consumer;
	
	private SAMRecord left = null;
	private int unpaired = 0;
	private int improperPair = 0;
	private int qualCutoff = 1;
	private int qualFiltered = 0;
	
	public PairsToEdges(Technology technology,int qualCutoff) {
		this.tech = technology;
		this.qualCutoff = qualCutoff;
	}

	@Override
	public void callback(SAMRecord aln) {
		if (left == null)
			left = aln;
		else {
			if (tech.samePair(aln.getReadName(), left.getReadName())) {
				SAMRecord right = aln;
				if (left.getMappingQuality() < qualCutoff || right.getMappingQuality() < qualCutoff) {
					qualFiltered++;
				} else {
					boolean leftOrientation = !left.getReadNegativeStrandFlag();
					boolean rightOrientation = !right.getReadNegativeStrandFlag();
					int MQ = left.getMappingQuality() + right.getMappingQuality();
						consumer.callback(new AlignEdge(
								left.getReferenceName(),
								right.getReferenceName(),
								left.getAlignmentStart(),
								left.getAlignmentEnd(),
								right.getAlignmentStart(),
								right.getAlignmentEnd(),
								MQ,
								leftOrientation,
								rightOrientation
								));
				}
			} else {
				left = aln;
				unpaired++;
			}
			left = null;
		}
	}

	@Override
	public void start() {
		consumer.setSequences(seqs);
		consumer.start();
	}

	@Override
	public void finish() {
		log.info(String.format("Qual Filtered %d",qualFiltered));
		log.info(String.format("Improper Pair %d",this.improperPair));
		log.info(String.format("Unpaired %d",this.unpaired));
		consumer.finish();
	}

	public void setConsumer(EdgeConsumer consumer) {
		this.consumer = consumer;
	}


}
