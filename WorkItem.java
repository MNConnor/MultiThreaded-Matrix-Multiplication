public class WorkItem 
{
int[][] subA;
int[][] subB;
int[][] subC;
int lowA;
int highA;
int lowB;
int highB;
boolean done;
	public WorkItem(int[][] subA, int[][] subB, int lowA, int highA, int lowB, int highB)
	{
		this.subA = subA;
		this.subB = subB;
		this.lowA = lowA;
		this.highA = highA;
		this.lowB = lowB;
		this.highB = highB;
		subC = new int[subA.length][subB.length];
		done = false;
	}
	
public void setDone(boolean value)
{
	done = value;
}

public boolean getdone()
{
	return done;
}
}
