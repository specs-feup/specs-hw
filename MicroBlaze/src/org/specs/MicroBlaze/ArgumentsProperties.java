/*
 * Copyright 2010 SPeCS Research Group.
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

package org.specs.MicroBlaze;

import static org.specs.MicroBlaze.ArgumentsProperties.ArgumentProperty.*;
import static org.specs.MicroBlaze.MbInstructionName.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.Parsing.MbOperand;
import org.specs.MicroBlaze.Parsing.MbOperand.Flow;

/**
 * Indicates which registers are for read or write, by the order they appear in the instruction, according to the
 * instruction format used in the MicroBlaze Processor Reference Guide v10.3.
 *
 * @author Joao Bispo
 */
public class ArgumentsProperties {

    private static final ArgumentProperty[] writeReadRead = { write, read, read };
    private static final ArgumentProperty[] readRead = { read, read };
    private static final ArgumentProperty[] singleRead = { read };
    private static final ArgumentProperty[] writeRead = { write, read };
    private static final ArgumentProperty[] readReadRead = { read, read, read };

    private static Map<MbInstructionName, Integer> numReadsCache = new EnumMap<>(
	    MbInstructionName.class);
    private static Map<MbInstructionName, Integer> numWritesCache = new EnumMap<>(
	    MbInstructionName.class);

    /**
     * Maps MicroBlaze Instruction Types to arrays with the read/write properties of the instruction arguments.
     *
     * <p>
     * It has an incomplete set of instructions.
     */
    public static final Map<MbInstructionName, ArgumentProperty[]> argumentsProperties;

    static {
	EnumMap<MbInstructionName, ArgumentProperty[]> aMap = new EnumMap<>(
		MbInstructionName.class);
	aMap.put(add, writeReadRead);
	aMap.put(addc, writeReadRead);
	aMap.put(addk, writeReadRead);
	aMap.put(addkc, writeReadRead);
	aMap.put(addi, writeReadRead);
	aMap.put(addic, writeReadRead);
	aMap.put(addik, writeReadRead);
	aMap.put(addikc, writeReadRead);
	aMap.put(and, writeReadRead);
	aMap.put(andi, writeReadRead);
	aMap.put(andn, writeReadRead);
	aMap.put(andni, writeReadRead);

	// --- Only used in traces
	aMap.put(beq, readRead);
	aMap.put(beqd, readRead);
	aMap.put(beqi, readRead);
	aMap.put(beqid, readRead);
	aMap.put(bgei, readRead);
	aMap.put(bgeid, readRead);
	aMap.put(bgti, readRead);
	aMap.put(bgtid, readRead);
	aMap.put(blei, readRead);
	aMap.put(bleid, readRead);
	aMap.put(blti, readRead);
	aMap.put(bltid, readRead);
	aMap.put(bnei, readRead);
	aMap.put(bneid, readRead);

	aMap.put(br, singleRead);
	aMap.put(bra, singleRead);
	aMap.put(brd, singleRead);
	aMap.put(brad, singleRead);
	aMap.put(brld, writeRead);
	aMap.put(brald, writeRead);
	aMap.put(bri, singleRead);
	aMap.put(brai, singleRead);
	aMap.put(brid, singleRead);
	aMap.put(braid, singleRead);
	aMap.put(brlid, writeRead);
	aMap.put(bralid, writeRead);
	aMap.put(brk, writeRead);
	aMap.put(brki, writeRead);
	aMap.put(bsrl, writeReadRead);
	aMap.put(bsra, writeReadRead);
	aMap.put(bsll, writeReadRead);
	aMap.put(bsrli, writeReadRead);
	aMap.put(bsrai, writeReadRead);
	aMap.put(bslli, writeReadRead);
	aMap.put(cmp, writeReadRead);
	aMap.put(cmpu, writeReadRead);

	aMap.put(fadd, writeReadRead);
	aMap.put(frsub, writeReadRead);
	aMap.put(fmul, writeReadRead);
	aMap.put(fint, writeRead);
	aMap.put(flt, writeRead);
	aMap.put(fdiv, writeReadRead);
	aMap.put(fcmp_eq, writeReadRead);
	aMap.put(fcmp_ge, writeReadRead);
	aMap.put(fcmp_gt, writeReadRead);
	aMap.put(fcmp_le, writeReadRead);
	aMap.put(fcmp_lt, writeReadRead);
	aMap.put(fcmp_ne, writeReadRead);
	aMap.put(fcmp_un, writeReadRead);

	aMap.put(idiv, writeReadRead);
	aMap.put(idivu, writeReadRead);
	aMap.put(imm, singleRead);
	aMap.put(lbu, writeReadRead);
	aMap.put(lbui, writeReadRead);
	aMap.put(lhu, writeReadRead);
	aMap.put(lhui, writeReadRead);
	aMap.put(lw, writeReadRead);
	aMap.put(lwr, writeReadRead);
	aMap.put(lwi, writeReadRead);
	aMap.put(mfs, writeRead);
	aMap.put(mts, writeRead);
	aMap.put(mul, writeReadRead);
	aMap.put(mulh, writeReadRead);
	aMap.put(mulhu, writeReadRead);
	aMap.put(mulhsu, writeReadRead);
	aMap.put(muli, writeReadRead);
	aMap.put(or, writeReadRead);
	aMap.put(ori, writeReadRead);
	aMap.put(pcmpbf, writeReadRead);
	aMap.put(pcmpeq, writeReadRead);
	aMap.put(pcmpne, writeReadRead);
	aMap.put(rsub, writeReadRead);
	aMap.put(rsubc, writeReadRead);
	aMap.put(rsubk, writeReadRead);
	aMap.put(rsubkc, writeReadRead);
	aMap.put(rsubi, writeReadRead);
	aMap.put(rsubic, writeReadRead);
	aMap.put(rsubik, writeReadRead);
	aMap.put(rsubikc, writeReadRead);
	aMap.put(rtbd, readRead);
	aMap.put(rted, readRead);
	aMap.put(rtid, readRead);
	aMap.put(rtsd, readRead);
	aMap.put(sb, readReadRead);
	aMap.put(sbi, readReadRead);
	aMap.put(sext16, writeRead);
	aMap.put(sext8, writeRead);
	aMap.put(sh, readReadRead);
	aMap.put(shi, readReadRead);
	aMap.put(sra, writeRead);
	aMap.put(src, writeRead);
	aMap.put(srl, writeRead);
	aMap.put(sw, readReadRead);
	aMap.put(swi, readReadRead);
	aMap.put(swx, readReadRead);
	aMap.put(wdc, readRead);
	aMap.put(wdc_clear, readRead);
	aMap.put(wdc_flush, readRead);
	aMap.put(wic, readRead);
	aMap.put(xor, writeReadRead);
	aMap.put(xori, writeReadRead);

	argumentsProperties = Collections.unmodifiableMap(aMap);
    }

