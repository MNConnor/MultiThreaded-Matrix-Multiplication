import java.util.Random;

public class MatrixManager 
{
	public static int[][] generateRandomMatrix(int Rows, int Columns)
	{
		Random rand = new Random(); 
		int[][] Matrix = new int[Rows][Columns];
		
        for (int i = 0; i < Matrix.length; i++) 
        {
            for (int j = 0; j < Matrix[i].length; j++) 
            {
            	Matrix[i][j] = rand.nextInt(10);
            }
        }
		return Matrix;
	}
	
	public static int[][] verify(int[][] A, int[][] B, int[][] C)
	{
		// Creating a matrix to store the multiplication of two matrices    
		int vC[][]=new int[A.length][B[0].length];  //3 rows and 3 columns  
		int validCounter = 0;
  
        for (int i = 0; i <= A.length-1; i++) 
        { // aRow
            for (int j = 0; j <= B[0].length-1; j++)
            { // B Columns
                for (int k = 0; k <= A[0].length-1; k++) 
                { // A Columns
                	vC[i][j] += A[i][k] * B[k][j];
                }
            	if(vC[i][j] == C[i][j])
            	{
            		validCounter++;
            	}
            }
        }
		System.out.println("Verification Matrix:");
		System.out.println(MatrixManager.printMatrix(vC));
		float percentCorrect = (float)validCounter/(float)((A.length)*(B[0].length));
		System.out.println(percentCorrect*100 + "% Correct");
		
		return vC;
	}  
	
	public static String printMatrix(int[][] Matrix)
	{
		StringBuffer SB = new StringBuffer();
        for (int[] Row : Matrix) 
        {
        	SB.append("[  ");
            for (int Column : Row) 
            {
                SB.append(String.format("%-4d", Column));
            }
            SB.append("]\n");
        }
        return SB.toString();
	}
	
	public static int[][] getColumns(int[][] Matrix)
	{
		int[][] Columns = new int[Matrix[0].length][Matrix.length];
		for(int ColumnCounter = 0; ColumnCounter <= Matrix[0].length; ColumnCounter++)
		{
		    for (int row = 0; row < Matrix.length; row++)
		    {
		        for (int col = 0; col < Matrix[row].length; col++)
		        {
		            if(col==ColumnCounter) 
		            {
		                Columns[col][row] = Matrix[row][col];
		            }
		        }
		    }
		} 
		return Columns;
	}
}
