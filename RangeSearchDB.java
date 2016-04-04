import com.sleepycat.db.*;
import java.util.*;
import java.io.*;

public class RangeSearchDB
{

    public RangeSearchDB() { }

    public void run(String dbType) {
        Database std_db = DbInstance.getInstance(dbType);
        System.out.println("Enter a start key to search:");
        Console co = System.console();
	String startkeystr = co.readLine().toLowerCase();
	
	System.out.println("Enter a end key to search:");
	String endkeystr = co.readLine().toLowerCase();
	String datastr = null;
	String keystr = null;

	

        DatabaseEntry key = new DatabaseEntry();
        DatabaseEntry data = new DatabaseEntry();

	try {
		long micros = 0;
		List<KeyValue> records = new ArrayList<KeyValue>();
		if (dbType.equals("1")) {
			//btree
			key.setData(startkeystr.getBytes());
        		key.setSize(startkeystr.length());
			long start = System.nanoTime();
			Cursor std_cursor = std_db.openCursor(null, null); 
			OperationStatus oprStatus = std_cursor.getSearchKeyRange(key, data, LockMode.DEFAULT);
			keystr = new String(key.getData());
			datastr = new String(data.getData());

			while (keystr.compareTo(endkeystr) <= 0 && oprStatus == OperationStatus.SUCCESS) {
				records.add(new KeyValue(keystr, datastr));
				oprStatus = std_cursor.getNext(key, data, LockMode.DEFAULT);
				keystr = new String(key.getData());
				datastr = new String(data.getData());
			}
			long end = System.nanoTime();

			micros = (end - start) / 1000;
		} else if (dbType.equals("2")) {
			//hashtable
			long start = System.nanoTime();
			Cursor std_cursor = std_db.openCursor(null, null);
			OperationStatus oprStatus = std_cursor.getFirst(key, data, LockMode.DEFAULT);
			keystr = new String(key.getData());
			datastr = new String(data.getData());

			while (oprStatus == OperationStatus.SUCCESS) {
				if (keystr.compareTo(endkeystr) <= 0 && keystr.compareTo(startkeystr) >= 0) {
					records.add(new KeyValue(keystr, datastr));
				}
				oprStatus = std_cursor.getNext(key, data, LockMode.DEFAULT);
				keystr = new String(key.getData());
				datastr = new String(data.getData());
			}
			long end = System.nanoTime();

			micros = (end - start) / 1000;
			
		}

		System.out.println(String.format("%d records retrieved in %d micro-seconds", records.size(), micros));
		Answers ans = new Answers("answers");
		ans.insertAnswers(records);

	} catch (DatabaseException ex) {
		System.err.println("Error doing key search on DB");
	}
    }
}
