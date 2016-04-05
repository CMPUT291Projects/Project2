import com.sleepycat.db.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class DbInstance
{
    private static final String DB_TABLE = "/tmp/edcarter/db_table.db";
    private static final String DB_INDEX_TABLE = "/tmp/edcarter/db_index_table.db";
    private static final String DB_TABLE_PATH = "/tmp/edcarter/";
    private static Database instance;

    public DbInstance() {}

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
        DatabaseConfig dbConfig = new DatabaseConfig();
        if (dbType.equals("1")) {
            dbConfig.setType(DatabaseType.BTREE);
        } else if (dbType.equals("2")) {
            dbConfig.setType(DatabaseType.HASH);
        } else if (dbType.equals("3")) {
            dbConfig.setType(DatabaseType.BTREE);
            //construct the secondary db
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
            //Files.delete(Paths.get("/tmp/edcarter/_db.001"));
            // EnvironmentConfig envConfig = new EnvironmentConfig();
            // envConfig.setAllowCreate(true);
            // envConfig.setInitializeCache(true);
            // envConfig.setCacheSize(1000000);
            // Environment env = new Environment(null, envConfig);

            SwapKeys keyCreator = new SwapKeys(); // Your key creator implementation.
            SecondaryConfig secConfig = new SecondaryConfig();
            secConfig.setAllowCreate(true);
            secConfig.setSortedDuplicates(true);
            secConfig.setKeyCreator(keyCreator);
            secConfig.setType(DatabaseType.BTREE);
            //SecondaryDatabase newDb = env.openSecondaryDatabase(null, "my_index_table", DB_TABLE_PATH, primaryDb, secConfig);
            SecondaryDatabase newDb = new SecondaryDatabase(DB_INDEX_TABLE, null, primaryDb, secConfig);
            return newDb;
        } catch (Exception e) {
            System.err.println("Secondary database error: " + e);
            return null;
        }
    }

    public class SwapKeys implements SecondaryKeyCreator {

        SwapKeys() {

        }

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
