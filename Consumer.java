import java.util.concurrent.ThreadLocalRandom;

public class Consumer implements Runnable 
{
	String ConsumerName;
	SharedBuffer SB;
	boolean Running;
	int ConsumedItems;
	int TotalSleep;
	
	public Consumer(String Name, SharedBuffer SB)
	{
		this.ConsumerName = Name;
		this.SB = SB;
		Running = true;
		ConsumedItems = 0;
	}

	public void Stop()
	{
		Running = false;
		Thread.currentThread().interrupt();
		SB.close();
	}
	
	public void Multiply(WorkItem W)
	{
		for(int ARow = 0; ARow <= W.subA.length-1; ARow++)
		{
			for(int BRow = 0; BRow <= W.subB.length-1; BRow++)
			{
				int Total = 0;
				for(int Column = 0; Column <= W.subA[0].length-1; Column++)
				{
					Total += W.subA[ARow][Column] * W.subB[BRow][Column]; // Multiple the value on each column and sum them together
				}
				W.subC[ARow][BRow] = Total; // Set the sub C to the dot product of subA and subB
			}
		}
	}
	public void run() 
	{
		System.out.println(ConsumerName + " has started.");
		while(Running)
		{			
			try
			{
				WorkItem WI = (WorkItem) SB.get(ConsumerName);
				if(!Running) // End the loop if no longer running. 
				{
					break;
				}
				System.out.println(ConsumerName + " gets row " +WI.lowA+"-"+WI.highA + " of matrix A and column "+WI.lowB+"-"+WI.highB+" from buffer");
				Multiply(WI);
				System.out.println();
				System.out.println(ConsumerName + " Finishes calculating\n" + MatrixManager.printMatrix(WI.subA) + "x\n" + MatrixManager.printMatrix(MatrixManager.getColumns(WI.subB)) + "=\n" + MatrixManager.printMatrix(WI.subC));
				WI.done = true; // The WorkItem is complete
				ConsumedItems++;
				int SleepTime = ThreadLocalRandom.current().nextInt(0, Config.MaxConsumerSleepTime + 1);
				TotalSleep += SleepTime;
				Thread.sleep(SleepTime);
			}
			catch(InterruptedException ex)
			{
			    Thread.currentThread().interrupt();
			    break;
			}
		}
	}
}
