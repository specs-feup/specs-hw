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
		String apply(SpecsCGRA cgra, List<String> operands);
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

	public String InstructionParser(String st){

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

		System.out.printf("received instruction: '%s' \n", st);


		return func.apply(this.parent, operands);
	}


	private static String set(SpecsCGRA cgra, List<String> operands){

		if(operands.size() != 3) {
			return String.format("Error reading SET instruction parameters");
		}

		var x = Integer.valueOf(operands.get(0));
		var y = Integer.valueOf(operands.get(1));
		var ctrl = Integer.valueOf(operands.get(2));

		if (cgra.getMesh().getProcessingElement(x, y).setControl(ctrl))
		{
			return String.format("SET succesful");
		}

		else
		{
			return String.format("SET unsuccesful");
		}
	}

	private static String set_io(SpecsCGRA cgra, List<String> operands) {

		if(operands.size() != 6) {
			return String.format("Error reading SET_IO instruction parameters");
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

			if ((cgra.getInterconnect().setConnection(pe1.getPorts().get(2), pesrc.getPorts().get(0)) 
					&& cgra.getInterconnect().setConnection(pe2.getPorts().get(2), pesrc.getPorts().get(1))
					))
			{
				return String.format("SET_IO succesful");
			}

			else
			{
				return String.format("SET_IO unsuccesful");
			}


		}
		return String.format("SET_IO unsuccesful");

		//out de pe1 -> in1 de pesrc
		//out de pe2 -> in2 de pesrc


	}

	private static String gen_info(SpecsCGRA cgra, List<String> operands) {

		return "x";// print cgra.toString?

	}

	private static String pe_info(SpecsCGRA cgra, List<String> operands) {

		return "y";// print pe.toString?

	}

	private static String mem_info(SpecsCGRA cgra, List<String> operands) {

		return "z";// return genericmemory.tostring?

	}

	private static String store_ctx(SpecsCGRA cgra, List<String> operands) {

		if (cgra.setContext(cgra.getInterconnect().getContext()))
		{
			return String.format("STORE_CTX succesful");
		}

		else
		{
			return String.format("STORE_CTX unsuccesful");
		}
	}

	private static String switch_ctx(SpecsCGRA cgra, List<String> operands){

		if(operands.size() != 1) {
			return String.format("Error reading SWITCH_CTX instruction parameters");
		}

		var index = Integer.valueOf(operands.get(0));

		if (cgra.applyContext(index))
		{
			return String.format("SWITCH_CTX succesful");
		}

		else
		{
			return String.format("SWITCH_CTX unsuccesful");
		}
	}

	private static String run(SpecsCGRA cgra, List<String> operands) {

		var i = cgra.execute();

		if (i > 0) return String.format ("RUN succesful with %d steps", i);
		else return String.format("RUN unsuccesful with %d steps", i);

	}

	private static String step(SpecsCGRA cgra, List<String> operands) {

		if (cgra.step())
		{
			return String.format("STEP succesful");
		}

		else
		{
			return String.format("STEP unsuccesful");
		}

	}

	private static String pause(SpecsCGRA cgra, List<String> operands) {

		if (cgra.pause())
		{
			return String.format("PAUSE succesful");
		}

		else
		{
			return String.format("PAUSE unsuccesful");
		}
	}

	private static String reset(SpecsCGRA cgra, List<String> operands) {

		if (cgra.reset())
		{
			return String.format("RESET succesful");
		}

		else
		{
			return String.format("RESET unsuccesful");
		}		
	}


	private static String undefined(SpecsCGRA cgra, List<String> operands) {
		return String.format("Undefined instr");
	}
}
