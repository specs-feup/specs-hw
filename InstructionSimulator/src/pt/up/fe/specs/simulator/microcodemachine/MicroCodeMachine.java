/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.simulator.microcodemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.simulator.Addr;
import pt.up.fe.specs.simulator.SimInstruction;
import pt.up.fe.specs.simulator.impl.AMachine;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsLogs;

public class MicroCodeMachine extends AMachine {

    private final Function<Number, Addr> toAddr;
    private final Addr pcRegister;
    private final Alu alu;
    private final MicroCodeDecoder decoder;

    private Map<Addr, Instruction> program;
    private Map<Addr, MicroCodeInstruction> decodedInstructions;
    private TemporaryStorage temporaryStorage;

    private Addr currentAddr;
    private Addr nextAddr;

    // Status of delay slot:
    // > 0 - Currently in a delay slot queue
    private int currentDelaySlot;

    // If set, next PC will jump to this value
    private Addr jump;

    // Preloaded bits for immediate value
    // private Number preloadedImm;
    // private Number imm;

    public MicroCodeMachine(Function<Number, Addr> toAddr, Number pcRegister, Alu alu, MicroCodeDecoder decoder) {
        this.toAddr = toAddr;
        this.pcRegister = toAddr.apply(pcRegister);
        this.alu = alu;
        this.decoder = decoder;

        program = new HashMap<>();
        decodedInstructions = new HashMap<>();
        temporaryStorage = new TemporaryStorage();

        // No current instruction yet
        currentAddr = null;

        // By default, start address is 0
        nextAddr = toAddr(0);

        this.currentDelaySlot = 0;
        // this.delaySlotJump = null;
        this.jump = null;

        // this.preloadedImm = null;
        // this.imm = null;
    }

    public TemporaryStorage getTemp() {
        return temporaryStorage;
    }

    public void setDelay(int delaySlotSize) {
        this.currentDelaySlot = delaySlotSize;
    }

    public void setJump(Number jumpAddr) {
        setJump(toAddr(jumpAddr));
    }

    public void setJump(Addr jumpAddr) {
        this.jump = jumpAddr;
    }

    public void loadProgram(Map<Addr, Instruction> program) {
        reset();
        this.program = program;
    }

    public void run() {
        run(0);
    }

    public void run(Number startAddress) {
        // int counter = 0;
        // int max = 10_000_000;
        // Set address
        setStartAddress(startAddress);

        while (!hasStopped()) {
            // while (true) {
            var instruction = nextInstruction();
            // System.out.println("EXEC: " + instruction.getAddress());
            instruction.execute();

            // if (counter > max) {
            // System.out.println("BREAKING!");
            // break;
            // }
            //
            // counter++;
        }

    }

    public void reset() {
        decodedInstructions = new HashMap<>();
        temporaryStorage = new TemporaryStorage();

        // No current instruction yet
        currentAddr = null;

        // By default, start address is 0
        nextAddr = toAddr(0);

        this.currentDelaySlot = 0;
        // this.delaySlotJump = null;
        this.jump = null;
    }

    public Alu getAlu() {
        return alu;
    }

    // public void setPreloadedImm(Number preloadedImm) {
    // this.preloadedImm = preloadedImm;
    // }

    // public void setImm(Number imm) {
    // this.imm = imm;
    // }

    // public Number getImm() {
    // return imm;
    // }

    // public Number getPreloadedImm() {
    // return preloadedImm;
    // }

    /**
     * @return true if current instruction is the same as the next instruction, false otherwise
     */
    @Override
    public boolean hasStopped() {
        if (currentAddr == null) {
            return false;
        }

        return currentAddr.equals(nextAddr);
    }

    @Override
    public SimInstruction nextInstruction() {
        if (nextAddr == null) {
            throw new RuntimeException(
                    "Not address set for next instruction. Make sure previous instruction has executed");
        }

        // Update addresses
        currentAddr = nextAddr;
        nextAddr = null;

        // Get instruction
        return getInstruction(currentAddr);
    }

    private SimInstruction getInstruction(Addr address) {
        // Check if instruction has been decoded
        var inst = decodedInstructions.get(address);

        if (inst != null) {
            return inst;
        }

        // Try to decode instruction
        var rawInstruction = program.get(address);

        // System.out.println("INSTRUC: " + instructions);
        SpecsCheck.checkNotNull(rawInstruction,
                () -> "No program instruction found at address " + address.toNumber() + " (" +
                        address + ")");

        var microCodeInstruction = decoder.decode(rawInstruction, this);
        decodedInstructions.put(address, microCodeInstruction);

        return microCodeInstruction;
    }

    public void addInstruction(MicroCodeInstruction instruction) {
        var previousValue = decodedInstructions.put(instruction.getAddress(), instruction);

        if (previousValue != null) {
            SpecsLogs.info("Overwriting instruction at address '" + instruction.getAddress() + "'\nPrevious value: '"
                    + previousValue + "'\nNew value: '" + instruction + "'");
        }
    }

    @Override
    public void setStartAddress(Number startAddress) {
        this.currentAddr = null;
        this.nextAddr = toAddr(startAddress);
    }

    @Override
    public Addr toAddr(Number addr) {
        return toAddr.apply(addr);
    }

    public Addr updatePC() {
        nextAddr = nextPC();
        // var nextPC = nextPC();
        getRegisters().write(pcRegister, nextAddr.toNumber());
        return nextAddr;
    }

    private Addr nextPC() {

        // If in delay slot, just execute next instruction
        if (inDelaySlotQueue()) {
            // Next address
            return currentAddr.inc();
        }

        // If there is a jump scheduled, return it
        if (jump != null) {
            var jumpAddr = jump;
            jump = null;
            return jumpAddr;
        }

        // Next address
        return currentAddr.inc();
    }

    /**
     * 
     * @return true if currently in a delay queue
     */
    private boolean inDelaySlotQueue() {
        boolean inDelayQueue = currentDelaySlot > 0;

        if (inDelayQueue) {
            currentDelaySlot--;
        }

        return inDelayQueue;
    }
}
