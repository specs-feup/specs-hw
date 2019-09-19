package pt.up.fe.specs.binarytranslation.sandbox;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

	/*
	 * Finishes InstructionTrace by writing it to file specified by "it.filename"
	 */
	public static void toFile(Object obj, String fileName) throws IOException {
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(obj);
		out.flush();
		out.close();
		return;
	}

	/*
	 * Inits InstructionTrace by reading from file indicated in "it.filename"
	 */
	public static Object fromFile(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream file = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(file);
		Object obj = in.readObject();
		in.close();
		file.close();
		return obj;
	}
}
