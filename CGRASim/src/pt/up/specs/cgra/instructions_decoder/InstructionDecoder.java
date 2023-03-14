package pt.up.specs.cgra.instructions_decoder;

import java.util.StringTokenizer;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.pes.AProcessingElement;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSettingObject;
import pt.up.specs.cgra.structure.pes.alu.ALUElement;

public class InstructionDecoder {

    GenericSpecsCGRA parent;

    public InstructionDecoder(GenericSpecsCGRA parent) {
        this.parent = parent;
    }

    // TODO: tokenizar string,
    public boolean InstructionParser(String st) {
        var tk = new StringTokenizer(st);
        while (tk.hasMoreElements()) {
            var s = tk.nextToken();

            switch (s) {
            case "SET":
                var x = Integer.parseInt(tk.nextToken());
                var y = Integer.parseInt(tk.nextToken());
                var op = Integer.parseInt(tk.nextToken());
                var pe = new ALUElement();
                var aux = new ALUControlSettingObject(op);

                SET(x, y, aux);
            }
        }
        return true;
    }

    // TODO: definir limites dos bits
    public void InstructionParser(Integer x) {

    }

    private <Y extends PEControlSetting> boolean SET(int x, int y, Y ctrl) {

        parent.getMesh().getProcessingElement(x, y).setControl(ctrl);

        return true;

    }

    private <T extends AProcessingElement> boolean SET_IO() {
        return true;

    }

    private boolean GEN_INFO() {
        return true;// return cgra.toString?

    }

    private boolean PE_INFO() {
        return true;// return pe.toString?

    }

    private boolean MEM_INFO() {
        return true;

    }

    private boolean STORE_CTX() {
        return true;

    }

    private boolean SWITCH_CTX() {
        return true;

    }

    private boolean RUN() {
        return true;

    }

    private boolean PAUSE() {
        return true;

    }

    private boolean RESET() {
        return true;

    }

}
