package pt.up.specs.cgra.instructions_decoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import pt.up.specs.cgra.structure.SpecsCGRA;

public class InstructionDecoder {

	/*
     * helper mapping of per-instruction decoders
     */
    interface InstDecoder {
        boolean apply(SpecsCGRA cgra, List<String> operands);
    }
    
    private static final Map<String, InstDecoder> InstDecoders;
    static {
        Map<String, InstDecoder> amap = new HashMap<String, InstDecoder>();
        amap.put("SET", InstructionDecoder::SET);
        InstDecoders = Collections.unmodifiableMap(amap);
    }
    
	SpecsCGRA parent;

    public InstructionDecoder(SpecsCGRA parent) {
        this.parent = parent;
    }

    // TODO: tokenizar string,
    public boolean InstructionParser(String st) {
    	
    	var tk = new StringTokenizer(st);
    	var instname = tk.nextToken(); // TODO: exception
    	var operands = new ArrayList<String>();
        while (tk.hasMoreElements())
        	operands.add(tk.nextToken());
   
        // get decoder of this instruction
        var func = InstDecoders.get(instname);
        if (func == null)
            func = InstructionDecoder::undefined;

        return func.apply(this.parent, operands);
        
    	/*
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
        return true;*/
    }

    // TODO: definir limites dos bits
    public void InstructionParser(Integer x) {

    }

    //private boolean SET(int x, int y, PEControlSetting ctrl) {
    private static boolean SET(SpecsCGRA cgra, List<String> operands) {
    	var x = Integer.valueOf(operands.get(0));
    	var y = Integer.valueOf(operands.get(1));
    	
    	var ctrl = Integer.valueOf(operands.get(2));
        
    	cgra.getMesh().getProcessingElement(x, y).setControl(ctrl);
        return true;
    }
/*
    private boolean SET_IO(List<String> operands) {
        return true;
    }

    private boolean GEN_INFO(List<String> operands) {
        return true;// return cgra.toString?

    }

    private boolean PE_INFO(List<String> operands) {
        return true;// return pe.toString?

    }

    private boolean MEM_INFO(List<String> operands) {
        return true;

    }

    private boolean STORE_CTX(List<String> operands) {
        return true;

    }

    private boolean SWITCH_CTX(List<String> operands) {
        return true;

    }

    private boolean RUN(List<String> operands) {
        return true;

    }

    private boolean PAUSE(List<String> operands) {
        return true;

    }

    private boolean RESET(List<String> operands) {
        return true;
    }
*/
    
    private static boolean undefined(SpecsCGRA cgra, List<String> operands) {
        return false;
    }
}
