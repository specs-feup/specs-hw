package pt.up.fe.specs.binarytranslation.sandbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class SerializerTester {

	private static final Pattern ARM_REGEX = Pattern.compile("\\s(.[0-9a-f]):\\s*([0-9a-f]+)");

	/////////////// data to serialize
	static class testData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -906710544959694691L;
		public String st1;
		public int int1;
		public Vector<Integer> vec1;

		public testData() {
			this.vec1 = new Vector<Integer>();
		}
	}

	public static void main(String[] args) {

		var output = SpecsSystem.runProcess(Arrays.asList("arm-none-eabi-objdump", "-d", "./resources/test.elf"),
				new File("."), SerializerTester::streamProcessor, SerializerTester::streamProcessor);

		System.out.println("STD OUT:\n" + output.getStdOut());
		System.out.println("STD ERR:\n" + output.getStdErr());
	}

	public static String streamProcessor(InputStream inputStream) {

		try (var lineStream = LineStream.newInstance(inputStream, "stream")) {

			StringBuilder out = new StringBuilder();

			while (lineStream.hasNextLine()) {
				String line = lineStream.nextLine();

				if (!SpecsStrings.matches(line, ARM_REGEX)) {
					continue;
				}

				var addressAndInst = SpecsStrings.getRegex(line, ARM_REGEX);

				System.out.println("ADDR AND INST: " + addressAndInst);

				out.append(line).append("\n");
//				out.append(lineStream.nextLine());
			}

			return out.toString();
		}

	}

	public static void main2(String[] args) {

		testData td = new testData();
		td.int1 = 4;
		td.st1 = "test1";
		td.vec1.add(1);
		td.vec1.add(4);
		td.vec1.add(5);

		try {
			Serializer.toFile(td, "testfile.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testData td2 = new testData();
		try {
			td2 = (testData) Serializer.fromFile("testfile.txt");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}
}
