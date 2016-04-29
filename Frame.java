public class Frame	{
	int page_number;
	int pid;
	char ref_bits;
	boolean dirty;

	public Frame(){
		this.ref_bits = 0;
		this.pid = -1;	// no page
		this.page_number = -1;	// no page
		this.dirty = false;
	}
}