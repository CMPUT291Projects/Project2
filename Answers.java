import java.io.*;
import java.util.List;

public class Answers
{

	private String path;

	public Answers(String path) {
		this.path = path;
	}

	public void createAnswersFile() {
		try {
			File file = new File(path);
			file.createNewFile();
		} catch (Exception e) {
			System.err.println("File creation error: " + e.toString());
		}
	}

	public void clearAnswersFile() {
		try {
			File file = new File(path);
			file.delete();
		} catch (Exception e) {
			System.err.println("File creation error: " + e.toString());
		}
	}

	public void insertAnswer(KeyValue answer) {
		try {
			File file = new File(path);
			PrintWriter writer = new PrintWriter(new FileOutputStream(file, true));
			writer.println(answer.getKey());
			writer.println(answer.getValue());
			writer.println("");
			writer.close();
		} catch (Exception ex) {
			System.err.println("Error writing answers to file");
		}	
	}

	public void insertAnswers(List<KeyValue> answers) {
		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			for (KeyValue kv : answers) {
				writer.println(kv.getKey());
				writer.println(kv.getValue());
				writer.println("");
			}
			writer.close();
		} catch (Exception ex) {
			System.err.println("Error writing answers to file");
		}	
	}
}
