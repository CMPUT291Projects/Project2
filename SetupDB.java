/*
*  A sample program to create an Database, and then
*  populate the db with 1000 records, using Berkeley DB
*
*  Author: Prof. Li-Yan Yuan, University of Alberta
*
*  A directory named "/tmp/my_db" must be created before testing this program.
*  You may replace my_db with user_db, where user is your user name,
*  as required.
*
*  Modified on March 30, 2007 for Berkeley DB 4.3.28
*
*/
import com.sleepycat.db.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class SetupDB {

    // to specify the file name for the table
    private static final String DB_TABLE = "/tmp/my_db/db_table.db";
    private static final String DB_TABLE_PATH = "/tmp/my_db/";
    private static final int NO_RECORDS = 100000;

    /*
    *  the main function
    */
    public SetupDB() {

    }

    public void createDb(String dbType) {

        try {

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

            Database my_table = new Database(DB_TABLE, null, dbConfig);
            System.out.println(DB_TABLE + " has been created");

            /* populate the new database with NO_RECORDS records */
            populateTable(my_table,NO_RECORDS);
            System.out.println("100000 records inserted into" + DB_TABLE);

            /* cloase the database and the db enviornment */
            my_table.close();
            // /* to remove the table */
            // my_table.remove(SAMPLE_TABLE,null,null);

        }
        catch (Exception e1) {
            System.err.println("Test failed: " + e1.toString());
        }
    }


    /*
    *  To poplate the given table with nrecs records
    */
    static void populateTable(Database my_table, int nrecs ) {
        int range;
        DatabaseEntry kdbt, ddbt;
        String s;

        /*
        *  generate a random string with the length between 64 and 127,
        *  inclusive.
        *
        *  Seed the random number once and once only.
        */
        Random random = new Random(1000000);

        try {
            for (int i = 0; i < nrecs; i++) {

                /* to generate a key string */
                range = 64 + random.nextInt( 64 );
                s = "";
                for ( int j = 0; j < range; j++ )
                s+=(new Character((char)(97+random.nextInt(26)))).toString();

                /* to create a DBT for key */
                kdbt = new DatabaseEntry(s.getBytes());
                kdbt.setSize(s.length());

                // to print out the key/data pair
                // System.out.println(s);

                /* to generate a data string */
                range = 64 + random.nextInt( 64 );
                s = "";
                for ( int j = 0; j < range; j++ )
                s+=(new Character((char)(97+random.nextInt(26)))).toString();
                // to print out the key/data pair
                // System.out.println(s);
                // System.out.println("");

                /* to create a DBT for data */
                ddbt = new DatabaseEntry(s.getBytes());
                ddbt.setSize(s.length());

                /* to insert the key/data pair into the database */
                my_table.putNoOverwrite(null, kdbt, ddbt);
            }
        }
        catch (DatabaseException dbe) {
            System.err.println("Populate the table: "+dbe.toString());
            System.exit(1);
        }
    }
}
