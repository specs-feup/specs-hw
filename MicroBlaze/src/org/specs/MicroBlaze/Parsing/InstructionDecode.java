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
package org.specs.MicroBlaze.Parsing;

import static org.specs.MicroBlaze.Parsing.MbParserUtils.*;

import org.specs.MicroBlaze.MbInstructionName;

/**
 * Decodes a MicroBlaze instruction from its binary representation into a String representation.
 *
 * @author Joao Bispo
 */
public class InstructionDecode {

    public static int signExtended(int number) {
	if ((number & 0x8000) == 0x8000) {
	    return number | 0xffff0000;
	}
	return number;
    }

    public static String nameSpecial(int code) {
	switch (code) {
	case 0x0000:
	    return "rpc";
	case 0x0001:
	    return "rmsr";
	case 0x0003:
	    return "rear";
	case 0x0005:
	    return "resr";
	case 0x0007:
	    return "rfsr";
	case 0x000b:
	    return "rbtr";
	case 0x2000:
	    return "rpvr0";
	case 0x2001:
	    return "rpvr1";
	case 0x2002:
	    return "rpvr2";
	case 0x2003:
	    return "rpvr3";
	case 0x2004:
	    return "rpvr4";
	case 0x2005:
	    return "rpvr5";
	case 0x2006:
	    return "rpvr6";
	case 0x2007:
	    return "rpvr7";
	case 0x2008:
	    return "rpvr8";
	case 0x2009:
	    return "rpvr9";
	case 0x200a:
	    return "rpvr10";
	case 0x200b:
	    return "rpvr11";
	default:
	    return "undefined";
	}
    }

