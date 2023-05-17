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

import java.util.List;

import pt.up.specs.cgra.CGRAUtils;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEDataNull;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort.PEPortDirection;

public abstract class AProcessingElement implements ProcessingElement {

    // parent related (initialized when PE is placed in Mesh)
    private Mesh myparent;
    private int xPos = -1;
    private int yPos = -1;

    // info
    private final int latency;
    private final boolean hasMemory;
    private final int memorySize;

    // status
    private boolean ready;
    private boolean executing;
    private int executeCount;
    private int writeIdx = 0; // defaults to writing output to 1st element of own register file

    /*
     * local memory to hold constants
     */
    private GenericMemory constants = new GenericMemory(2);// 2 inteiros

    /*
     * register file (for values computed during operation)
     */
    private GenericMemory registerFile;

    /*
     * initialized by children
     */
    protected List<ProcessingElementPort> ports;

    /*
     * Implemented by concrete instances
     */
    protected abstract ProcessingElement getThis();

    private void debug(String str) {
        CGRAUtils.debug(getThis().getClass().getSimpleName(), str);
    }

    protected AProcessingElement(int latency, int memorySize) {
        this.latency = latency;
        this.memorySize = memorySize;
        this.registerFile = new GenericMemory(memorySize);
        this.ready = false;
        this.hasMemory = (this.memorySize > 0) ? true : false;
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

    /*
     * first call helper
     */
    private boolean _getPos() {
        if (this.getMesh() == null) {
            // this.debug("Cannot get position in Mesh since I have no parent.");
            return false; // have no parent
        }

        int x = 0, y = 0;
        for (x = 0; x < this.myparent.getWidth(); x++)
            for (y = 0; y < this.myparent.getHeight(); y++)
                if (this.myparent.getProcessingElement(x, y) == this)
                    break;

        this.xPos = x;
        this.yPos = y;
        return true;
    }

    @Override
    public boolean setMesh(Mesh parent) {
        if (this.getMesh() != null) {
            this.debug("This PE already has a parent! Not changing.");
            return false;
        } else {
            this.myparent = parent;
            this._getPos();
            return true;
        }
    }

    @Override
    public Mesh getMesh() {
        return this.myparent;
    }

    @Override
    public int getX() {
        if (this.xPos == -1)
            this._getPos(); // first call

        return this.xPos;
    }

    @Override
    public int getY() {
        if (this.yPos == -1)
            this._getPos(); // first call

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

    @Override
    public PEData getOperand(int idx) {
        return this.ports.get(idx).getPayload();
    }

    /*
     * Use by children
     */
    protected abstract PEData _execute();

    /*
     * Can be overridden by children
     */
    protected boolean setReady() {
        for (var port : this.ports)
            if (port.getPayload() instanceof PEDataNull) {
                this.debug(this.getAt() + "NOT ready for execution due to NULL payload at port.");
                return false;
            }
        this.debug(this.getAt() + " ready for execution");
        return true;
    }

    @Override
    public PEData execute() {
        this.ready = setReady();
        if (this.ready) {
            var result = _execute();
            this.debug(this.getAt() + " executed successfuly with result:" + result);
            this.executeCount++;
            if (this.hasMemory && this.writeIdx != -1) {
                this.registerFile.write(this.writeIdx, result);
                this.debug("Value stored in register");
            }
            return result;
        } else
            return null;
    }

    @Override
    public final String toString() {
        return this.getType().toString();
    }

    @Override
    public void reset() {
        this.registerFile.reset();
        for (var port : this.getPorts())
            port.setPayload(new PEDataNull());
    }

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
