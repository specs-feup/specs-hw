package pt.up.specs.cgra.structure.pes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.specs.cgra.controlDeprecated.PEControl;
import pt.up.specs.cgra.controlDeprecated.PEControlALU;
import pt.up.specs.cgra.controlDeprecated.PEControlNull;
import pt.up.specs.cgra.controlDeprecated.PEControl.PEDirection;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort.PEPortDirection;

public class NullProcessingElement implements ProcessingElement {

	private Mesh myparent;
	private int xPos, yPos;
	private int latency = 1;
	private boolean hasMemory = true;
	private boolean ready = true;
	private boolean executing = false;
	private int executeCount = 0;
	private int memorySize = 1;
	private int writeIdx = 0;
	protected PEControlNull control;

	private List<PEData> registerFile;
	protected List<ProcessingElementPort> ports;

	@Override
	public List<ProcessingElementPort> getPorts() {
		return this.ports;
	}

	public NullProcessingElement(int latency)
	{
		this.latency = latency;
		this.registerFile = new ArrayList<PEData>(1);
		this.control = new PEControlNull();
		this.ports = Arrays.asList(
                new ProcessingElementPort(this, PEPortDirection.input, null),
                new ProcessingElementPort(this, PEPortDirection.input, null),
                new ProcessingElementPort(this, PEPortDirection.output, null));
	}

	@Override
	public boolean setMesh(Mesh myparent) {
		this.myparent = myparent;
		return true;
	}

	@Override
	public Mesh getMesh() {
		return this.myparent;
	}

	public List<PEData> getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(List<PEData> registerFile) {
		this.registerFile = registerFile;
	}

	@Override
	public boolean setX(int x) {
		if (this.xPos != -1)
			return false;
		else {
			this.xPos = x;
			return true;
		}
	}

	@Override
	public boolean setY(int y) {
		if (this.yPos != -1)
			return false;
		else {
			this.yPos = y;
			return true;
		}
	}

	@Override
	public int getX() {
		return this.xPos;
	}

	@Override
	public int getY() {
		return this.yPos;
	}

	@Override
	public boolean isExecuting() {
		return this.executing;
	}


	@Override
	public boolean isReady() {
		return this.ready;
	}

	@Override
	public int getExecuteCount() {
		return this.executeCount;
	}

	protected PEData getOperand(int idx) {
		return this.ports.get(idx).getPayload();

	}

	@Override
	public PEData execute() {

		this.executeCount++;
		this.registerFile.set(0, new PEInteger(0));
		return this.registerFile.get(0);
	}

	@Override
	public String toString() {
		return "NULL";
	}

	public PEControlNull getControl() {
		return control;
	}

	public void setControl(PEControlNull control) {
		this.control = control;
	}


}
