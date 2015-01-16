package nivance.dbpapi.shell;

public interface DBPShell {

	public void version();
	public void showkeys();
	public void showinfo(String[] keys);
	public void setinfo(String key, String value);
	public void status();
	public void testinsert(String messageid);
	
}
