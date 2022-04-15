import java.util.LinkedList;

public class Main
{

	public static void main(String[] args)
	{
		Config.importConfig("Config.txt");

		SharedBuffer SB = new SharedBuffer(Config.MaxBuffSize);
		int[][] A = MatrixManager.generateRandomMatrix(Config.M, Config.N);
		int[][] B = MatrixManager.generateRandomMatrix(Config.N, Config.P);;
		
		System.out.println("First Matrix:");
		System.out.println(MatrixManager.printMatrix(A));
		System.out.println("Second Matrix");
		System.out.println(MatrixManager.printMatrix(B));

		System.out.println("------------------------------------------------");
		long startTime = System.nanoTime(); 

		LinkedList<Consumer> Consumers = new LinkedList<Consumer>();
		for(int i = 0; i < Config.NumConsumer; i++)
		{
			Consumer Consumer = new Consumer("Consumer" + i, SB);
			Thread ConsumerThread = new Thread(Consumer);
			ConsumerThread.start();
			Consumers.add(Consumer);
		}

		Producer Producer = new Producer("Producer1", SB, A, B);
		Thread ProducerThread = new Thread(Producer);
		ProducerThread.start();
		try
		{
			ProducerThread.join(); //Sleep until Producer Thread is done calculating the final matrix. 
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		// End Consumer threads
		for(Consumer C : Consumers)
		{
			C.Stop();
		}
		
		long SimulationTime = System.nanoTime() - startTime;

		System.out.println("------------------------------------------------");
		int[][] C = Producer.getFinalMatrix();
		System.out.println("Final Matrix:");
		System.out.println(MatrixManager.printMatrix(C));
		MatrixManager.verify(A, B, C);
		System.out.println("------------------------------------------------");
		System.out.println("PRODUCER / CONSUMER SIMULATION RESULT");
		double simTime = (double) SimulationTime/1000000;
		System.out.println("Simulation Time:\t\t\t" + String.format("%.2f", simTime) + "ms");
		int TotalConsumed = 0;
		int TotalSleeps = 0;
		for(Consumer Consumer : Consumers)
		{
			TotalConsumed += Consumer.ConsumedItems;
			TotalSleeps += Consumer.TotalSleep;
		}
		double avgTime = (double)(TotalSleeps+Producer.TotalSleep)/(TotalConsumed + Producer.ProducedItems);
		System.out.println("Average Thread Sleep Time:\t\t" + String.format("%.2f", avgTime) + "ms");
		System.out.println("Number of Producer Threads:\t\t" + "1");
		System.out.println("Number of Consumer Threads:\t\t" + Config.NumConsumer);
		System.out.println("Size of Buffer:\t\t\t\t" + Config.MaxBuffSize);
		System.out.println("Total Number of Items Produced:\t\t" + Producer.ProducedItems);
		System.out.println("\tProducer\t\t\t" + Producer.ProducedItems);

		System.out.println("Total Number of Items Consumed:\t\t" + TotalConsumed);
		for(Consumer Consumer : Consumers)
		{
			System.out.println("\t" + Consumer.ConsumerName + "\t\t\t" + Consumer.ConsumedItems);
		}
		System.out.println("Number Of Items Remaining in Buffer:\t" + SB.count);
		System.out.println("Number Of Times Buffer Was Full:\t" + SB.FullCount);
		System.out.println("Number Of Times Buffer Was Empty:\t" + SB.EmptyCount);
	}

}