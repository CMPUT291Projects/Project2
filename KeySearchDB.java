import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class KeySearchDB {
    DatabaseEntry key;
    DatabaseEntry data;

    public KeySearchDB() {
        System.out.print("Enter a key to search");
        Console co = System.console();
		String keystr = co.readLine().toLowerCase();
		System.out.println();

        this.key = new DatabaseEntry();
        this.key.setData(keystr.getBytes());
        this.key.setSize(keystr.lenth());
        this.data = new DatabaseEntry();

        OpperationStatus oprStatus = std_cursor.getSearchKey(key, data, LockMode.DEFAULT);

		while (oprStatus == OperationStatus.SUCCESS)
		{
			String s = new String(data.getData( ));
			System.out.println(s);
			oprStatus = std_cursor.getNextDup(key, data, LockMode.DEFAULT);
		}
    }

}
