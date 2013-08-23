package bfx.assembly.scaffold.edges;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.samtools.SAMRecord;
import bfx.assembly.scaffold.bam.BAMReader;
import bfx.assembly.scaffold.technology.IonTorrentTechnology;
import bfx.assembly.scaffold.technology.Technology;

public class MergePairs extends BAMReader.AlignConsumer {
	private static Logger log = LoggerFactory.getLogger(BAMReader.class);
	
	public static interface EdgeConsumer {
		public void callback(GraphEdge edge);
	}

	private Technology tech;
	private EdgeConsumer consumer;
	
	private SAMRecord left = null;
	private int unpaired = 0;
	private int improperPair = 0;
	private int qualCutoff = 1;
	private int qualFiltered = 0;
	
	public MergePairs(Technology technology,int qualCutoff) {
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
					
					if(tech.validOrientation(leftOrientation, rightOrientation)) {
						consumer.callback(new GraphEdge(
								left.getReferenceName(),
								right.getReferenceName(),
								left.getAlignmentStart(),
								right.getAlignmentStart(),
								MQ,
								leftOrientation
								));
					} else {
						improperPair++;
					}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		log.info(String.format("Qual Filtered %d",qualFiltered));
		log.info(String.format("Improper Pair %d",this.improperPair));
		log.info(String.format("Unpaired %d",this.unpaired));
	}

	public void setConsumer(EdgeConsumer consumer) {
		this.consumer = consumer;
	}


}
