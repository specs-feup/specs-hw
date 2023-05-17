package pt.up.specs.cgra.microcontroler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import pt.up.specs.cgra.CGRAUtils;
import pt.up.specs.cgra.structure.SpecsCGRA;

public class InstructionDecoder {

    /*
     * TODO: help map of control generators based on type of PE control?
     */

    /*
     * helper mapping of per-instruction decoders
     */
    interface InstDecoder {
        boolean apply(SpecsCGRA cgra, List<Integer> operands);
    }

    private static final Map<String, InstDecoder> InstDecoders;
    static {
        var amap = new HashMap<String, InstDecoder>();
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

    private SpecsCGRA parent;

    public InstructionDecoder(SpecsCGRA parent) {
        this.parent = parent;
    }

    private static void debug(String str) {
        CGRAUtils.debug(InstructionDecoder.class.getSimpleName(), str);
    }

    public boolean InstructionParser(String st) {

        var tk = new StringTokenizer(st);
        var operands = new ArrayList<String>();
        var instname_str = "";

        if (tk.hasMoreTokens())
            instname_str = tk.nextToken();

        while (tk.hasMoreElements())
            operands.add(tk.nextToken());

        var func = InstDecoders.get(instname_str);
        if (func == null)
            func = InstructionDecoder::undefined;

        InstructionDecoder.debug("received instruction:" + instname_str);
        return func.apply(this.parent, operands.stream().map(Integer::valueOf).collect(Collectors.toList()));
    }

    private static boolean set(SpecsCGRA cgra, List<Integer> ops) {

        if (ops.size() != 3) {
            InstructionDecoder.debug("Erro a ler parametros da instrucao set");
            return false;
        }

        var pe = cgra.getMesh().getProcessingElement(ops.get(0), ops.get(1));
        var ctrl = Integer.valueOf(ops.get(2));
        return ControlDecoder.applyControl(pe, ctrl);
    }

    private static boolean set_io(SpecsCGRA cgra, List<Integer> ops) {

        if (ops.size() != 6) {
            InstructionDecoder.debug("Erro a ler parametros da instrucao set_io");
            return false;
        }

        var pesrc = cgra.getMesh().getProcessingElement(ops.get(0), ops.get(1));
        var pe1 = cgra.getMesh().getProcessingElement(ops.get(2), ops.get(3));
        var pe2 = cgra.getMesh().getProcessingElement(ops.get(4), ops.get(5));
        if (pe1 == null || pe2 == null || pesrc == null) {
            InstructionDecoder.debug("Erro nos parametros da instrucao set_io");
            return true;
        }

        var intc = cgra.getInterconnect();
        if (!intc.setConnection(pe1.getPort(2), pesrc.getPort(0))) {
            InstructionDecoder.debug("Erro nos parametros da instrucao set_io");
            return true;
        }

        if (!intc.setConnection(pe2.getPort(2), pesrc.getPort(1))) {
            InstructionDecoder.debug("Erro nos parametros da instrucao set_io");
            return true;
        }

        // out de pe1 -> in1 de pesrc
        // out de pe2 -> in2 de pesrc
        return false;
    }

    private static boolean gen_info(SpecsCGRA cgra, List<Integer> ops) {
        return true;// print cgra.toString?
    }

    private static boolean pe_info(SpecsCGRA cgra, List<Integer> ops) {
        return true;// print pe.toString?
    }

    private static boolean mem_info(SpecsCGRA cgra, List<Integer> ops) {
        return true;// return genericmemory.tostring?
    }

    private static boolean store_ctx(SpecsCGRA cgra, List<Integer> ops) {
        return cgra.setContext(cgra.getInterconnect().getContext());
    }

    private static boolean switch_ctx(SpecsCGRA cgra, List<Integer> ops) {
        if (ops.size() != 1) {
            InstructionDecoder.debug("Erro a ler parametros da instrucao switch_ctx");
            return false;
        }
        return cgra.applyContext(ops.get(0));
    }

    private static boolean run(SpecsCGRA cgra, List<Integer> ops) {
        var i = cgra.execute();
        if (i > 0)
            return true;
        else
            return false;
    }

    private static boolean step(SpecsCGRA cgra, List<Integer> ops) {
        return cgra.step();
    }

    private static boolean pause(SpecsCGRA cgra, List<Integer> ops) {
        return cgra.pause();
    }

    private static boolean reset(SpecsCGRA cgra, List<Integer> ops) {
        return cgra.reset();
    }

    private static boolean undefined(SpecsCGRA cgra, List<Integer> ops) {
        InstructionDecoder.debug("Undefined instr");
        return true;
    }
}