    public static ArgumentProperty[] getProperties(MbInstructionName type) {
	ArgumentProperty[] props = argumentsProperties.get(type);
	if (props == null) {
	    throw new RuntimeException("Arguments properties not defined for instruction '" + type + "'");
	    // LoggingUtils.msgWarn("Instruction '" + type.name() + "' not present in table. Returning " +
	    // "empty array.");
	    // return new ArgumentProperty[0];
	}

	return props;
    }

    public static MbOperand getRd(MbInstructionName instruction, List<MbOperand> operands) {
	ArgumentProperty[] properties = getProperties(instruction);

	if (properties == readRead || properties == singleRead) {
	    throw new RuntimeException("Instruction '" + instruction + "' does not have an rD argument");
	}

	if (properties == writeRead || properties == writeRead || properties == readReadRead) {
	    return operands.get(0);
	}

	throw new RuntimeException("Case not defined: " + properties);
    }

    public static MbOperand getRa(MbInstructionName instruction, List<MbOperand> operands) {
	ArgumentProperty[] properties = getProperties(instruction);

	if (properties == singleRead || properties == writeRead) {
	    throw new RuntimeException("Instruction '" + instruction + "' does not have an rA argument");
	}

	if (properties == readRead) {
	    return operands.get(0);
	}

	if (properties == writeReadRead || properties == readReadRead) {
	    return operands.get(1);
	}

	throw new RuntimeException("Case not defined: " + properties);

    }

    public static MbOperand getRbImm(MbInstructionName instruction, List<MbOperand> operands) {
	ArgumentProperty[] properties = getProperties(instruction);
	/*
		if (OperationProperties.isTypeB(instruction)) {
		    throw new RuntimeException(
			    "Instruction '" + instruction + "' is of type B and does not have an rB argument");
		}
	*/
	if (properties == singleRead) {
	    return operands.get(0);
	}

	if (properties == readRead || properties == writeRead) {
	    return operands.get(1);
	}

	if (properties == writeReadRead || properties == readReadRead) {
	    return operands.get(2);
	}

	throw new RuntimeException("Case not defined: " + properties);
    }

    public static int getNumReads(MbInstructionName instName) {
	return getNum(instName, ArgumentProperty.read);
    }

    public static int getNumWrites(MbInstructionName instName) {
	return getNum(instName, ArgumentProperty.write);
    }

    private static int getNum(MbInstructionName instName, ArgumentProperty prop) {
	Map<MbInstructionName, Integer> cache = getCache(prop);
	Integer result = cache.get(instName);
	if (result != null) {
	    return result;
	}

	ArgumentProperty[] props = getProperties(instName);
	if (props.length == 0) {
	    return -1;
	}

	int counter = 0;
	for (int i = 0; i < props.length; i++) {
	    if (props[i] != prop) {
		continue;
	    }

	    counter++;
	}

	// numReadsCache.put(type, counter);
	cache.put(instName, counter);
	return counter;
    }

    private static Map<MbInstructionName, Integer> getCache(ArgumentProperty argumentProperty) {
	if (argumentProperty == read) {
	    return numReadsCache;
	}

	if (argumentProperty == write) {
	    return numWritesCache;
	}

	System.err.println("Case not defined: '" + argumentProperty + "'");
	return null;
    }

    public enum ArgumentProperty {
	write,
	read;

	/**
	 * Extracts the flow of the given ArgumentProperty. If it could not be determined throws an exception.
	 * 
	 * @param argProp
	 * @return
	 */
	public Flow getFlow() {
	    if (this == ArgumentProperty.read) {
		return MbOperand.Flow.READ;
	    }

	    if (this == ArgumentProperty.write) {
		return MbOperand.Flow.WRITE;
	    }

	    throw new RuntimeException("Case not defined: '" + this + "'");
	    // LoggingUtils.getLogger().warning("Case not defined: '" + argProp + "'");
	    // return null;

	}
    }

}
