public class Page{
	int pid;
	int address;
	boolean write;

	public Page(int pid, int address, boolean write){
		this.pid = pid;
		this.address = address;
		this.write = write;
	}
}