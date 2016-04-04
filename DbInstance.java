import com.sleepycat.db.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class DbInstance
{
    	private static final String DB_TABLE = "/tmp/edcarter/db_table.db";
    	private static final String DB_TABLE_PATH = "/tmp/edcarter/";
	private static Database instance;

	public static Database getInstance(String dbType) {
		if (instance == null) instance = constructInstance(dbType);
		return instance;
	}
		
	public static void deleteInstance() {
		try {
			DatabaseConfig dbConfig = new DatabaseConfig();
			Database my_table = new Database(DB_TABLE, null, dbConfig);;
			my_table.remove(DB_TABLE,null,null);
			File my_file = new File(DB_TABLE);
			my_file.delete();
			instance = null;
		} catch (Exception ex) {
			System.out.println("Unable to delete db");
		}
	}

	public static void closeInstance() {
		try {
			if (instance != null) instance.close();
		} catch (DatabaseException ex) {
			System.out.println("Unable to close database");
		}
	}

	private static Database constructInstance(String dbType) {
		// Create the database object.
            	// There is no environment for this simple example.
            	DatabaseConfig dbConfig = new DatabaseConfig();
            	if (dbType.equals("1")) {
                	dbConfig.setType(DatabaseType.BTREE);
            	} else if (dbType.equals("2")) {
                	dbConfig.setType(DatabaseType.HASH);
            	} else if (dbType.equals("3")) {
                	//dbConfig.setType(DatabaseType.INDEX);
            	} else {
                	System.err.println("Invalid database type specified.");
            	}
            	dbConfig.setAllowCreate(true);

           	if (Files.notExists(Paths.get(DB_TABLE_PATH))) {
                	File dir = new File(DB_TABLE_PATH);
		        dir.mkdir();
		}
		try {
                	return new Database(DB_TABLE, null, dbConfig);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
