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
 
package pt.up.fe.specs.binarytranslation.elf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.StaticInstructionProducer;

/**
 * Map holding a dump of an ELF; unique addresses are assumed
 * 
 * @author nuno
 *
 */
public abstract class ELFDump {

    private Application appdata;

    // TODO: map should also hold section of instruction
    // since addresses repeat per individual section of linker script
    private final Map<Long, Instruction> elfdump = new HashMap<Long, Instruction>();

    public ELFDump(StaticInstructionProducer producer) {

        this.appdata = producer.getApp();

        try {
            Instruction i = null;
            while ((i = producer.nextInstruction()) != null) {
                var addr = i.getAddress().longValue();
                if (elfdump.containsKey(addr))
                    throw new Exception();

                elfdump.put(i.getAddress().longValue(), i);
            }

            // TODO: do something better
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Instruction getInstruction(Long addr) {
        return elfdump.get(addr/*.longValue()*/);
    }

    /*
     * Get a sequential range of instructions between two addresses
     */
    public List<Instruction> getRange(Long startaddr, Long endaddr) {
        var list = new ArrayList<Instruction>();
        for (Long i = startaddr; i < endaddr; i += appdata.getInstructionWidth()) {
            list.add(this.elfdump.get(i));
        }
        return list;
    }

    public Map<Long, Instruction> getElfdump() {
        return elfdump;
    }
}
