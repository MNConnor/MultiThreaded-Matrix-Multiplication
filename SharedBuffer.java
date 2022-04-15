public class SharedBuffer<WorkItem>
{
	private static int BUFFER_SIZE;
	public int count, in, out;
	int EmptyCount, FullCount;
	private WorkItem[] buffer;
	boolean Open;
	public SharedBuffer(int BUFFER_SIZE) 
	{
	this.BUFFER_SIZE = BUFFER_SIZE;
	count = 0;
	in = 0;
	out = 0;
	FullCount = 0;
	EmptyCount = 0;
	Open = true;
	buffer = (WorkItem[]) new Object[BUFFER_SIZE];
	}
	
	public synchronized void close()
	{
		Open = false;
	}
	
	public synchronized void put (WorkItem item, String Name) 
	{
		while(count == BUFFER_SIZE)
		{
			try
			{
				System.out.println("The queue is full. " + Name + " is waiting.");
				FullCount++;
				wait();
			}
			catch (InterruptedException ie) {}
		}
		
		if(count == 0)
		{

		}
		
		buffer[in] = item;
		in = (in + 1) % BUFFER_SIZE;
		count++;
		
		notify();
	}
	
	public synchronized WorkItem get(String Name) 
	{
		WorkItem item;

		while (count == 0)
		{
			if(!Open)
			{
				break;
			}
			System.out.println("The queue is empty. " + Name + " is waiting.");
			EmptyCount++;
			try
			{
				wait(100);
			}
			catch (InterruptedException ie)
			{
				break;
			}
		}
		
		item = buffer[out];
		out = (out+1) % BUFFER_SIZE;
		count--;
		
		notify();
		
		return item;
	}

}
