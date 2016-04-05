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

    private static final String DB_TABLE = "/tmp/edcarter/db_table.db";
    private static final String DB_INDEX_TABLE = "/tmp/edcarter/db_index_table.db";
    private static final String DB_TABLE_PATH = "/tmp/edcarter/";

    private static final int NO_RECORDS = 100000;

    public SetupDB() {

    }

    public void createDb(String dbType) {

        try {

            Database my_table = DbInstance.getInstance(dbType);
            if (dbType.equals("3")) { // Create secondary database
                DbInstance dbi = new DbInstance();
                SecondaryDatabase my_index_table = dbi.createIndexDatabase(my_table);
            }
            /* populate the new database with NO_RECORDS records */
            populateTable(my_table,NO_RECORDS);
            my_table.sync();

            System.out.println(String.format("100000 records inserted into database %s", my_table.getDatabaseFile()));

        }
        catch (Exception e1) {
            System.err.println("Creation failed: " + e1.toString());
        }
    }

    /*
    *  To poplate the given table with nrecs records
    *
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


                /* to generate a data string */
                range = 64 + random.nextInt( 64 );
                s = "";
                for ( int j = 0; j < range; j++ )
                s+=(new Character((char)(97+random.nextInt(26)))).toString();

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
