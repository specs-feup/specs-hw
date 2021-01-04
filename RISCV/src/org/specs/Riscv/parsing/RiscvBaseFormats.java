package org.specs.Riscv.parsing;

public enum RiscvBaseFormats {

    R("opcodec(7)_rs2(5)_rs1(5)_opcodeb(3)_rd(5)"),
    R4("rs3(3)_funct2(2)_rs2(5)_rs1(5)_opcodeb(3)_rd(5)"),
    I("immtwelve(12)_rs1(5)_funct3(3)_rd(5)"),
    S("immseven(7)_rs2(5)_rs1(5)_opcode(3)_immfive(5)"),
    U("immtwenty(20)_rd(5)");

    private String format;

    private RiscvBaseFormats(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}