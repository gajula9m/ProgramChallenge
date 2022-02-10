import java.io.*;
import java.util.*;

public class Problem_1 {
	
	static ArrayList<Room>[][] switches;
	static boolean[][] lightOn;
	static boolean[][] roomVisited;
	
	/* 
	 * main function
	 */
	public static void main (String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		
		//input file location
		System.out.println("Enter input file: ");
		String inFile = scan.nextLine();
		
		// read File
		readFile(inFile);
		
		// the top left room is already on, and that is our starting point
		lightOn[0][0] = true;
		navigate(0,0);
		
		// number of lights on
		int lightsOn = countLights();
		
		// the output file location
		System.out.println("Enter output file: ");
		String outFile = scan.nextLine();
		
		
		// write number of lights on to output file
		writeFile(outFile, lightsOn);
	}
	
	/*
	 * writes the information to the output file
	 */
	private static void writeFile(String writeFile, int lightsOn) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File(writeFile)));
		pw.println(lightsOn);
		pw.close();
	}
	
	/*
	 * uses the lightsOn array and counts the number of lights that
	 * are switched on
	 */
	private static int countLights() {
		
		int lightsOn = 0;
		
		for (boolean[] row : lightOn) {
			for (boolean light: row) {
				if (light) {
					lightsOn++;
				} 	
			}
		}
		
		return lightsOn;
	}
	
	/*
	 * navigates through the barn, and while navigating, turns on the switches that
	 * are located in the room. And then recursively, if possible, goes to that room 
	 * turns on those lights
	 */
	private static void navigate(int roomX, int roomY) {
		/*
		 * saves time by not revisiting rooms where switches were turned on
		 */
		if (!roomVisited[roomX][roomY]) {
			// set room visited to true
			roomVisited[roomX][roomY] = true;
			for (Room lightSwitch: switches[roomX][roomY]) {
				if (!lightOn[lightSwitch.x][lightSwitch.y]) {
					//turn on light if not on
					lightOn[lightSwitch.x][lightSwitch.y] = true;
					//check if light switch can be accessed 
					if (canBeReached(lightSwitch.x, lightSwitch.y)) {
						// if the room is accessible, then, navigate that room
						navigate(lightSwitch.x, lightSwitch.y);
					}
				}
			}
			
			// if neighboring rooms are turned on, then navigate those rooms too.
			if (lightIsOn(roomX + 1, roomY)) {
				navigate(roomX + 1, roomY);
			}
			if (lightIsOn(roomX - 1, roomY)) {
				navigate(roomX - 1, roomY);
			} 
			if (lightIsOn(roomX, roomY + 1)) {
				navigate(roomX, roomY + 1);
			} 
			if (lightIsOn(roomX, roomY - 1)) {
				navigate(roomX, roomY - 1);;
			}
		}
		
	}
	
	/*
	 *  Checks if a specific rooms lights are on
	 */
	public static boolean lightIsOn(int roomX, int roomY) {
		return roomX >= 0 && roomX < lightOn.length && roomY >= 0 && roomY < lightOn[roomX].length 
				&& lightOn[roomX][roomY];
	}
	
	/*
	 *  Checks if a specific room has been visited already
	 */
	public static boolean roomVisited(int roomX, int roomY) {
		return roomX >= 0 && roomX < roomVisited.length && roomY >= 0 && roomY < roomVisited[roomX].length 
				&& roomVisited[roomX][roomY];
	}
	
	/*
	 *  Checks if a room has neighboring rooms with lightsOn and has been visited.
	 *  If so, then the room can be reached and returns true.
	 */
	private static boolean canBeReached(int roomX, int roomY) {
		if (lightIsOn(roomX + 1, roomY) && roomVisited(roomX + 1, roomY)) {
			return true;
		} else if (lightIsOn(roomX - 1, roomY) && roomVisited(roomX - 1, roomY)) {
			return true;
		} else if (lightIsOn(roomX, roomY + 1) && roomVisited(roomX, roomY + 1)) {
			return true;
		} else if (lightIsOn(roomX, roomY - 1) && roomVisited(roomX , roomY - 1)) {
			return true;
		}
		
		return false;
	}
	/*
	 * reads the file and declares the switches ArrayList and the lightsOn and
	 * roomVisited boolean arrays with the given grid Size. Then reads through 
	 * all the lines and assigns each room with the switches located in that room
	 */
	private static void readFile(String inFile) throws IOException {
		BufferedReader bfr = new BufferedReader(new FileReader(inFile));
		
		String[] values = bfr.readLine().split(" ");
		int gridSize = Integer.parseInt(values[0]);
		int numLines = Integer.parseInt(values[1]);
		
		switches = new ArrayList[gridSize][gridSize];
		lightOn = new boolean[gridSize][gridSize];
		roomVisited = new boolean[gridSize][gridSize];
		
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				// each room consists of switches that access other Rooms
				switches[i][j] = new ArrayList<Room>();
			}
		}
		
		for (int i = 0; i < numLines; i++) {
			values = bfr.readLine().split(" ");
			int roomX = Integer.parseInt(values[0]) - 1;
			int roomY = Integer.parseInt(values[1]) - 1;
			int switchX = Integer.parseInt(values[2]) - 1;
			int switchY = Integer.parseInt(values[3]) - 1;
			switches[roomX][roomY].add(new Room(switchX,switchY));
		}
		bfr.close();
	}
	
	/*
	 * a class that stores the coordinates for the room
	 */
	static class Room {
		public int x,y;
		public Room(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

}
