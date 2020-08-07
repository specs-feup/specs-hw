package pt.up.fe.specs.binarytranslation.gearman.workers.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExampleOutput {

    private int anInteger;
    private String aString;
    private boolean aBoolean;
    private String[] aStringArray;

    public ExampleOutput(int anInteger, String aString, boolean aBoolean, String[] aStringArray) {
        super();
        this.anInteger = anInteger;
        this.aString = aString;
        this.aBoolean = aBoolean;
        this.aStringArray = aStringArray;
    }

    public int getAnInteger() {
        return anInteger;
    }

    public void setAnInteger(int anInteger) {
        this.anInteger = anInteger;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public String[] getaStringArray() {
        return aStringArray;
    }

    public void setaStringArray(String[] aStringArray) {
        this.aStringArray = aStringArray;
    }

    /**
     * Create a {@link ExampleOutput} instance containing the error message
     * 
     * @param errorMessage
     * @return
     */
    public static byte[] getErrorOutput(String errorMessage) {
        // Create empty output with error message
        ExampleOutput clavaOutput = new ExampleOutput(-1, errorMessage, true, new String[0]);

        Gson gson = new GsonBuilder().create();
        return gson.toJson(clavaOutput).getBytes();
    }

    /**
     * New instance of {@link ExampleOutput} based on the bytes array of a json object
     * 
     * @param data
     *                 the json object byte array
     * @return
     */
    public static ExampleOutput newInstance(byte[] data) {
        String dataString = new String(data);
        Gson gson = new GsonBuilder().create();
        ExampleOutput clavaOutput = gson.fromJson(dataString, ExampleOutput.class);

        return clavaOutput;
    }

    public byte[] getJsonBytes() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this).getBytes();
    }
}
