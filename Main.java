import com.sleepycat.db.*;
import java.io.*;
import java.util.*;


public class Main
{

	private static final String DB_DIRECTORY = "/tmp/edcarter";
	private static final int NO_RECORDS = 100000;
	public static String ANSWER_PATH = "answers";

	/*
		Run the database application.  Log the user into the database and then prompt
		them to select which action they want to perform.
	*/
	public static void main(String[] args) {

		Answers answers = new Answers(ANSWER_PATH);
		answers.createAnswersFile();

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
				sdb.createDb(args[0]);
			} else if (action.equals("2")) {
				KeySearchDB ksdb = new KeySearchDB();
				ksdb.run();
			} else if (action.equals("3")) {
			} else if (action.equals("4")) {
			} else if (action.equals("5")) {
				SetupDB sdb = new SetupDB();
				sdb.delDB();
			} else if (action.equals("6")) {
				break;
			} else {
				System.err.println("Invalid value entered, please try again\n");
			}
		}

		answers.clearAnswersFile();
		try {
			DbInstance.getInstance().close();
		} catch (Exception ex) {
			System.err.println("Error closing database.");
		}
	}
}
