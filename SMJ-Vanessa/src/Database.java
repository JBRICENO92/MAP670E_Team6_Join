import java.util.HashMap;
import java.util.Map;

public class Database {
	String fileDir;
	Map<String, Table> tables;
	
	public Database(String fileDir) {
		this.fileDir = fileDir;
		tables = new HashMap<String, Table>();
	}
	
	public Table addTable(String tablename, String filename) {
		Table t = new Table(tablename, fileDir + "/" + filename);
		tables.put(tablename, t);
		return t;
	}
	
	public void deleteTable(String tablename) {
		tables.remove(tablename);
	}
	
	public static void main(String[] args) {
		Database d = new Database("database");
		Table t1 = d.addTable("mini_clients", "mini_clients.csv");
		Table t2 = d.addTable("mini_purchases", "mini_purchases.csv");
		long startTime = System.currentTimeMillis();
		SortMergeJoin j = new SortMergeJoin(t1, t2);
		j.join("database/joined.csv");
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.println("Duration: " + duration + " ms");
		
		long startTime0 = System.currentTimeMillis();
		SortMergeJoinThreadMain j0 = new SortMergeJoinThreadMain(t1, t2, 4, "database/joined_thread.csv");
		j0.run();
                try
                {
                    j0.join();
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
		long endTime0 = System.currentTimeMillis();
		long duration0 = endTime0 - startTime0;
		System.out.println("Total duration: " + duration0 + " ms");
                System.out.println("Partitioning duration: " + j0.duration_partition + " ms");
                System.out.println("Threads duration: " + j0.duration_threads + " ms");
                System.out.println("Combine duration: " + j0.duration_combine + " ms");
	}
}
