//Project 3

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/*FLOW:
	-take in 3 command line arguments: alg type, size of page (max pages = 64000/pagesize), input file name
	-allocate physical mem space 2000/pagesize rounding down = # of frames
	-read in text file into object Page that has PID, address, read or write (write=true or false)
	-decide which algorithm to use (switch statement like before?)
	-execute each line
	-print page#, PID, frame#, if page fault, if frame needed to be written, virtual to physical translation

  ALGORITHMS:
  	Optimal
  		Read whole sample. Decide order of replace (HOW?) in advance and put that order in an array.  At runtime just execute in order.
  	FIFO
  		replace frames in order (just keep a counter of the next frame to replace, increment every time a frame is replaced)
  	LRU
		find the frame that wasn't accessed for the longest time.  (need some array of number and move the one that was accessed
		to the front. Array list prob will work best.  Search for index of frame, remove it from the list, add it to the front)
	Second Chance		
		FIFO but with an extra reference bit.  when you want to replace, check the reference bit.  If 1 set to 0 move on to next victim,
		if 0 replace.  When used set bit to 1.
	Enhanced Second Chance
		2 reference bits this time.  When you want to replace a page, look for (x,0), if (1,0) set to (0,0), if (0,0) replace.  If none 
		are "clean" then repeat looking for 0,1.
	Custom
		?
*/

public class Pager	{
	public static void main(String args[])	{
		int pageSize = Integer.parseInt(args[1]);
		int numPages = 65536/pageSize; //this may not be relevant
		int numFrames = 2048/pageSize;
		Frame[] frames = new Frame[numFrames]; //this will be my physical memory
		for(int i=0; i < numFrames; ++i) {
			frames[i] = new Frame();
		}
		ArrayList<MemoryAccess> queue = new ArrayList<MemoryAccess>();


		File input = new File(args[2]);
		readFromFile(input, queue); //populate queue with data from the file
		String algorithm = args[0];
		switch(algorithm)	{
			case "fifo":
				fifo(queue, frames, pageSize);
				break;
			case "lru":
				lru(queue, frames, pageSize);
				break;
			case "sc":
				sc(queue, frames, pageSize);
				break;
			case "esc":
				esc(queue, frames, pageSize);
				break;
			case "optimal":
				optimal(queue, frames, pageSize);
				break;
			case "custom":
				custom(queue, frames, pageSize);
				break;
			default:
				System.out.println("Invalid algorithm type");
		}
	}

	static void readFromFile(File f, ArrayList<MemoryAccess> queue)	{
		int pid, address;
		boolean write;
		try	{
			Scanner read = new Scanner(f);
			read.useDelimiter("(, *)|\\n");	// matches a comma and zero or more spaces, or a newline.

			while(read.hasNext())	{
				pid = read.nextInt();
				address = read.nextInt();
				if(read.next().equals("W"))
					write = true;
				else
					write = false;
				MemoryAccess p = new MemoryAccess(pid, address, write);
				queue.add(p);
			}

			read.close();
		}
		catch(FileNotFoundException e)	{
			System.out.println("File not found");
		}
	}
	
	// returns the number of the frame corresponding to the given page number and pid, or -1 if not found.
	static int find_frame(Frame[] frames, int pid, int page_number) {
		for(int i = 0; i < frames.length; ++i) {
			Frame f = frames[i];
			if(f.pid == pid && f.page_number == page_number)
				return i;
		}
		return -1;	// frame not found
	}
	
	static void fifo(ArrayList<MemoryAccess> queue, Frame[] frames, int pageSize)	{
		int index_to_replace = 0;
		int num_page_faults = 0;
		int num_disk_accesses = 0;
		for(MemoryAccess access : queue) {
			int virtual_address = access.address;
			int page_number = virtual_address / pageSize;
			int pid = access.pid;
			boolean write = access.write;
			
			// check if the page is in physical memory
			int frame_number = find_frame(frames, pid, page_number);
			if(frame_number == -1) {
				// page is not in memory.
				
				// page fault.
				++num_page_faults;
				if(!write) {
					// read from disk.
					++num_disk_accesses;
				}
				
				frame_number = index_to_replace;
				Frame frame = frames[frame_number];
				
				if(frame.page_number == -1) {
					// no replacement
					frame.pid = pid;
					frame.page_number = page_number;
					
					System.out.println("loaded page #" + page_number + " of process #" + pid + " to frame #" + frame_number + " with no replacement.");
				} else {
					// must replace existing page
					
					boolean frame_was_dirty = false;
					
					if(frame.dirty) {
						// page is dirty. Must write to disk
						++num_disk_accesses;
						frame.dirty = false;
						frame_was_dirty = true;
					}
					
					frame.pid = pid;
					frame.page_number = page_number;
					
					System.out.println("loaded page #" + page_number + " of process #" + pid + " to frame #" + frame_number + " with replacement.");
					if(frame_was_dirty)
						System.out.println("\tNeeded to write frame #" + frame_number + " to disk");
				}
				
				index_to_replace++;
				if(index_to_replace >= frames.length)
					index_to_replace = 0;
			} else {
				// page is already in physical memory.
				System.out.println("no page fault. accessed frame #" + frame_number);
			}
			
			if(write) {
				// mark as dirty
				frames[frame_number].dirty = true;
			}
			
			int physical_address = (frame_number * pageSize) + (virtual_address % pageSize);
			
			System.out.println("\tVirtual Address: " + virtual_address + " -> Physical Address: " + physical_address);
		}
		
		System.out.println("Number of page faults: " + num_page_faults + ". Number of disk accesses: " + num_disk_accesses);
	}
	
	static void lru(ArrayList<MemoryAccess> queue, Frame[] frames, int pageSize)	{
		
	}
	static void sc(ArrayList<MemoryAccess> queue, Frame[] frames, int pageSize)	{
		
	}
	static void esc(ArrayList<MemoryAccess> queue, Frame[] frames, int pageSize)	{
		
	}
	static void optimal(ArrayList<MemoryAccess> queue, Frame[] frames, int pageSize)	{
		
	}
	static void custom(ArrayList<MemoryAccess> queue, Frame[] frames, int pageSize)	{
		
	}
}
