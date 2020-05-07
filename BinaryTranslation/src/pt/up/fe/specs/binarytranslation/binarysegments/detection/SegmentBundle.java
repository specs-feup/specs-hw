package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream.InstructionStreamType;

/**
 * Segment detectors should return a single object of this type. A bundle contains the list of segments, and data about
 * the instruction stream that generated them, including nr instructions, nr cycles, architecture type, etc
 * 
 * @author nuno
 *
 */
public class SegmentBundle implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Date date;
    private String appName;
    private String compilationFlags;
    private List<BinarySegment> segments;

    // stats from the stream
    private long totalCycles;
    private long numInsts;
    private InstructionStreamType itype;

    public SegmentBundle(List<BinarySegment> segments, InstructionStream istream) {

        this.appName = istream.getApplicationName();
        this.compilationFlags = istream.getCompilationInfo();
        this.segments = segments;
        this.date = new Date(System.currentTimeMillis());
        this.totalCycles = istream.getCycles();
        this.numInsts = istream.getNumInstructions();
        this.itype = istream.getType();

        /*
         * For all segments, compute their coverage
         */
        for (BinarySegment seg : this.segments) {

            seg.setStaticCoverage((float) (seg.getLatency() * seg.getContexts().size()) / this.totalCycles);

            if (this.itype == InstructionStreamType.TRACE)
                seg.setDynamicCoverage((float) seg.getExecutionCycles() / this.totalCycles);
        }
    }

    public List<BinarySegment> getSegments() {
        return segments;
    }

    public String getCompilationFlags() {
        return compilationFlags;
    }

    public String getAppName() {
        return appName;
    }

    public Date getDate() {
        return date;
    }

    /*
    public BinarySegment getSegment(Integer addr) {
        
    }*/

    // TODO methods that can compute the coverage and/or acceleration only for a filtered set of this bundle
    // look up what kind of java trickery can be used to do this

    /*
     * For static: Return the percentage of the ELF code the segments detected represent
     * For trace: Return the percentage of the executed instructions the segments detected represent
     * NOTE: only considers segments of size "segmentSize", to avoid overlap
     */
    /*public float getCoverage(int segmentSize) {
    
        Integer detectedportion = 0;
        for (BinarySegment seg : this.segments) {
            if (seg.getSegmentLength() == segmentSize)
                detectedportion += seg.getExecutionCycles();
        }
        return (float) detectedportion / this.totalCycles;
    }*/

    /*
     * Serialize this segment to file (useful for processing only past this point)
     */
    public void serializeToFile(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    public void serializeToFile() throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd").format(this.date);
        this.serializeToFile(this.appName + "_" + date + ".bundle");

        // TODO add cpu architecture to filename
    }

    /*
     * Serialize this segment from file (useful for processing only past this point)
     */
    public static SegmentBundle serializeFromFile(String filename) throws IOException {

        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        SegmentBundle bundle = null;
        try {
            bundle = (SegmentBundle) ois.readObject();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();
        return bundle;
    }
}
