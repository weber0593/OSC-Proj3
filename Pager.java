//Project 3

/*FLOW:
	-take in 3 command line arguements: alg type, size of page (max pages = 64000/pagesize), input file name
	-allocate physical mem space 2000/pagesize rounding down = # of frames
	-read in text file into object Page that has PID, address, read or write (write=true or false)
	-decide which algorithm to use (switch statement like before?)
	-execute each line
	-print page#, PID, frame#, if page fault, if frame needed to be written, virtual to physical translation

  ALGORITHMS:
  	Optimal
  		Read whole sample. Decide order of replace (HOW?) in advance and put that order in an array.  At runtime just execute in order.
  	FIFO
  		replace frames in order (just keep a counter of the next frame to replace, increment everytime a frame is replaced)
  	LRU
		find the frame that wasnt accessed for the longest time.  (need some array of number and move the one that was accessed 
		to the front. Array list prob will work best.  Search for index of frame, remove it from the list, add it to the front)
	Second Chance		
		FIFO but with an extra reference bit.  when you want to replace, check the reference bit.  If 1 set to 0 move on to next vitim,
		if 0 replace.  When used set bit to 1.
	Enhanced Second Chance
		2 reference bits this time.  When you want to replace a page, look for (x,0), if (1,0) set to (0,0), if (0,0) replace.  If none 
		are "clean" then repeat looking for 0,1.
	Custom
		?
*/

public class Pager{
	public static void main(String args[]){
		int pageSize = args[2];
		int numPages = 64000/pageSize; //this may not be relevent
		int numFrames = 2000/pageSize;
		int frames[] = new frames[numFrames]; //this will be my physical memory
		ArrayList<Page> queue = new ArrayList<Page>(); 


		File input = new File(args[1]);
		readFromFile(input, queue); //populate queue with data from the file
		String algorithm = args[0];
		switch(algorithm){
			case "fifo":
				fifo(queue, frames[]);
				break;
			case "lru":
				lru(queue, frames[]);
				break;
			case "sc":
				sc(queue, frames[]);
				break;
			case "esc":
				esc(queue, frames[]);
				break;
			case "optimal":
				optimal(queue, frames[]);
				break;
			case "custom":
				custom(queue, frames[]);
				break;
			default:
				System.out.println("Invalid algorithm type");
		}
	}

	static void readFromFile(File f, ArrayList<Page> queue){
		int pid, address;
		char tempChar;
		boolean write;
		try{
			Scanner read = new Scanner(f);
			read.useDelimiter("(, *)|\\n");	// matches a comma and zero or more spaces, or a newline.

			while(read.hasNext()){
				pid = read.nextInt();
				address = read.nextInt();
				tempChar = read.nextChar();
				if(tempChar=='W')
					write = true;
				else
					write = false;
				Page p = new Page(pid, address, write);
				queue.add(p);
			}

			read.close();
		}
		catch(FileNotFoundException e){
			System.out.println("File not found");
		}
	}
}
