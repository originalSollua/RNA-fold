import java.util.ArrayList;
public class Folder {
	int best_energy = 0;
	public static void main(String[]args)
	{
		
		
		//String rna = "UGCCUGGCA";
		//String rna = "CUUCAUCAAGGAAAUGAC";
		//String rna = "GUCACAUGCAUUUUGAAAGGCUACUAGA";
		//String rna = "GGACUAGCGGAGGCUAGUCC";
		String rna = "GGAUCGCAUUUGGACUUCUGCCCGGCACCACGGUCGGAUC";
		//String rna = "GACUGUGACUGGUAAAACUCUAGUACUAUGGAUUUGUAUC";
		//String rna = "UGCCUGGCGGCCGUAGCGCGCGGUGGUCCCACCUGACCCCAUGCCGAACUCAGAAGUGAAACGCCGUAGCGCCGAUGGUAGUGUGUGGGGUCCCCAUGCGAGAGUAGGGAACUGCCAGGCAU";
		Folder binder = new Folder();
		int l = rna.length();
		int[][] stem_mat = new int[l][l];
		for(int i = 0; i < l; i++)
		{
			for(int j = 0; j < l; j++)
			{
				char row = rna.charAt(i);
				char col = rna.charAt(j);
				if((row == 'C' && col =='G')|| col == 'C' && row == 'G' )
				{
					stem_mat[i][j] = 3;
				}
				else if((row == 'A'&& col == 'U')|| row == 'U' && col=='A')
				{
					stem_mat[i][j]= 2;
				}
				else if(( row == 'G'&& col == 'U')||(row=='U'&& col=='G'))
				{
					stem_mat[i][j] = 1;
				}
				else
				{
					stem_mat[i][j] = 0;
				}
			}			
		}
		//make matrix finished
		// identify stems
		
		ArrayList<ArrayList<Pair>> mega_list = new ArrayList<ArrayList<Pair>>();
		//list of all the possible stems.
		
	
		for(int i = 0; i < l; i++)
		{
			for(int j = 0; j < l; j++)
			{
				if(stem_mat[i][j] !=0 && i+1 < l && j+1 < l && stem_mat[i+1][j+1] !=0)
				{
					int temp_i = i;
					int temp_j = j;
					ArrayList<Pair> Stem = new ArrayList<Pair>();
					while(i < l && j < l && stem_mat[i][j] !=0)
					{
						Stem.add(new Pair(i, j, stem_mat[i][j]));
						stem_mat[i][j] = 0;
						i++;
						j++;
					}
					mega_list.add(Stem);
							
					i = temp_i;
					j = temp_j;
				}
			}
		}
			
		// now that list is generated, sort it by energy of each pairing. 
		//get the sum of a mega_list element's associated list ofg pairs strength
		//put it into the list @ an index such that all elements after it have more strength
		ArrayList<ArrayList<Pair>> sorted = new ArrayList<ArrayList<Pair>>();
		int posfinder[] = new int[mega_list.size()];
		// index 0 in posfinder corresponds to the energy sum of index 0 in mega_list
		for(int i = 0; i < mega_list.size(); i++)
		{
			for(int j = 0; j < mega_list.get(i).size(); j++)
			{
				posfinder[i] = posfinder[i]+mega_list.get(i).get(j).energy;
			}
		
		}

		for(int j = 0; j < posfinder.length; j++)
		{
			int max = 0;
			int index = 0;
			for(int i = j; i < posfinder.length; i++)
			{
				// find biggest value
				if(posfinder[i] > max)
				{
					max = posfinder[i];
					index = i;
				}
			}
			
			int temp = posfinder[j];
			posfinder[j] = max;
			posfinder[index] = temp;
			
			ArrayList<Pair> tepid = mega_list.set(index, mega_list.get(j));
			tepid = mega_list.set(j, tepid);
		}	
	
	
		// good heavens, look at the time
		// ITS TIME TO DUEL
		// AND BY DUEL, I MEAN FOLD RNA
		//pic a stem(in order, down the list
		System.out.println("Folding RNA...");
		double st = System.nanoTime();
		for(int i = 0; i < mega_list.size(); i++)
		{
			ArrayList<Pair> pick = mega_list.get(i);
			int folded_energy = 0;
			System.out.println("Folding Stem "+ i + " of "+ mega_list.size());
			folded_energy = binder.flatbread(mega_list, pick);
			if(folded_energy >= binder.best_energy)	
			{
				binder.best_energy = folded_energy;
			}
			//System.out.println(folded_energy);
		}
		double endt = System.nanoTime();
		 double need = 1000000000; //divisor for seconds i think
		 double est = (endt-st) / need;
		System.out.println("Strand length: "+ l + " Best energy: "+ binder.best_energy +" found in "+ est + " sec.");
		
	//here endeth main	
	}
	public int flatbread(ArrayList<ArrayList<Pair>> smaller, ArrayList<Pair> curent)
	{
		ArrayList<ArrayList<Pair>>even_smaller = new ArrayList<ArrayList<Pair>>();
		boolean good = true;
		for(int i = 0; i < smaller.size(); i++)
		{
			for(int j = 0; j< smaller.get(i).size(); j++)
			{
				int first = smaller.get(i).get(j).first_half;
				int second = smaller.get(i).get(j).second_half;
				for(int k = 0; k < curent.size(); k++)
				{
					int testf =curent.get(k).first_half;
					int tests = curent.get(k).second_half;
					if(testf == first || testf == second || tests == first || tests == second)
					{
						good = false;
					}
				}
			}
			if(good)
			{
				even_smaller.add(smaller.get(i));
			}
			good = true;
		}
		// the above should compare all the base pairs in the passed list to the ones in the curent stem. if any pairs are in both, the stem is not added to even_smaller
		if(even_smaller.isEmpty())
		{
			return 0;
		}
		else
		{
			int []energy = new int[even_smaller.size()];
			for(int g = 0; g < even_smaller.size(); g++)
			{
				energy[g] =0;
				for(int p = 0; p < even_smaller.get(g).size(); p++)
				{
					energy[g] = energy[g]+even_smaller.get(g).get(p).energy;
				}
				
			}
			for(int f = 0; f < energy.length; f++)
			{
				energy[f] = energy[f]+flatbread(even_smaller, even_smaller.get(f));
			}
			int max = 0;
			for(int f = 0; f < energy.length; f++)
			{
				if(max < energy[f])
				{
					max = energy[f];
				}
			}
			return max;
		}
		
	}
}

