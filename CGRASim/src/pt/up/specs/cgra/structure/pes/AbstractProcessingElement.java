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

import pt.up.specs.cgra.control.PEControl;
import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort.PEPortDirection;

public abstract class AbstractProcessingElement<T extends PEControlSetting> implements ProcessingElement {

    /*
     * 
     */
    // private String peID;
    private Mesh myparent;
    private int xPos, yPos;
    private int latency = 1;
    private boolean hasMemory = true;
    private boolean ready = true;
    private boolean executing = false;
    private int executeCount = 0;
	private int memorySize = 1;
	
	private T ctrl;
    //private int writeIdx = 0;
   

	/*
     * local memory to hold constants (useful for using 
     * same PE for different contexts, if that PE needs different contexts)
     */
    private GenericMemory constants = new GenericMemory(2);//2 inteiros

    /*
     * register file (for values computed during operation)
     */
    private List<PEData> registerFile;

	/*
     * initialized by children
     */
    protected List<ProcessingElementPort> ports;

    // TODO: each processing element will need a map of operations which can be validly mapped to it
    // so that the scheduler holding the CGRA object can receive success or failure states during
    // scheduling
    // the map should be in the childmost class (maybe?)

    protected AbstractProcessingElement(int latency, int memorySize) {
        this.latency = latency;
        this.memorySize = memorySize;
//        if (this.memorySize > 0) {
//            this.hasMemory = true;
        this.registerFile = new ArrayList<PEData>(3);
//        } else {
//            this.hasMemory = false;
//        }
    }

    protected AbstractProcessingElement(int latency) {
        this(latency, 1);
    }

    protected AbstractProcessingElement() {
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
    
    public List<PEData> getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(List<PEData> registerFile) {
		this.registerFile = registerFile;
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

/*    @Override
    public boolean setResultRegister(int regIndex) {
        if (regIndex < this.memorySize) {
            this.writeIdx = regIndex;
            return true;

        } else {
            return false;
        }
    }
*/
    /*
     * Implemented by children
     */
    protected abstract PEData _execute();

    /*
     * Use by children
     */
    protected PEData getOperand(int idx) {
    	/*if (idx == 0 && this.control.getInputone() == PEDirection.ZERO) return new PEInteger(0);
    	else if (idx == 1 && this.control.getInputtwo() == PEDirection.ZERO) return new PEInteger(0);
    	
    	else */ return this.ports.get(idx).getPayload();
    	
    }

    @Override
    public PEData execute() {

        // TODO: implement latency! requires a counter which sets ready = false and executing = true

        var result = _execute();
        this.executeCount++;
//        if (this.writeIdx != -1)
//            this.registerFile.set(this.writeIdx, result)
        this.registerFile.set(0, result);

        return result;
    }

    /*
     * 
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

	public <T extends PEControlSetting> boolean setControl(T ctrl) {
		this.ctrl = (T) ctrl;
		return true;
	}

/*	
	public void printStatus() {
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
