import com.sleepycat.db.*;

public class DbInstance
{
	private static Database instance;

	public static void setInstance(Database db) {
		instance = db;
	}

	public static Database getInstance() {
		if (instance == null) throw new RuntimeException("Database instance is null");
		return instance;
	}
}
