public class MemoryAccess	{
	int pid;
	int address;
	boolean write;

	public MemoryAccess(int pid, int address, boolean write){
		this.pid = pid;
		this.address = address;
		this.write = write;
	}
}