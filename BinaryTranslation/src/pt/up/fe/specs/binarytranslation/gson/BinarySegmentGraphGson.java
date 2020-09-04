package pt.up.fe.specs.binarytranslation.gson;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;

/**
 * Equivalent to {@BinarySegmentGraph} object, but all fields are final and "flat", for later emission of JSON file via
 * GSON library
 * 
 * @author nuno
 *
 */
@SuppressWarnings("unused")
public class BinarySegmentGraphGson {

    private final String graphtype;
    private final int numnodes;
    private final int cpl;
    private final int maxwidth;
    private final int numstores, numloads;
    private final int initiationInterval;
    private final float estimatedIPC;
    List<GraphNodeGson> nodes;

    public BinarySegmentGraphGson(BinarySegmentGraph graph) {
        this.graphtype = graph.getType().toString();
        this.numnodes = graph.getNodes().size();
        this.cpl = graph.getCpl();
        this.maxwidth = graph.getMaxwidth();
        this.numstores = graph.getNumStores();
        this.numloads = graph.getNumLoads();
        this.initiationInterval = graph.getInitiationInterval();
        this.estimatedIPC = graph.getEstimatedIPC();

        // TODO: other fields such as contexts etc
    }

    public byte[] getJsonBytes() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this).getBytes();
    }
}
