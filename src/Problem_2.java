import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;


public class Problem_2 {

	/*
	 * main function
	 */
	public static void main(String[] args) throws IOException {
		
		Scanner scan = new Scanner(System.in);
		
		String in_file;
		System.out.println("Enter file name: ");
		in_file = scan.nextLine();
		
		int minValue = heterogenityCalc(in_file);
		
		
		System.out.println("Enter output file: ");
		String outFile = scan.nextLine();
		
		printToFile(minValue, outFile);
		
		
	}

	/*
	 * Prints the minimum value V to the specified out file
	 */
	private static void printToFile(int minValue, String outFile) throws FileNotFoundException {
		// TODO Auto-generated method stub
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File(outFile)));
		pw.println(minValue);
		pw.close();
	}

	/* 
	 * reads the input file, and creates three arrays. One for the ratings, 
	 * one for the difference between the ratings, and one for the indexes used when 
	 * calculating that difference. 
	 * 
	 * THe function does make an assumption that after sorting the data, the smallest jumps will
	 * occur with data that is next to each other
	 */
	private static int heterogenityCalc(String inFile) throws IOException {
		
		BufferedReader bfr = new BufferedReader(new FileReader(new File(inFile)));
		String line = bfr.readLine();
		/*
		 * retrieves the P and K values from the data
		 */
		String[] values = line.split(" ");
		int numP = Integer.parseInt(values[0]);
		int numK = Integer.parseInt(values[1]);
		
		line = bfr.readLine();
		values = line.split(" ");
		
		bfr.close();
		
		/*
		 * parses all the ratings into Integers and stores into an array list
		 */
		ArrayList<Integer> ratings = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
			ratings.add(Integer.parseInt(values[i]));
		}
		
		Collections.sort(ratings); // sorts ratings
		
		ArrayList<Integer> min_diff = new ArrayList<>();
		ArrayList<int[]> indexes = new ArrayList<>();
		
		/*
		 * Calculates ALL the various min_differnces with the values next to each other, 
		 * keeping track of the indexes used in a different array. Ex. if ratings was {1,2,3,4,5}
		 * , min_diff would be {1,2,2} and indexes would be {[0,1,2],[1,2,3],[3,4,5]}
		 */
		for (int i = 0; i < ratings.size()-2; i++) {
			min_diff.add(ratings.get(i+2) - ratings.get(i));
			indexes.add(new int[] {i, i + 1, i + 2});
		}
		
		/* 
		 * keeps tracks of the indexes used in making the groups
		 */
		ArrayList<Integer> used_indexes = new ArrayList<>();
		
		// finds the min and adds the indexes used for that min to the group
		
		int min = Collections.min(min_diff);
		int current_index = min_diff.indexOf(min);
		used_indexes.add(indexes.get(current_index)[0]);
		used_indexes.add(indexes.get(current_index)[1]);
		used_indexes.add(indexes.get(current_index)[2]);
		
		
		/*
		 * then repeats, and finds the next min, and if those indexes are 
		 * already used, then reduces the loop value as a new min has not been found, and 
		 * removes the min and the corresponding indexes from the arrays. The final min value will 
		 * largest value V that will be returned.
		 */
		
		for (int i = 1; i < numK; i++) {
			min = Collections.min(min_diff);
	
			current_index = min_diff.indexOf(min);
			boolean index_found = false;
			
			for (int index : indexes.get(current_index)) {
				if (used_indexes.contains(index)) {
					index_found = true;
					break;
				}
			}
			
			if (index_found) {
				i--;
			} else {
				for (int index : indexes.get(current_index)) {
					used_indexes.add(index);
				}
			}
			
			min_diff.remove(current_index);
			indexes.remove(current_index);
		}
		
		
		return min;
		
	}

}
