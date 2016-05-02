public class Frame	{
	int page_number;
	int pid;
	int ref_bits;	// reference bits used by page replacement algorithm.
	boolean dirty;

	public Frame(){
		this.ref_bits = 0;
		this.pid = -1;	// no page
		this.page_number = -1;	// no page
		this.dirty = false;
	}
}