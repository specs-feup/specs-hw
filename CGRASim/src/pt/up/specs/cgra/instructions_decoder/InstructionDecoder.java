package pt.up.specs.cgra.instructions_decoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSettingObject;

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

	public boolean InstructionParser(String st) throws NoSuchElementException{

		var tk = new StringTokenizer(st);
		var operands = new ArrayList<String>();

		String instname_str;

		try 
		{
			instname_str = tk.nextToken(); // TODO: exception

			while (tk.hasMoreElements())
				operands.add(tk.nextToken());
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Erro a ler opcode da instrucao: " + st);
			return false;
		};

		// get decoder of this instruction
		var func = InstDecoders.get(instname_str);
		if (func == null)
			func = InstructionDecoder::undefined;

		return func.apply(this.parent, operands);


	}

	// TODO: definir limites dos bits
	/*    public void InstructionParser(Integer x) {

    }*/
	//TODO exception
	private static boolean set(SpecsCGRA cgra, List<String> operands) throws IndexOutOfBoundsException, Exception{

		Integer x, y, ctrl;

		try
		{
			x = Integer.valueOf(operands.get(0));
			y = Integer.valueOf(operands.get(1));
			ctrl = Integer.valueOf(operands.get(2));
		}
		catch(IndexOutOfBoundsException e)
		{
			System.out.println("Erro a ler parametros da instrucao set");
			return false;
		}

		try {
			cgra.getMesh().getProcessingElement(x, y).setControl(ctrl);
		}
		catch(Exception e)
		{
			System.out.println("Erro na instrucao set - definir operacao");
			return false;
		}

		return true;
	}

	//TODO exception
	private static boolean set_io(SpecsCGRA cgra, List<String> operands) throws IndexOutOfBoundsException, Exception {

		Integer xs, ys, x1, y1, x2, y2;

		try {
			xs = Integer.valueOf(operands.get(0));
			ys = Integer.valueOf(operands.get(1));

			x1 = Integer.valueOf(operands.get(2));
			y1 = Integer.valueOf(operands.get(3));

			x2 = Integer.valueOf(operands.get(4));
			y2 = Integer.valueOf(operands.get(5)); }

		catch (IndexOutOfBoundsException a)
		{
			System.out.println("Erro a ler parametros da instrucao set_io");
			return false;
		}

		ProcessingElement pe1 = null, pe2 = null, pesrc = null;
		try
		{
			pe1 = cgra.getMesh().getProcessingElement(x1, y1);
			pe2 = cgra.getMesh().getProcessingElement(x2, y2);
			pesrc = cgra.getMesh().getProcessingElement(xs, ys);
		}
		catch (NoSuchElementException e)
		{
			System.out.println("Erro na instrucao set_io - coordenadas dos PEs");
			return false;
		}

		try {
			cgra.getInterconnect().setConnection(pe1.getPorts().get(2), pesrc.getPorts().get(0)); //out de pe1 -> in1 de pesrc
			cgra.getInterconnect().setConnection(pe2.getPorts().get(2), pesrc.getPorts().get(1)); //out de pe2 -> in2 de pesrc
		}
		catch (Exception e)
		{
			System.out.println("Erro na instrucao set_io - ligacao dos ports");
			return false;
		}
		return true;

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

	private static boolean store_ctx(SpecsCGRA cgra, List<String> operands) throws IndexOutOfBoundsException{

		Integer id;
		try {
			id = Integer.valueOf(operands.get(0));
		}
		catch (IndexOutOfBoundsException e)
		{
			System.out.println("Erro a ler parametros da instrucao store_ctx");
			return false;
		}

		cgra.setContext(cgra.getInterconnect().makeContext(id));
		return true;

	}

	private static boolean switch_ctx(SpecsCGRA cgra, List<String> operands) throws IndexOutOfBoundsException{

		Integer id;
		try {
			id = Integer.valueOf(operands.get(0));
		}
		catch (IndexOutOfBoundsException e)
		{
			System.out.println("Erro a ler parametros da instrucao switch_ctx");
			return false;
		}

		return cgra.applyContext(id);

	}

	private static boolean run(SpecsCGRA cgra, List<String> operands) {

		return cgra.execute();

	}

	private static boolean pause(SpecsCGRA cgra, List<String> operands) {

		cgra.pause();
		return true;

	}

	private static boolean reset(SpecsCGRA cgra, List<String> operands) {
		return true;
	}


	private static boolean undefined(SpecsCGRA cgra, List<String> operands) {
		return false;
	}
}
