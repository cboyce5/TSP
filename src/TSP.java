import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TSP {
	
	//variable declaration
	private static Scanner reader;
	private String readFile = "input.txt";
	private int n = 0;
	private ArrayList<Point> points;
	public static Point[] shortestRoute;
	public static double shortestDistance = Double.MAX_VALUE;
	
	//constructor
	public TSP() {
		reader = new Scanner(System.in);
		points = new ArrayList<Point>();
		shortestRoute = new Point[n];
	}
	
	//read in file function
	private void read() {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(readFile);
			Scanner in = new Scanner(fileReader);
			n = in.nextInt();
			String dummy = in.nextLine();
			int x;
			int y;
			while (in.hasNext()) {
				x = in.nextInt();
				y = in.nextInt();
				Point p = new Point(x,y);
				points.add(p);
			}
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("no such file.");
		}
	}
	
	private double distance(Point p1, Point p2) {
		return Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX()) + (p1.getY() - p2.getY())*(p1.getY() - p2.getY()));
	}
	
	//nearest neighbor implementation
	private void nearest() {
		
		//local variables
		ArrayList<Point> remainingPoints = new ArrayList<Point>(points);
		ArrayList<Point> finalPath = new ArrayList<Point>();
		Point firstPoint = remainingPoints.remove(0);
		finalPath.add(firstPoint);
		Point currentPoint = firstPoint;
		double totalDistance = 0;
		
		//loop through all possible points
		for (int i = 0; i < n-1; i++) {
			
			//local variables that help iterate and compare
			double distance = Double.MAX_VALUE;
			int index = 0;
			
			//loop through remaining points
			for (int j = 0; j < remainingPoints.size(); j++) {
				
				//if the distance is less than the current least distance, set the points and distance appropriately
				if (distance(currentPoint,remainingPoints.get(j)) <= distance) {
					distance = distance(currentPoint,remainingPoints.get(j));
					index = j;
				}
			}

			
			//setting temporary and final variables
			totalDistance += distance;
			finalPath.add(remainingPoints.get(index));
			if (remainingPoints.size() != 1) {
				currentPoint = remainingPoints.remove(index);
			}
			
		}
		
		//connecting the two end points by an edge
		totalDistance += distance(firstPoint,remainingPoints.get(0));
		
		//final outputs
		System.out.println();
		System.out.println("Final Distance: " + totalDistance);
		System.out.println("Final Path: " + finalPath);
		System.out.println();
	}
	
	private double totalPathCalc(Point[] p) {
		double total = 0;
		for (int i = 0; i < p.length-1; i++) {
			total += distance(p[i],p[i+1]);
		}
		total+= distance(p[0],p[p.length-1]);
		return total;
	}
	
	//code used from internet to generate permutations
	private void permuteGen(Point[] p, int index) {
		if(index >= p.length - 1){ 
			System.out.println();
			System.out.print("Path: ");
			System.out.print("[");
	        for(int i = 0; i < p.length - 1; i++){
	            System.out.print(p[i] + ", ");
	        }
	        if(p.length > 0) 
	            System.out.print(p[p.length - 1]);
	        System.out.println("]");
	        System.out.println("Distance: " + totalPathCalc(p));
	        System.out.println();
	        
	        if (totalPathCalc(p) <= shortestDistance) {
	        	shortestDistance = totalPathCalc(p);
	        	shortestRoute = Arrays.copyOf(p, p.length);
	        }
	        
	        return;
	    }

	    for(int i = index; i < p.length; i++){ //For each index in the sub array arr[index...end]

	        //Swap the elements at indices index and i
	        Point t = p[index];
	        p[index] = p[i];
	        p[i] = t;

	        //Recurse on the sub array arr[index+1...end]
	        permuteGen(p, index+1);

	        //Swap the elements back
	        t = p[index];
	        p[index] = p[i];
	        p[i] = t;
	    }
	}
	
	//exhaustive search implementation
	private void exhaustive() {
		Point[] pts = points.toArray(new Point[n]);
		permuteGen(pts,1);
		
		System.out.println("\nShortest Distance: " + shortestDistance);
		System.out.print("Shortest Path: [");
		for (int i = 0; i < shortestRoute.length-1; i++) {
			System.out.print(shortestRoute[i] + ", ");
		}
		if(shortestRoute.length > 0) 
            System.out.print(shortestRoute[shortestRoute.length - 1]);
		System.out.print("]");
		System.out.println();
		System.out.println();
	}
	
	//start function
	private void start() {
		boolean loop = true;
		while (loop) {
			System.out.println("The Traveling Salesperson Problem.");
			System.out.println("Options:");
			System.out.println("1. Nearest Neighbor");
			System.out.println("2. Exhaustive Search");
			System.out.println("3. Quit");
			int selection = reader.nextInt();
			switch (selection) {
			case 1:
				long startTime = System.nanoTime();
				nearest();
				long endTime = System.nanoTime();
				long duration = (endTime-startTime)/1000;
				System.out.println("Time: " + duration + " microseconds");
				break;
			case 2:
				startTime = System.nanoTime();
				exhaustive();
				endTime = System.nanoTime();
				duration = (endTime-startTime)/1000;
				System.out.println("Time: " + duration + " microseconds");
				break;
			case 3:
				loop = false;
				break;
			default:;
				System.out.println("Invalid selection.");
				break;
			}
		}
		
	}
	
	//main
	public static void main(String[] args) {
		TSP tsp = new TSP();
		tsp.read();
		tsp.start();
	}

}
