/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.specs.cgra.structure.pes;

import java.util.ArrayList;
import java.util.List;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort.PEPortDirection;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;

public abstract class AProcessingElement implements ProcessingElement {

	// private String peID;
	private Mesh myparent;
	private int xPos, yPos;
	private int latency;
	private boolean hasMemory;
	private boolean ready;
	private boolean executing;
	private int executeCount;
	private int memorySize;
	protected PEControlSetting ctrl;
	// private int writeIdx = 0;


	/*
	 * local memory to hold constants
	 */
	private GenericMemory constants = new GenericMemory(2);// 2 inteiros

	/*
	 * register file (for values computed during operation)
	 */
	private List<PEData> registerFile;

	/*
	 * initialized by children
	 */
	protected List<ProcessingElementPort> ports;


	protected AProcessingElement(int latency, int memorySize) {
		this.latency = latency;
		this.memorySize = memorySize;
		this.registerFile.add(0, null);
		if (this.memorySize > 0) {
			this.hasMemory = true;
		} else {
			this.hasMemory = false;
		}
	}

	protected AProcessingElement(int latency) {
		this(latency, 1);
	}

	protected AProcessingElement() {
		this(1);
	}

	/*
	 * (just for testing???)
	 */
	@Override
	public List<ProcessingElementPort> getPorts() {
		return this.ports;
	}

	@Override
	public String statusString() {
		var strbld = new StringBuilder();
		strbld.append("PE." + this.xPos + "." + this.yPos + "(");
		for (var iPort : this.ports) {
			if (iPort.getDir() == PEPortDirection.input) {
				// PE -> Mesh (parent) -> CGRA (parent) -> Interconnect (child)
				var driver = this.getMesh().getCGRA().getInterconnect().findDriver(iPort);
				if (driver != null)
					strbld.append(iPort.toString() + "<-" + driver.toString());
			}
		}
		return strbld.toString();
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

	@Override
	public List<PEData> getRegisterFile() {
		return registerFile;
	}

	@Override
	public void setRegisterFile(List<PEData> registerFile) {
		this.registerFile = registerFile;
	}

	public void clearRegisterFile() {
		this.registerFile.set(0, null);
	}

	@Override
	public boolean setX(int x) {
		if (this.xPos == -1)
			return false;
		else {
			this.xPos = x;
			return true;
		}
	}

	@Override
	public boolean setY(int y) {
		if (this.yPos == -1)
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
	public int getLatency() {
		return latency;
	}

	@Override
	public boolean hasMemory() {
		return hasMemory;
	}

	@Override
	public int getMemorySize() {
		return memorySize;
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

	/*
	 * Use by children
	 */
	protected abstract PEData _execute();

	
	public PEData getOperand(int idx) {
		return this.ports.get(idx).getPayload();
	}

	@Override
	public PEData execute() {

		var result = _execute();
		this.executeCount++;
		// if (this.writeIdx != -1)
		// this.registerFile.set(this.writeIdx, result)
		this.registerFile.set(0, result);

		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public abstract boolean setControl(int i);
	
	public abstract PEControlSetting getControl();

	/* public void printStatus() {
    		if (this.control == null)
    		{
    			System.out.println("No control associated");
    		}
    		else
    		{
    			System.out.println("PE stuff:");
    			System.out.printf("PE coords: %d, ", this.getX());
    			System.out.printf("%d \n", this.getY());
    			System.out.println("control settings: \n");
    			System.out.printf("Type: %s \n", this.control.getType().name());
    			System.out.printf("OP: %s \n", this.control.getOperation().name());
    			System.out.printf("MemAccess: %s \n", this.control.getMemAccess().name());
    			System.out.printf("Input One: %s \n", this.control.getInputone().name());
    			System.out.printf("Input Two: %s \n", this.control.getInputtwo().name());
    			System.out.println("\n");

    		}
    	}

	 */
}
