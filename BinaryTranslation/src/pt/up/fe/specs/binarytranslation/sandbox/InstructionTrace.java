package pt.up.fe.specs.binarytranslation.sandbox;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.suikasoft.XStreamPlus.XStreamUtils;

public class InstructionTrace {

	/////////////// data to serialize
	private class InstructionTraceData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5668842309768632051L;
		private String cpuName;
		private int numInsts;
		// private List<Instruction> insts;
		private String tracefilename;

		// private String processorname?
		// private enum ProcessorType?
		// private enum ISA?
		// private String GeneratedBy?
		// private List<String> compile flags of elf?
		// private String rundate?
		// private String runlength?
		// private String runlength?
	}
	/////////////////////////////////

	private String fileName;
	private InstructionTraceData ITD;
	private int currline;

	/*
	 * Constructor used when reading existing trace from file
	 */
	public InstructionTrace(String fileName) {
		this.fileName = fileName;
		this.currline = 0;
		this.ITD = XStreamUtils.read(new File(this.fileName), InstructionTraceData.class);
	}

	/*
	 * Constructor used when creating a new InstructionTrace from simulator
	 */
	public InstructionTrace() {
		this.ITD.cpuName = "";
		this.ITD.numInsts = 0;
		this.currline = 0;
		// this.ITD.insts = new ArrayList<Instruction>();
		// this.fileName - generate from constructor argument.. possibly siminfo?
	}

	/*
	 * Serialize InstructionTraceData into file
	 */
	public void toFileSerial() throws IOException {
		XStreamUtils.write(new File(this.fileName), this);
		// Serializer.toFile(this, this.fileName);
	}

	/*
	 * Print InstructionTraceData to TXT file
	 */
	public void tofileTxt() {

	}

	// public

	public String getFilename() {
		return this.fileName;
	}
}
