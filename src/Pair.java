
public class Pair {

	int first_half;
	int second_half;
	// the above represent the indices that identify the pair in the matrix.
	// Halves of the base pair.
	boolean is_used;
	//this refers to if another stem is using this. part of the recursive method
	int energy;
	public Pair(int x, int y, int e)
	{
		first_half = x; 
		second_half = y;
		energy = e;
		is_used = false;
	}
	
}
