import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Problem_1 {
	
	private final static int NO_LIGHT = 0; // no light in room
	private final static int LIGHT = 1; // light in room but can't reach
	private final static int LIGHT_AND_ACCESS = 2; // light in room and can reach
	
	// a 2D array list of int[] that store the type of room and the switches locatedd in that room
	private static ArrayList<int[]>[][] barn; 
	
	/* main function */
	public static void main(String[] args) throws IOException{
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter input file: ");
		String in_file = scan.nextLine();
		
		readFile(in_file);
		
		int startX = 0;
		int startY = 0;
		
		navigateBarn(startX, startY);
		
		int lights = countLights();
		
		System.out.println("Enter output file: ");
		String outFile = scan.nextLine();
		
		writeFile(lights, outFile);
	}
	
	/*
	 * counts the total number of lights that have been turned on
	 */
	private static int countLights() {
		int lights = 0;
		
		for (ArrayList<int[]>[] row : barn) {
			for (ArrayList<int[]> arr : row) {
				if (arr.get(0)[0] == 2 || arr.get(0)[0] == 1) {
					lights++;
				}
			}
		}
		
		return lights;
	}
	
	/*
	 * Once a new LIGHT_AND_ACCESS room is created, it may be possible that this new room starts up 
	 * a new path, connecting the path to rooms that have been lit and not connected to a path. 
	 * This function will check if those paths have been created, and will reassign certain rooms 
	 * with just LIGHT to LIGH_AND_ACCESSS, recursively calling the navigateBarn method.
	 */
	private static void checkNeighbors(int roomX, int roomY) {
		
		int left = roomX - 1;
		int right = roomX + 1;
		int top = roomY - 1;
		int bottom = roomY + 1;
		
		
		// check if left already has a value of LIGHT, and if so, make it LIGHT_AND_ACCESS
		if (left >= 0) {
			if (barn[left][roomY].get(0)[0] == LIGHT) {
				barn[left][roomY].set(0, new int[] {LIGHT_AND_ACCESS});
				navigateBarn(left, roomY);
			}
		}
		
		// check if right already has a value of LIGHT, and if so, make it LIGHT_AND_ACCESS
		if (right < barn.length) {
			if (barn[right][roomY].get(0)[0] == LIGHT) {
				barn[right][roomY].set(0, new int[] {LIGHT_AND_ACCESS});
				navigateBarn(right, roomY);
			}
		}
		
		// check if top already has a value of LIGHT, and if so, make it LIGHT_AND_ACCESS
		if (top >= 0) {
			if (barn[roomX][top].get(0)[0] == LIGHT) {
				barn[roomX][top].set(0, new int[] {LIGHT_AND_ACCESS});
				navigateBarn(roomX, top);
			}
		}
		
		// check if bottom already has a value of LIGHT, and if so, make it LIGHT_AND_ACCESS
		if (bottom < barn.length) {
			if (barn[roomX][bottom].get(0)[0] == LIGHT) {
				barn[roomX][bottom].set(0, new int[] {LIGHT_AND_ACCESS});
				navigateBarn(roomX, bottom);
			}
		}
	}
	
	/*
	 * Navigation starts from [0,0] in the array (or 1 ,1 in the input file).
	 * Each switch in the room will be turned on and then if the room is right next to a room
	 * that connects to a path, then that room's switches will all be activated by 
	 * recursively calling the function
	 */
	private static void navigateBarn(int roomX, int roomY) {
		for (int[] switches : barn[roomX][roomY]) {
			/*
			 *  make sure switches are actually switches and not
			 *  the value of the room itself
			 */
			if (switches.length == 2) { 
				/*
				 * ignore if the switch is already on
				 */
				if (barn[switches[0]][switches[1]].get(0)[0] == LIGHT_AND_ACCESS) {
					continue;
				} else {
					/* 
					 * turn on switch
					 */
					barn[switches[0]][switches[1]].set(0, new int[] {LIGHT});
					
					/*
					 * if connected to path, make it LIGHT_AND ACCESSIBLE SWITCH
					 * and check that lights switches
					 */
					if (pathAvailable(switches[0], switches[1])) {
						barn[switches[0]][switches[1]].set(0, new int[] {LIGHT_AND_ACCESS});
						checkNeighbors(switches[0], switches[1]);
						navigateBarn(switches[0], switches[1]);
					}
				}
			}
		}
		
		
	}
		
	/*
	 * Checks if their is a path that is next to a certain room, so that the current room
	 * become a part of that path
	 */
	private static boolean pathAvailable(int roomX, int roomY) {
		
		int left = roomX - 1;
		int right = roomX + 1;
		int top = roomY - 1;
		int bottom = roomY + 1;
		
		if (left >= 0) {
			if (barn[left][roomY].get(0)[0] == LIGHT_AND_ACCESS) {
				return true;
			}
		}
		
		if (right < barn.length) {
			if (barn[right][roomY].get(0)[0] == LIGHT_AND_ACCESS) {
				return true;
			}
		}
		
		if (top >= 0) {
			if (barn[roomX][top].get(0)[0] == LIGHT_AND_ACCESS) {
				return true;
			}
		}
		
		if (bottom < barn.length) {
			if (barn[roomX][bottom].get(0)[0] == LIGHT_AND_ACCESS) {
				return true;
			}
		}
		
		
		return false;
	}
	
	
	/* 
	 * Reads the input file and creates the appropriate sized barn array
	 * Then enters all information about the switches in each room
	 */
	private static void readFile(String in_file) throws IOException {
		BufferedReader bfr = new BufferedReader(new FileReader(new File(in_file)));
		String line = bfr.readLine();
		
		String[] values = line.split(" ");
		int gridSize = Integer.parseInt(values[0]);
		int numLines = Integer.parseInt(values[1]);
		
		barn = new ArrayList[gridSize][gridSize];
		
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				barn[i][j] = new ArrayList<int[]>();
				barn[i][j].add(new int[] {NO_LIGHT});
			}
		}
		
		barn[0][0].set(0, new int[] {LIGHT_AND_ACCESS});
		
		
		for (int i = 0; i < numLines; i++) {
			line = bfr.readLine();
			values = line.split(" ");
			int switchRoomX = Integer.parseInt(values[0]) - 1;
			int switchRoomY = Integer.parseInt(values[1]) - 1;
			int lightRoomX = Integer.parseInt(values[2]) - 1;
			int lightRoomY = Integer.parseInt(values[3]) - 1;
			
			barn[switchRoomX][switchRoomY].add(new int[] {lightRoomX, lightRoomY});
		}
		
	}

	/*
	 * Writes the information to the designated output file
	 */
	private static void writeFile(int lightsOn ,String outFile) throws IOException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File(outFile)));
		pw.println(lightsOn);
		pw.close();
	}
}

