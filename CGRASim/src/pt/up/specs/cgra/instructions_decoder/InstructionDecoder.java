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
        amap.put("set", InstructionDecoder::set);
        amap.put("set_io", InstructionDecoder::set_io);
        amap.put("gen_info", InstructionDecoder::gen_info);
        amap.put("pe_info", InstructionDecoder::pe_info);
        amap.put("mem_info", InstructionDecoder::mem_info);
        amap.put("store_ctx", InstructionDecoder::store_ctx);
        amap.put("switch_ctx", InstructionDecoder::switch_ctx);
        amap.put("run", InstructionDecoder::run);
        amap.put("pause", InstructionDecoder::pause);
        amap.put("reset", InstructionDecoder::reset);


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
    private static boolean set(SpecsCGRA cgra, List<String> operands) {
    	var x = Integer.valueOf(operands.get(0));
    	var y = Integer.valueOf(operands.get(1));
    	var ctrl = Integer.valueOf(operands.get(2));
    	cgra.getMesh().getProcessingElement(x, y).setControl(ctrl);
        return true;
    }
    
    private static boolean set_io(SpecsCGRA cgra, List<String> operands) {
    	
    	var xs = Integer.valueOf(operands.get(0));
    	var ys = Integer.valueOf(operands.get(1));
    	
    	var x1 = Integer.valueOf(operands.get(2));
    	var y1 = Integer.valueOf(operands.get(3));

    	var x2 = Integer.valueOf(operands.get(4));
    	var y2 = Integer.valueOf(operands.get(5));
    	
    	

    	
    	
        return true;// return cgra.toString?

    }

    private static boolean gen_info(SpecsCGRA cgra, List<String> operands) {
    	
        return true;// return cgra.toString?

    }

    private static boolean pe_info(SpecsCGRA cgra, List<String> operands) {
    	
        return true;// return pe.toString?

    }

    private static boolean mem_info(SpecsCGRA cgra, List<String> operands) {
    	
        return true;// return genericmemory.tostring?

    }

    private static boolean store_ctx(SpecsCGRA cgra, List<String> operands) {
        return true;

    }

    private static boolean switch_ctx(SpecsCGRA cgra, List<String> operands) {
        return true;

    }

    private static boolean run(SpecsCGRA cgra, List<String> operands) {
        return true;

    }

    private static boolean pause(SpecsCGRA cgra, List<String> operands) {
        return true;

    }

    private static boolean reset(SpecsCGRA cgra, List<String> operands) {
        return true;
    }

    
    private static boolean undefined(SpecsCGRA cgra, List<String> operands) {
        return false;
    }
}
