import java.io.File;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.List;

public class Config 
{
	static int M; // The number of rows of matrix A
	static int N; // The number of columns of matrix A (also the number of rows of matrix B)
	static int P; // The number of columns of matrix B
	static int MaxBuffSize; // The maximum size of the shared buffer
	static int SplitSize; // The parameter to split rows of A into multiple sub-rows and columns of B into multiple sub-columns
	static int NumConsumer; // The number of consumer threads
	static int MaxProducerSleepTime; // The maximum sleep time of the producer thread between two pairs of sub-rows of A and sub-columns of B to shared queue
	static int MaxConsumerSleepTime; // The maximum sleep time of the producer thread between two pairs of sub-rows of A and sub-columns of B to shared queue

	public static void importConfig(String filename) // Importing a scenario from a file
	{
		try
		{
			File text = new File(filename); // Create a new file using the filename requested
			Scanner SC = new Scanner(text); // Start a new scanner using that file
			
			while(SC.hasNext())
			{
				String line = SC.nextLine(); // Create a string of the current line
				String currentline[] = line.split(" = "); // Split the line at the spaces and create an array of all the values
				
				switch(currentline[0])
				{
				case "M":
					M = Integer.parseInt((currentline[1]));
					break;
				case "N":
					N = Integer.parseInt((currentline[1]));
					break;
				case "P":
					P = Integer.parseInt((currentline[1]));
					break;
				case "MaxBuffSize":
					MaxBuffSize = Integer.parseInt((currentline[1]));
					break;
				case "SplitSize":
					SplitSize = Integer.parseInt((currentline[1]));
					break;
				case "NumConsumer":
					NumConsumer = Integer.parseInt((currentline[1]));
					break;
				case "MaxProducerSleepTime":
					MaxProducerSleepTime = Integer.parseInt((currentline[1]));
					break;
				case "MaxConsumerSleepTime":
					MaxConsumerSleepTime = Integer.parseInt((currentline[1]));
					break;
				}
			}
			SC.close();
		}
		catch (Exception ex)
		{
			System.err.println("Error importing file");
			ex.printStackTrace();
		}
	}
	
	public static void printSettings()
	{
		System.out.println("M: " + M);
		System.out.println("N: " + N);
		System.out.println("P " + P);
		System.out.println("MaxBuffSize: " + MaxBuffSize);
		System.out.println("SplitSize: " + SplitSize);
		System.out.println("NumConsumer: " + NumConsumer);
		System.out.println("MaxProducerSleepTime: " + MaxProducerSleepTime);
		System.out.println("MaxConsumerSleepTime: " + MaxConsumerSleepTime);
	}
}
