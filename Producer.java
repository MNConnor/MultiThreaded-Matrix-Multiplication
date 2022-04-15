import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;


public class Producer implements Runnable
{
	String ProducerName;
	int[][] A;
	int[][] B;
	int[][] C;
	SharedBuffer SB;
	int ProducedItems;
	int TotalSleep;
	
	public Producer(String Name, SharedBuffer SB, int[][] A, int[][] B)
	{
		ProducerName = Name;
		this.SB = SB;
		this.A = A;
		this.B = B;
		ProducedItems = 0;
	}

	public int[][] getSubColumns(int lowB, int highB, int highA)
	{
		int[][] FlippedColumns = MatrixManager.getColumns(B);
		int[][] subB = new int[highB-lowB][FlippedColumns[0].length];
		// GETTING SUB COLUMNS
		for(int Row = 0; Row <= subB.length-1; Row++)
		{
			for(int Column = 0; Column <= FlippedColumns[0].length-1; Column++)
			{
				subB[Row][Column] = FlippedColumns[lowB][Column];
			}
			lowB++;
		}
		return subB;
	}
	
	public int[][] getSubRows(int lowA, int highA)
	{
		int[][] subA = new int[highA-lowA][A[0].length];

		for(int Row = 0; Row <= subA.length-1; Row++)
		{
			for(int Column = 0; Column <= subA[0].length-1; Column++)
			{
				subA[Row][Column] = A[lowA][Column];
			}
			lowA++;
		}
		return subA;
	}

	public void run() 
	{
		System.out.println(ProducerName + " has started.");
		int ARowCount = A.length;
		int BColCount = B[0].length;
		
		int SplitRows = ARowCount / Config.SplitSize;
		int SplitCols = BColCount / Config.SplitSize;
		
		// Increase split size to 1 if split size is greater than row count or column count which would return a decimal
		if(SplitRows == 0)
		{
			SplitRows = 1;
		}
		if(SplitCols == 0)
		{
			SplitCols = 1;
		}
		
		int lowA = 0;
		int lowB = 0;
		int highA = SplitRows;
		int highB = SplitCols;
		
		LinkedList<WorkItem> WorkItems = new LinkedList<WorkItem>();
		while(true)
		{
			for(int Row = 0; Row <= (BColCount/SplitCols)-1; Row++) 
			{
				// if we're on the last loop and still rows/columns left, add them to last round. 
				if(Row == ((BColCount/SplitCols)-1)) 
				{
					if(B[0].length % Config.SplitSize == 1)
					{
						highB = B[0].length;
						System.out.println("adding column");
					}
				}
				
				if(highA == A.length-1) 
				{
					if(A.length % Config.SplitSize == 1)
					{
						highA = A.length;
					}
				}
				
				System.out.println("Producer puts row " + lowA + "-" + highA + " of matrix A and column " + lowB + "-" + highB + " of B to buffer");
				int[][] subB = getSubColumns(lowB, highB, highA);
				int[][] subA = getSubRows(lowA, highA);
				WorkItem NewWorkItem = new WorkItem(subA, subB, lowA, highA, lowB, highB);
				SB.put(NewWorkItem, ProducerName);
				ProducedItems++;
				WorkItems.add(NewWorkItem);
				highB += SplitCols;
				lowB += SplitCols;
				int SleepTime = ThreadLocalRandom.current().nextInt(0, Config.MaxProducerSleepTime + 1);
				TotalSleep += SleepTime;
				
				try
				{
				    Thread.sleep(ThreadLocalRandom.current().nextInt(0, Config.MaxProducerSleepTime + 1));
				}
				catch(InterruptedException ex)
				{
				    Thread.currentThread().interrupt();
				}
			}
			highB = SplitCols;
			lowB = 0;
			lowA += SplitRows;
			highA += SplitRows;

			if(highA > A.length)
			{
				break;
			}
		}
		
		// Wait for all WorkItems to finish calculating
		while(true)
		{
			boolean AllDone = true;
			for(WorkItem W : WorkItems)
			{
				if(W.done == false)
				{
					AllDone = false;
				}
			}
			if(AllDone == false)
			{
				continue;
			}
			else
			{
				break;
			}
		}	
		
		// Create final Matrix C
		C = new int [A.length][B[0].length];
		
		// Calculate the final matrix
		for(WorkItem W : WorkItems)
		{
			for(int Row = W.lowA; Row <= W.highA-1; Row++)
			{
				for(int Column = W.lowB; Column <= W.highB-1; Column++)
				{
					C[Row][Column] = W.subC[Row-W.lowA][Column-W.lowB];
				}
			}
		}
		System.out.println("Producer successfully assembly all the results from consumer threads");
	}
	
	synchronized public int[][] getFinalMatrix()
	{
		return C;
	}
}
