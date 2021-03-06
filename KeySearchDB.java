import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class KeySearchDB {
    DatabaseEntry key;
    DatabaseEntry data;


    public KeySearchDB() {
    }

    public void run(String dbType) {
        Database std_db = DbInstance.getInstance(dbType);
        System.out.println("Enter a key to search:");
        Console co = System.console();
	String keystr = co.readLine().toLowerCase();

        key = new DatabaseEntry();
        key.setData(keystr.getBytes());
        key.setSize(keystr.length());
        data = new DatabaseEntry();

	try {
		long start = System.nanoTime();
		OperationStatus oprStatus = std_db.get(null, key, data, LockMode.DEFAULT);
		long end = System.nanoTime();

		long micros = (end - start) / 1000;

		if (data.getData() == null) {
			System.out.println(String.format("0 records retrieved in %d micro-seconds", micros));
			System.out.println("No entry with that key");
			return;
		}
		System.out.println(String.format("1 record retrieved in %d micro-seconds", micros));
		String datastr = new String(data.getData());
		Answers ans = new Answers("answers");
		ans.insertAnswer(new KeyValue(keystr, datastr));
	} catch (DatabaseException ex) {
		System.err.println("Error doing key search on DB");
	}
    }
}
