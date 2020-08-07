package pt.up.fe.specs.binarytranslation.gearman.workers.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExampleInput {
    private String field1;
    private String anotherField;

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("field1: " + field1 + "\n");
        string.append("anotherField: " + anotherField + "\n");

        return string.toString();
    }

    public String getField1() {
        return field1;
    }

    public String getAnotherField() {
        return anotherField;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public void setAnotherField(String anotherField) {
        this.anotherField = anotherField;
    }

    /**
     * New instance of {@link ExampleInput} based on the bytes array of a json object
     * 
     * @param data
     *                 the json object byte array
     * @return
     */
    public static ExampleInput newInstance(byte[] data) {
        String dataString = new String(data);
        Gson gson = new GsonBuilder().create();
        ExampleInput exampleInput = gson.fromJson(dataString, ExampleInput.class);
        return exampleInput;
    }

}
