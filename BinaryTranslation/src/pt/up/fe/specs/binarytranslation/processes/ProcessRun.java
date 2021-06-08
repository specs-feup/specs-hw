package pt.up.fe.specs.binarytranslation.processes;

import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

public interface ProcessRun {

    /*
     * 
     */
    public ConcurrentChannel<String> getStdin();

    /*
     * 
     */
    public ConcurrentChannel<String> getStdout();

    /*
     * 
     */
    public Process getProc();

    /*
     * 
     */
    public boolean isAlive();

    /*
     * 
     */
    public String receive(int i);
}
