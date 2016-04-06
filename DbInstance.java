import com.sleepycat.db.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class DbInstance
{
    private static final String DB_TABLE = "/tmp/edcarter_db/db_table.db";
    private static final String DB_INDEX_TABLE = "/tmp/edcarter_db/db_index_table.db";
    private static final String DB_TABLE_PATH = "/tmp/edcarter_db/";
    private static Database instance;
    private static SecondaryDatabase index_instance;

    private DbInstance() {}

    public static Database getInstance(String dbType) {
        if (instance == null) instance = constructInstance(dbType);
        return instance;
    }

    public static SecondaryDatabase getIndexInstance(Database primaryDb) {
        if (index_instance == null) index_instance = (new DbInstance()).createIndexDatabase(primaryDb);
        return index_instance;
    }

    public static void deleteInstance(String dbType) {
        try {
            if (dbType.equals("3")) {
                if (index_instance != null) { 
                    index_instance.close();
		}
                File idbf = new File(DB_INDEX_TABLE);
                if (idbf.exists()) {
                    idbf.delete();
                }
                index_instance = null;
            }

            if (instance != null) {
                index_instance.close();
            }
            File my_file = new File(DB_TABLE);
            if (my_file.exists()) {
                my_file.delete();
            }
            instance = null;
        } catch (Exception ex) {
            System.out.println(String.format("Unable to delete db: %s", ex.getMessage()));
        }
    }

    public static void closeInstance() {
        try {
            if (index_instance != null) index_instance.close();
            if (instance != null) instance.close();
        } catch (DatabaseException ex) {
            System.out.println(String.format("Unable to close database: %s", ex.getMessage()));
        }
    }

    private static Database constructInstance(String dbType) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        if (dbType.equals("1")) {
            dbConfig.setType(DatabaseType.BTREE);
        } else if (dbType.equals("2")) {
            dbConfig.setType(DatabaseType.HASH);
        } else if (dbType.equals("3")) {
            dbConfig.setType(DatabaseType.BTREE);
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

    public SecondaryDatabase createIndexDatabase(Database primaryDb) {
        try {
            SecondaryConfig secConfig = new SecondaryConfig();
            secConfig.setAllowCreate(true);
            secConfig.setSortedDuplicates(true);
            secConfig.setKeyCreator(new SwapKeys());
            secConfig.setType(DatabaseType.BTREE);
            return new SecondaryDatabase(DB_INDEX_TABLE, null, primaryDb, secConfig);
        } catch (Exception e) {
            System.err.println("Secondary database error: " + e);
            return null;
        }
    }

    public class SwapKeys implements SecondaryKeyCreator {

        SwapKeys() { }

        @Override
        public boolean createSecondaryKey(SecondaryDatabase secondary,
            DatabaseEntry pkey, DatabaseEntry pdata, DatabaseEntry skey) {

            // Make a fixed-length array of last_name
            byte[] newKey = pdata.getData();
            skey.setData(newKey);
            skey.setSize(newKey.length);
            return true;
        }
    }
}
