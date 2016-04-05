import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class DataSearchDB
{
	public DataSearchDB() { }

	public void run(String dbType) {
		try {
			System.out.println("Enter a data value to search:");
			Console co = System.console();
			String search = co.readLine().toLowerCase();
			List<KeyValue> records = new ArrayList<KeyValue>();
			Database std_db = DbInstance.getInstance(dbType);
			Cursor std_cursor = std_db.openCursor(null, null);
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry data = new DatabaseEntry();
			long start;

			if (dbType.equals("3")) {
				DatabaseEntry pKey = new DatabaseEntry();

				SecondaryDatabase indexDb = DbInstance.getIndexInstance(std_db);
				SecondaryCursor sec_cursor = indexDb.openSecondaryCursor(null, null);
		        key.setData(search.getBytes());
		        key.setSize(search.length());
				start = System.nanoTime();
				//OperationStatus oprStatus = indexDb.get(null, key, data, LockMode.DEFAULT);
				OperationStatus oprStatus = sec_cursor.getSearchKey(key, pKey, data, LockMode.DEFAULT);
				records.add(new KeyValue(new String(pKey.getData()), new String(data.getData())));

			} else {
				start = System.nanoTime();

				OperationStatus status = std_cursor.getFirst(key, data, LockMode.DEFAULT);
				while (status == OperationStatus.SUCCESS) {
					String datastr = new String(data.getData());
					if (datastr.equals(search)) {
						String keystr = new String(key.getData());
						records.add(new KeyValue(keystr, datastr));
					}
					status = std_cursor.getNext(key, data, LockMode.DEFAULT);
				}
			}

			long end = System.nanoTime();
			long micros = (end - start) / 1000;

			System.out.println(String.format("%d records retrieved in %d micro-seconds", records.size(), micros));
			Answers ans = new Answers("answers");
			ans.insertAnswers(records);
		} catch (DatabaseException ex) {
			System.out.println("Unable to search DB by data value");
		}
	}
}
