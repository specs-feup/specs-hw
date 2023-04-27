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
		amap.put("step", InstructionDecoder::step);
		amap.put("pause", InstructionDecoder::pause);
		amap.put("reset", InstructionDecoder::reset);


		InstDecoders = Collections.unmodifiableMap(amap);
	}

	SpecsCGRA parent;

	public InstructionDecoder(SpecsCGRA parent) {
		this.parent = parent;
	}

	public boolean InstructionParser(String st){

		var tk = new StringTokenizer(st);
		var operands = new ArrayList<String>();

		var instname_str = "";
		
		if(tk.hasMoreTokens())
			instname_str = tk.nextToken();

		while (tk.hasMoreElements())
			operands.add(tk.nextToken());


		var func = InstDecoders.get(instname_str);
		if (func == null)
			func = InstructionDecoder::undefined;
		
		System.out.printf("received instruction: '%s' \n", instname_str);


		return func.apply(this.parent, operands);
	}


	private static boolean set(SpecsCGRA cgra, List<String> operands){

		if(operands.size() != 3) {
			System.out.println("Erro a ler parametros da instrucao set");
			return false;			
		}

		var x = Integer.valueOf(operands.get(0));
		var y = Integer.valueOf(operands.get(1));
		var ctrl = Integer.valueOf(operands.get(2));

		return cgra.getMesh().getProcessingElement(x, y).setControl(ctrl);
	}

	private static boolean set_io(SpecsCGRA cgra, List<String> operands) {

		if(operands.size() != 6) {
			System.out.println("Erro a ler parametros da instrucao set_io");
			return false;			
		}

		var xs = Integer.valueOf(operands.get(0));
		var ys = Integer.valueOf(operands.get(1));

		var x1 = Integer.valueOf(operands.get(2));
		var y1 = Integer.valueOf(operands.get(3));

		var x2 = Integer.valueOf(operands.get(4));
		var y2 = Integer.valueOf(operands.get(5)); 

		var pesrc = cgra.getMesh().getProcessingElement(xs, ys);

		var pe1 = cgra.getMesh().getProcessingElement(x1, y1);
		var pe2 = cgra.getMesh().getProcessingElement(x2, y2);


		if (pe1 != null && pe2 != null && pesrc != null) {

			return (cgra.getInterconnect().setConnection(pe1.getPorts().get(2), pesrc.getPorts().get(0)) 
					&& cgra.getInterconnect().setConnection(pe2.getPorts().get(2), pesrc.getPorts().get(1))
					); 
			
		}
		
		//out de pe1 -> in1 de pesrc
		//out de pe2 -> in2 de pesrc

		return false;

	}

	private static boolean gen_info(SpecsCGRA cgra, List<String> operands) {

		return true;// print cgra.toString?

	}

	private static boolean pe_info(SpecsCGRA cgra, List<String> operands) {

		return true;// print pe.toString?

	}

	private static boolean mem_info(SpecsCGRA cgra, List<String> operands) {

		return true;// return genericmemory.tostring?

	}

	private static boolean store_ctx(SpecsCGRA cgra, List<String> operands) {
		
		return cgra.setContext(cgra.getInterconnect().getContext());
	}

	private static boolean switch_ctx(SpecsCGRA cgra, List<String> operands){
		
		if(operands.size() != 1) {
			System.out.println("Erro a ler parametros da instrucao switch_ctx");
			return false;			
		}

		var index = Integer.valueOf(operands.get(0));

		return cgra.applyContext(index);
	}

	private static boolean run(SpecsCGRA cgra, List<String> operands) {

		var i = cgra.execute();
		
		if (i > 0) return true;
		else return false;

	}
	
	private static boolean step(SpecsCGRA cgra, List<String> operands) {

		return cgra.step();

	}

	private static boolean pause(SpecsCGRA cgra, List<String> operands) {

		return cgra.pause();

	}

	private static boolean reset(SpecsCGRA cgra, List<String> operands) {
		
		return cgra.reset();
		
	}


	private static boolean undefined(SpecsCGRA cgra, List<String> operands) {
		System.out.println("Undefined instr");
		return true;
	}
}