    public static String toString(int value) {
	int opcode;
	int rD;
	int rA;
	int rB;
	int imm;
	int imm_5;
	int imm_14;
	int type_a;
	int type_b;
	int type_b_2;
	int type_b_11;
	int type_b_13;
	int rS;
	int FSLx;

	opcode = (value >> 26) & 0x3f;
	rD = (value >> 21) & 0x1f;
	rA = (value >> 16) & 0x1f;
	switch (opcode) {
	// 0b000000 add
	case 0x00:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		if (rD == 0x00 && rA == 0x00 && rB == 0x00) {
		    return "";
		}
		return MbInstructionName.add + "     " + REGISTER_PREFIX + rD + REGISTER_SEPARATOR + REGISTER_PREFIX
			+ rA + REGISTER_SEPARATOR + REGISTER_PREFIX + rB;
	    }
	    return "";
	// 0b000001 rsub
	case 0x01:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return MbInstructionName.rsub + "    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", "
			+ REGISTER_PREFIX + rB;
	    }
	    return "";
	// 0b000010 add (addc)
	case 0x02:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return MbInstructionName.addc + "    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", "
			+ REGISTER_PREFIX + rB;
	    }
	    return "";
	// 0b000011 rsub (rsubc)
	case 0x03:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return MbInstructionName.rsubc + "   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", "
			+ REGISTER_PREFIX + rB;
	    }
	    return "";
	// 0b000100 add (addk)
	case 0x04:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return MbInstructionName.addk + "    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", "
			+ REGISTER_PREFIX + rB;
	    }
	    return "";
	// 0b000101 rsub cmp
	case 0x05:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 rsubk
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "rsubk   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00000000001 cmp
	    case 0x001:
		rB = (value >> 11) & 0x1f;
		return "cmp     " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00000000011 cmpu
	    case 0x003:
		rB = (value >> 11) & 0x1f;
		return "cmpu    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    default:
		return "";
	    }

	    // 0b000110 add (addkc)
	case 0x06:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "addkc   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    }
	    return "";
	// 0b000111 rsub (rsubkc)
	case 0x07:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "rsubkc    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    }
	    return "";
	// 0b001000 addi
	case 0x08:
	    imm = value & 0xffff;
	    return "addi    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b001001 rsubi
	case 0x09:
	    imm = value & 0xffff;
	    return "rsubi   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b001010 addic
	case 0x0a:
	    imm = value & 0xffff;
	    return "addic   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b001011 rsubi (rsubic)
	case 0x0b:
	    imm = value & 0xffff;
	    return "rsubic  " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b001100 addik
	case 0x0c:
	    imm = value & 0xffff;
	    return "addik   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b001101 rsubi (rsubik)
	case 0x0d:
	    imm = value & 0xffff;
	    return "rsubik  " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b001110 addikc
	case 0x0e:
	    imm = value & 0xffff;
	    return "addikc  " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b001111 rsubi (rsubikc)
	case 0x0f:
	    imm = value & 0xffff;
	    return "rsubikc " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + signExtended(imm);
	// 0b010000 mul mulh mulhu
	case 0x10:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 mul
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "mul     " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00000000001 mulh
	    case 0x001:
		rB = (value >> 11) & 0x1f;
		return "mulh    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00000000011 mulhu
	    case 0x003:
		rB = (value >> 11) & 0x1f;
		return "mulhu   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    default:
		return "";
	    }

	    // 0b010001 bs
	case 0x11:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 bsrl
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "bsrl    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01000000000 bsra
	    case 0x200:
		rB = (value >> 11) & 0x1f;
		return "bsra    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b10000000000 bsll
	    case 0x400:
		rB = (value >> 11) & 0x1f;
		return "bsll    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    default:
		return "";
	    }

	    // 0b010010 idiv
	case 0x12:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 idiv
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "idiv    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00000000010 idivu
	    case 0x002:
		rB = (value >> 11) & 0x1f;
		return "idivu   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    default:
		return "";
	    }

	    // 0b010110 fadd frsub fmul fdiv fcmp
	case 0x16:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 fadd
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "fadd    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00010000000 frsub
	    case 0x080:
		rB = (value >> 11) & 0x1f;
		return "frsub   " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00100000000 fmul
	    case 0x100:
		rB = (value >> 11) & 0x1f;
		return "fmul    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b00110000000 fdiv
	    case 0x180:
		rB = (value >> 11) & 0x1f;
		return "fdiv    " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01000000000 fcmp.un
	    case 0x200:
		rB = (value >> 11) & 0x1f;
		return "fcmp.un " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01000010000 fcmp.lt
	    case 0x210:
		rB = (value >> 11) & 0x1f;
		return "fcmp.lt " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01000100000 fcmp.eq
	    case 0x220:
		rB = (value >> 11) & 0x1f;
		return "fcmp.eq " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01000110000 fcmp.le
	    case 0x230:
		rB = (value >> 11) & 0x1f;
		return "fcmp.le " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01001000000 fcmp.gt
	    case 0x240:
		rB = (value >> 11) & 0x1f;
		return "fcmp.gt " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01001010000 fcmp.ne
	    case 0x250:
		rB = (value >> 11) & 0x1f;
		return "fcmp.ne " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    // 0b01001100000 fcmp.ge
	    case 0x260:
		rB = (value >> 11) & 0x1f;
		return "fcmp.ge " + REGISTER_PREFIX + rD + ", " + REGISTER_PREFIX + rA + ", " + REGISTER_PREFIX + rB;
	    default:
		return "";
	    }

	    // 0b011000 muli
	case 0x18:
	    imm = value & 0xffff;
	    return "muli    r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b011001 bsi
	case 0x19:
	    rB = (value >> 11) & 0x1f;
	    if (rB == 0x00) {
		type_b_11 = (value >> 5) & 0x3f;
		switch (type_b_11) {
		// 0b000000 bsrli
		case 0x00:
		    // imm_5 = value & 0xffff;
		    imm_5 = value & 0x1f;
		    return "bsrli   r" + rD + ", r" + rA + ", " + imm_5;
		// 0b010000 bsrai
		case 0x10:
		    // imm_5 = value & 0xffff;
		    imm_5 = value & 0x1f;
		    return "bsrai   r" + rD + ", r" + rA + ", " + imm_5;
		// 0b100000 bslli
		case 0x20:
		    // imm_5 = value & 0xffff;
		    imm_5 = value & 0x1f;
		    return "bslli   r" + rD + ", r" + rA + ", " + imm_5;
		default:
		    return "";
		}
	    }

	    return "";
	// 0b011011 get put
	case 0x1b:
	    type_b_13 = (value >> 3) & 0x1fff;
	    switch (type_b_13) {
	    // 0b0000000000000 get
	    case 0x0000:
		if (rD == 0x00) {
		    FSLx = value & 0x00000007;
		    return "get     r" + rD + ", " + FSLx;
		}
		return "";
	    // 0b0010000000000 cget
	    case 0x0400:
		if (rD == 0x00) {
		    FSLx = value & 0x00000007;
		    return "cget    r" + rD + ", " + FSLx;
		}
		return "";
	    // 0b0100000000000 nget
	    case 0x0800:
		if (rD == 0x00) {
		    FSLx = value & 0x00000007;
		    return "nget    r" + rD + ", " + FSLx;
		}
		return "";
	    // 0b0110000000000 ncget
	    case 0x0c00:
		if (rD == 0x00) {
		    FSLx = value & 0x00000007;
		    return "ncget   r" + rD + ", " + FSLx;
		}
		return "";
	    // 0b1000000000000 put
	    case 0x1000:
		if (rA == 0x00) {
		    FSLx = value & 0x00000007;
		    return "put     r" + rA + ", " + FSLx;
		}
		return "";
	    // 0b1010000000000 cput
	    case 0x1400:
		if (rA == 0x00) {
		    FSLx = value & 0x00000007;
		    return "cput    r" + rA + ", " + FSLx;
		}
		return "";
	    // 0b1100000000000 nput
	    case 0x1800:
		if (rA == 0x00) {
		    FSLx = value & 0x00000007;
		    return "nput    r" + rA + ", " + FSLx;
		}
		return "";
	    // 0b1110000000000 ncput
	    case 0x1c00:
		if (rA == 0x00) {
		    FSLx = value & 0x00000007;
		    return "ncput   r" + rA + ", " + FSLx;
		}
		return "";
	    default:
		return "";
	    }

	    // 0b100000 or
	case 0x20:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 or
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "or      r" + rD + ", r" + rA + ", r" + rB;
	    // 0b10000000000 pcmpbf
	    case 0x400:
		rB = (value >> 11) & 0x1f;
		return "pcmpbf  r" + rD + ", r" + rA + ", r" + rB;
	    default:
		return "";
	    }
	    // return "";
	    // 0b100001 and
	case 0x21:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "and     r" + rD + ", r" + rA + ", r" + rB;
	    }
	    return "";
	// 0b100010 pcmpeq xor
	case 0x22:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 xor
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "xor     r" + rD + ", r" + rA + ", r" + rB;
	    // 0b10000000000 pcmpeq
	    case 0x400:
		rB = (value >> 11) & 0x1f;
		return "pcmpeq  r" + rD + ", r" + rA + ", r" + rB;
	    default:
		return "";
	    }
	    // return "";
	    // 0b100011 andn pcmpne
	case 0x23:
	    type_a = value & 0x7ff;
	    switch (type_a) {
	    // 0b00000000000 andn
	    case 0x000:
		rB = (value >> 11) & 0x1f;
		return "andn    r" + rD + ", r" + rA + ", r" + rB;
	    // 0b10000000000 pcmpne
	    case 0x400:
		rB = (value >> 11) & 0x1f;
		return "pcmpne  r" + rD + ", r" + rA + ", r" + rB;
	    default:
		return "";
	    }
	    // return "";
	    // 0b100100 sext16 sext8 sra src srl wdc wic
	case 0x24:
	    if (rD == 0x00) {
		type_a = value & 0x7ff;
		switch (type_a) {
		// 0b00001100100 wdc
		case 0x064:
		    rB = (value >> 11) & 0x1f;
		    return "wdc     r" + rA + ", r" + rB;
		// 0b00001101000 wic
		case 0x068:
		    rB = (value >> 11) & 0x1f;
		    return "wic     r" + rA + ", r" + rB;
		default:
		    return "";
		}
	    }

	    type_b = value & 0xffff;
	    switch (type_b) {
	    // 0b0000000000000001 sra
	    case 0x0001:
		return "sra     r" + rD + ", r" + rA;
	    // 0b0000000000100001 src
	    case 0x0021:
		return "src     r" + rD + ", r" + rA;
	    // 0b0000000001000001 srl
	    case 0x0041:
		return "srl     r" + rD + ", r" + rA;
	    // 0b0000000001100000 sext8
	    case 0x0060:
		return "sext8   r" + rD + ", r" + rA;
	    // 0b0000000001100001 sext16
	    case 0x0061:
		return "sext16  r" + rD + ", r" + rA;
	    default:
		return "";
	    }

	    // 0b100101 mfs msrclr msrset mts
	case 0x25:
	    type_b_2 = (value >> 14) & 0x3;
	    switch (type_b_2) {
	    // 0b00 msrclr msrset
	    case 0x0:
		switch (rA) {
		// 0b00000 msrset
		case 0x00:
		    imm_14 = value & 0x3fff;
		    return "msrset  r" + rD + ", " + imm_14;
		// 0b00001 msrclr
		case 0x01:
		    imm_14 = value & 0x3fff;
		    return "msrclr  r" + rD + ", " + imm_14;
		default:
		    return "";
		}
		// return "";
		// 0b10 mfs
	    case 0x2:
		if (rA == 0x00) {
		    rS = value & 0x3fff;
		    return "mfs     r" + rD + ", " + nameSpecial(rS);
		}
		return "";
	    // 0b11 mts
	    case 0x3:
		type_b_13 = (value >> 3) & 0x1fff;
		if (type_b_13 == 0x1800 && rD == 0x00) {
		    rS = value & 0x0007;
		    return "mts     " + nameSpecial(rS) + ", r" + rA;
		}
		return "";
	    default:
		return "";
	    }

	    // 0b100110 br brk
	case 0x26:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		switch (rA) {
		// 0b00000 br
		case 0x00:
		    if (rD == 0x00) {
			rB = (value >> 11) & 0x1f;
			return "br      r" + rB;
		    }
		    return "";
		// 0b01000 bra
		case 0x08:
		    if (rD == 0x00) {
			rB = (value >> 11) & 0x1f;
			return "bra     r" + rB;
		    }
		    return "";
		// 0b01100 brk
		case 0x0c:
		    rB = (value >> 11) & 0x1f;
		    return "brk     r" + rD + ", r" + rB;
		// 0b10000 brd
		case 0x10:
		    if (rD == 0x00) {
			rB = (value >> 11) & 0x1f;
			return "brd     r" + rB;
		    }
		    return "";
		// 0b10100 brld
		case 0x14:
		    rB = (value >> 11) & 0x1f;
		    return "brld    r" + rD + ", r" + rB;
		// 0b11000 brad
		case 0x18:
		    if (rD == 0x00) {
			rB = (value >> 11) & 0x1f;
			return "brad    r" + rB;
		    }
		    return "";
		// 0b11100 brald
		case 0x1c:
		    rB = (value >> 11) & 0x1f;
		    return "brald   r" + rD + ", r" + rB;
		default:
		    return "";
		}
	    }
	    return "";
	// 0b100111 beq bge bgt ble blt bne
	case 0x27:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		switch (rD) {
		// 0b00000 beq
		case 0x00:
		    rB = (value >> 11) & 0x1f;
		    return "beq     r" + rA + ", r" + rB;
		// 0b00001 bne
		case 0x01:
		    rB = (value >> 11) & 0x1f;
		    return "bne     r" + rA + ", r" + rB;
		// 0b00010 blt
		case 0x02:
		    rB = (value >> 11) & 0x1f;
		    return "blt     r" + rA + ", r" + rB;
		// 0b00011 ble
		case 0x03:
		    rB = (value >> 11) & 0x1f;
		    return "ble     r" + rA + ", r" + rB;
		// 0b00100 bgt
		case 0x04:
		    rB = (value >> 11) & 0x1f;
		    return "bgt     r" + rA + ", r" + rB;
		// 0b00101 bge
		case 0x05:
		    rB = (value >> 11) & 0x1f;
		    return "bge     r" + rA + ", r" + rB;
		// 0b10000 beqd
		case 0x10:
		    rB = (value >> 11) & 0x1f;
		    return "beqd    r" + rA + ", r" + rB;
		// 0b10001 bned
		case 0x11:
		    rB = (value >> 11) & 0x1f;
		    return "bned    r" + rA + ", r" + rB;
		// 0b10010 bltd
		case 0x12:
		    rB = (value >> 11) & 0x1f;
		    return "bltd    r" + rA + ", r" + rB;
		// 0b10011 bled
		case 0x13:
		    rB = (value >> 11) & 0x1f;
		    return "bled    r" + rA + ", r" + rB;
		// 0b10100 bgtd
		case 0x14:
		    rB = (value >> 11) & 0x1f;
		    return "bgtd    r" + rA + ", r" + rB;
		// 0b10101 beqd
		case 0x15:
		    rB = (value >> 11) & 0x1f;
		    return "bged    r" + rA + ", r" + rB;
		default:
		    return "";
		}
	    }
	    return "";
	// 0b101000 ori
	case 0x28:
	    imm = value & 0xffff;
	    return "ori     r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b101001 andi
	case 0x29:
	    imm = value & 0xffff;
	    return "andi    r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b101010 xori
	case 0x2a:
	    imm = value & 0xffff;
	    return "xori    r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b101011 andni
	case 0x2b:
	    imm = value & 0xffff;
	    return "andni   r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b101100 imm
	case 0x2c:
	    if (rD == 0x00 && rA == 0x00) {
		imm = value & 0xffff;
		return "imm     " + signExtended(imm);
	    }
	    return "";
	// 0b101101 rtsd rted rtid rtbd
	case 0x2d:
	    switch (rD) {
	    // 0b10000 rtsd
	    case 0x10:
		imm = value & 0xffff;
		return "rtsd    r" + rA + ", " + imm;
	    // 0b10001 rtid
	    case 0x11:
		imm = value & 0xffff;
		return "rtid    r" + rA + ", " + signExtended(imm);
	    // 0b10010 rtbd
	    case 0x12:
		imm = value & 0xffff;
		return "rtbd    r" + rA + ", " + imm;
	    // 0b10100 rted
	    case 0x14:
		imm = value & 0xffff;
		return "rted    r" + rA + ", " + imm;
	    default:
		return "";
	    }
	    // 0b101110 bri brki
	case 0x2e:
	    switch (rA) {
	    // 0b00000 bri
	    case 0x00:
		if (rD == 0x00) {
		    imm = value & 0xffff;
		    return "bri     " + signExtended(imm);
		}
		return "";
	    // 0b01000 brai
	    case 0x08:
		if (rD == 0x00) {
		    imm = value & 0xffff;
		    return "brai    " + signExtended(imm);
		}
		return "";
	    // 0b01100 brki
	    case 0x0c:
		imm = value & 0xffff;
		return "brki    r" + rD + ", " + signExtended(imm);
	    // 0b10000 brid
	    case 0x10:
		if (rD == 0x00) {
		    imm = value & 0xffff;
		    return "brid    " + signExtended(imm);
		}
		return "";
	    // 0b10100 brlid
	    case 0x14:
		imm = value & 0xffff;
		return "brlid   r" + rD + ", " + signExtended(imm);
	    // 0b11000 braid
	    case 0x18:
		if (rD == 0x00) {
		    imm = value & 0xffff;
		    return "braid   " + signExtended(imm);
		}
		return "";
	    // 0b11100 bralid
	    case 0x1c:
		imm = value & 0xffff;
		return "bralid  r" + rD + ", " + signExtended(imm);
	    default:
		return "";
	    }

	    // 0b101111 beqi bnei blei bgti bgei blti
	case 0x2f:
	    switch (rD) {
	    // 0b00000 beqi
	    case 0x00:
		imm = value & 0xffff;
		return "beqi    r" + rA + ", " + signExtended(imm);
	    // 0b00001 bnei
	    case 0x01:
		imm = value & 0xffff;
		return "bnei    r" + rA + ", " + signExtended(imm);
	    // 0b00010 blti
	    case 0x02:
		imm = value & 0xffff;
		return "blti    r" + rA + ", " + signExtended(imm);
	    // 0b00011 blei
	    case 0x03:
		imm = value & 0xffff;
		return "blei    r" + rA + ", " + signExtended(imm);
	    // 0b00100 bgti
	    case 0x04:
		imm = value & 0xffff;
		return "bgti    r" + rA + ", " + signExtended(imm);
	    // 0b00101 bgei
	    case 0x05:
		imm = value & 0xffff;
		return "bgei    r" + rA + ", " + signExtended(imm);
	    // 0b10000 beqid
	    case 0x10:
		imm = value & 0xffff;
		return "beqid   r" + rA + ", " + signExtended(imm);
	    // 0b10001 bneid
	    case 0x11:
		imm = value & 0xffff;
		return "bneid   r" + rA + ", " + signExtended(imm);
	    // 0b10010 bltid
	    case 0x12:
		imm = value & 0xffff;
		return "bltid   r" + rA + ", " + signExtended(imm);
	    // 0b10011 bleid
	    case 0x13:
		imm = value & 0xffff;
		return "bleid   r" + rA + ", " + signExtended(imm);
	    // 0b10100 bgtid
	    case 0x14:
		imm = value & 0xffff;
		return "bgtid   r" + rA + ", " + signExtended(imm);
	    // 0b10101 bgeid
	    case 0x15:
		imm = value & 0xffff;
		return "bgeid   r" + rA + ", " + signExtended(imm);
	    default:
		return "";
	    }

	    // 0b110000 lbu
	case 0x30:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "lbu     r" + rD + ", r" + rA + ", r" + rB;
	    }
	    return "";
	// 0b110001 lhu
	case 0x31:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "lhu     r" + rD + ", r" + rA + ", r" + rB;
	    }
	    return "";
	// 0b110010 lw
	case 0x32:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "lw      r" + rD + ", r" + rA + ", r" + rB;
	    }
	    return "";
	// 0b110100 sb
	case 0x34:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "sb      r" + rD + ", r" + rA + ", r" + rB;
	    }
	    return "";
	// 0b110101 sh
	case 0x35:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "sh      r" + rD + ", r" + rA + ", r" + rB;
	    }
	    return "";
	// 0b110110 sw
	case 0x36:
	    type_a = value & 0x7ff;
	    if (type_a == 0x000) {
		rB = (value >> 11) & 0x1f;
		return "sw      r" + rD + ", r" + rA + ", r" + rB;
	    }
	    return "";
	// 0b111000 lbui
	case 0x38:
	    imm = value & 0xffff;
	    return "lbui    r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b111001 lhui
	case 0x39:
	    imm = value & 0xffff;
	    return "lhui    r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b111010 lwi
	case 0x3a:
	    imm = value & 0xffff;
	    return "lwi     r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b111100 sbi
	case 0x3c:
	    imm = value & 0xffff;
	    return "sbi     r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b111101 shi
	case 0x3d:
	    imm = value & 0xffff;
	    return "shi     r" + rD + ", r" + rA + ", " + signExtended(imm);
	// 0b111110 swi
	case 0x3e:
	    imm = value & 0xffff;
	    return "swi     r" + rD + ", r" + rA + ", " + signExtended(imm);
	default:
	    return "";
	}
	// return "";
    }

}
