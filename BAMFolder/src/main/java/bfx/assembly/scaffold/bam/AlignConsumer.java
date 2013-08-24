package bfx.assembly.scaffold.bam;

import java.util.Map;

import net.sf.samtools.SAMRecord;

public abstract class AlignConsumer {
	protected Map<String, Integer> seqs;

	public abstract void callback(SAMRecord aln);
	
	public void setSequences(Map<String, Integer> seqs) {
		this.seqs = seqs;
	}
	
	public abstract void start();
	public abstract void finish();
}