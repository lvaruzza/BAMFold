package bfx.assembly.scaffold;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMSequenceRecord;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class BAMGraphBuilderNeo4j {
	private static Logger log = LoggerFactory.getLogger(BAMGraphBuilderNeo4j.class);
	private DescriptiveStatistics insertStats = new DescriptiveStatistics();
	private double inferedInsertMedian = 0;
	private double inferedInsertIQD = 0;
	private Map<Pair<String,String>,Long> edgeCount;
	
	public GraphDatabaseService buildGraph(String input,String output) throws Exception {
		SAMFileReader reader = new SAMFileReader(new File(input));
		long edges=0;
		edgeCount=new HashMap<Pair<String,String>,Long>();
		SAMFileHeader header = reader.getFileHeader();
		List<SAMSequenceRecord> seqs = header.getSequenceDictionary().getSequences();
		
		log.info("Reading BAM");
		for (SAMRecord aln:reader) {
			if(!aln.getDuplicateReadFlag() && !aln.getReadUnmappedFlag()) {
				//out.println(it);
				if (aln.getMateReferenceName().equals(aln.getReferenceName())) {
					//out.println(aln);
					calculateInsertSize(aln);
				} else {
					countEdge(aln);
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

		GraphDatabaseService graph = buildGraphNode4j(output,edges,seqs);
		return graph;
	}

	private static enum RelTypes implements RelationshipType
	{
	    LINK
	}

	private GraphDatabaseService buildGraphNode4j(String output, long totalEdges,List<SAMSequenceRecord> seqs) throws IOException {
		log.info("Build Graph");
		FileUtils.deleteDirectory(new File(output));
		GraphDatabaseService graph = new GraphDatabaseFactory().newEmbeddedDatabase(output);
		
		Transaction tx = graph.beginTx();
		
		Index<Node> nodeIndex = graph.index().forNodes( "nodes" );
		try {
			for(SAMSequenceRecord seq: seqs) {
					Node node = graph.createNode();
					node.setProperty("sequenceName", seq.getSequenceName());
					nodeIndex.add(node, "sequenceName", seq.getSequenceName());
			}
			//graph.setAutoCreate( true );
			for (Entry<Pair<String,String>,Long> e: edgeCount.entrySet()) {
				if (e.getValue() >= 30) {
					/*System.out.println(String.format("<%s %s %d>",
						e.getKey().fst,
						e.getKey().snd,e.getValue()));*/
					Node left = nodeIndex.get("sequenceName",e.getKey().fst).getSingle();
					Node right = nodeIndex.get("sequenceName",e.getKey().snd).getSingle();
					Relationship edge = left.createRelationshipTo(right, RelTypes.LINK);
					edge.setProperty("count", e.getValue());
					
				}
			}
			tx.success();
		} catch(Exception e) {
			tx.failure();
			throw new RuntimeException(e);
		} finally {
			tx.finish();
		}
		log.info("finished building graph");
		return graph;
	}

	private void countEdge(SAMRecord align) {
		String a=align.getReferenceName();
		String b=align.getMateReferenceName();
		
		int flags=align.getFlags();
	
		Pair<String,String> pair;
		
		if (((flags & 0x10) == 1) && ((flags & 0x20) ==1)) {
			pair=new Pair<String,String>(b,a);
		} else {
			pair=new Pair<String,String>(a,b);
		}
		
		if (edgeCount.containsKey(pair)) {
			edgeCount.put(pair,edgeCount.get(pair)+1);
		} else {
			edgeCount.put(pair,1l);
		}
	}

	private void calculateInsertSize(SAMRecord align) {
		insertStats.addValue(Math.abs(align.getInferredInsertSize()));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BAMGraphBuilderNeo4j gbuilder = new BAMGraphBuilderNeo4j();
		try {
			String dbfile="scaffold.gdb";
			GraphDatabaseService graphdb = gbuilder.buildGraph("data/mates.bam", dbfile);
			log.info("Finish building graph");
			graphdb.shutdown();
			log.info("Saving scaffold.graphml");
			Neo4jGraph g = new Neo4jGraph(dbfile);

			GraphMLWriter writer = new GraphMLWriter(g);
			FileOutputStream out = new FileOutputStream("scaffold.graphml");
			//writer.setNormalize(true);
			writer.outputGraph(out);
			out.close();
			log.info("Finished Saving scaffold.graphml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
