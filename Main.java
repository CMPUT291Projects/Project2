import com.sleepycat.db.*;
import java.io.*;
import java.util.*;


public class Main
{

	public static String ANSWER_PATH = "answers";

	/*
		Run the database application.  Log the user into the database and then prompt
		them to select which action they want to perform.
	*/
	public static void main(String[] args) {

		Answers answers = new Answers(ANSWER_PATH);
		answers.createAnswersFile();
		String dbType = args[0];

		while (true) {
			System.out.print("To create and populate a database enter '1'\n" +
				"To retrieve records with a given key enter '2'\n" +
				"To retrieve records with a given data enter '3'\n" +
				"To retrieve records with a given reange of key values  enter '4'\n" +
				"To destroy the database enter '5'\n" +
				"To quit enter '6'\n");
			
			Console co = System.console();
			String action = co.readLine().toUpperCase();
			if (action.equals("1")) {
				SetupDB sdb = new SetupDB();
				sdb.createDb(dbType);
			} else if (action.equals("2")) {
				KeySearchDB ksdb = new KeySearchDB();
				ksdb.run(dbType);
			} else if (action.equals("3")) {
				//retrieve by data
			} else if (action.equals("4")) {
				//retrieve range
				RangeSearchDB rs = new RangeSearchDB();
				rs.run(dbType);
			} else if (action.equals("5")) {
				DbInstance.deleteInstance(dbType);
			} else if (action.equals("6")) {
				break;
			} else {
				System.err.println("Invalid value entered, please try again\n");
			}
		}

		answers.clearAnswersFile();
		try {
			DbInstance.getInstance(dbType).close();
		} catch (Exception ex) {
			System.err.println("Error closing database.");
		}
	}
}
