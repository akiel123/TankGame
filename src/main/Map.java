package main;

public class Map {
	
	public int sizex; //Amount of blocks on the x-axis
	public int sizey; //amount of blocks on the z-axis
	public int players;
	public int[][] startPositions;
	public boolean[][] wall;
	
	
	public Map(int players){
		sizex = (int)(Math.random() * 10 + 5);
		sizey = (int)(Math.random() * 10 + 5);
		wall = new boolean[sizex][sizey * 2]; //Determines whether there is a wall at a given point. wall[i][0,2,4...] represents a vertical wall
		startPositions = new int[players][2];
		this.players = players;
		constructRandomMap();
	}
	
	public void constructRandomMap(){ //x and y represents number of blocks on the x and y axis
		for(int x = 0; x < wall.length; x++){
			for(int y = 0; y < wall[x].length; y++){
				if(Math.random() < 0.5) wall[x][y] = true;
			}
		}
		createRandomStartPoints();
	}
	public void createRandomStartPoints(){
		for(int i = 0; i < players; i++){
			boolean currentBlockIsAlreadyTaken = true;
			while(currentBlockIsAlreadyTaken){
				startPositions[i][0] = (int) (Math.random() * sizex);
				startPositions[i][1] = (int) (Math.random() * sizey);
				System.out.println(startPositions[i][0] + ", " + startPositions[i][1]);
				for(int j = 0; j < players; j++){
					if((j != i) && (startPositions[i] != startPositions[j])){
						currentBlockIsAlreadyTaken = false;
					}
				}
			}
		}
	}
	
	public void checkMapValidity(){
		boolean[] reachesPlayer = new boolean[players];
		int x = startPositions[0][0];
		int y = startPositions[0][1];
		reachesPlayer[0] = true;
		for(int i = 1; i < reachesPlayer.length; i++){
			reachesPlayer[i] = false;
		}
		
		int[][] frontier = {{startPositions[0][0],startPositions[0][1],1}};
		boolean[][] searched = new boolean[sizex][sizey];
		
		boolean allPlayersHasBeenFound = false;
		while(!allPlayersHasBeenFound && frontier.length > 0){
			int pathToTake = determineBestPath(frontier);
			searched[frontier[pathToTake][0]][frontier[pathToTake][1]] = true;
			int[] frontierPosition = {frontier[pathToTake][0], frontier[pathToTake][1]};
			for(int i = 1; i < reachesPlayer.length; i++){
				reachesPlayer[i] = (startPositions[i] == frontierPosition);
			}
			expandFrontier(frontier, pathToTake, searched);
			System.out.println(frontier.length);
		}
		
		allPlayersHasBeenFound = true;
		for(int i = 0; i < reachesPlayer.length; i++){
			if(!reachesPlayer[i]) allPlayersHasBeenFound = false;
		}
		
		System.out.println("Doesn't reach all players");
	}
	public int determineBestPath(int[][] f){
		int bestPath = 0;
		int bestValue = 99999;
		
		for(int i = 0; i < f.length; i++){
			if(f[i][2] < bestValue){
				bestPath = i;
				bestValue = f[i][2];
			}
		}
		
		return bestPath;
	}
	public void expandFrontier(int[][] frontiers, int path, boolean[][] searched){
		int x = frontiers[path][0];
		int y = frontiers[path][1];
		
		int pathsToExpand = 0;
		
		boolean north = false,
				south = false,
				east  = false,
				west  = false;
		
		//check if you might go north
		if(y != 0){
			if(!searched[x][y - 1]){
				north = !wall[x][y * 2];
				if(north) pathsToExpand++;
			}
		}
		//south
		if(y != sizey){
			if(!searched[x][y + 1]){
				south = !wall[x][(y + 1) * 2];	
				if(south) pathsToExpand++;
			}
		}
		//west
		if(y != 0){
			if(!searched[x - 1][y]){ 
				west = !wall[x][y * 2 + 1];
			}
		}
		//east
		if(y != 0){
			if(!searched[x + 1][y]){
				east = !wall[x][(y + 1) * 2 + 1];
				if(east) pathsToExpand++;
			}
		}

		System.out.println("Nort:" + north + "   South:" + south + "    west:" + west + "     east:" + east);
		
		int[][] temp = new int[frontiers.length + pathsToExpand - 1][3];
		if(temp.length == 0){
			frontiers = temp;
			return;
		}
		System.out.println(frontiers.length + "  " + pathsToExpand);
		int d = 0;
		for(int i = 0; i < frontiers.length; i++){
			System.out.println("Temp:" + temp.length + "   frontiers:" + frontiers.length + "    d:" + d + "     i:" + i);
			temp[i - d][0] = frontiers[i][0];
			temp[i - d][1] = frontiers[i][1];
			temp[i - d][2] = frontiers[i][2];
			if(i == path){
				d++;
			}
		}
		int expansionsMade = 0;
		if(north){
			temp[frontiers.length - 1 + expansionsMade][0] = x;
			temp[frontiers.length - 1 + expansionsMade][0] = y - 1;
			temp[frontiers.length - 1 + expansionsMade][0] = frontiers[path][2] + 1;
			expansionsMade++;
		}
		if(south){
			temp[frontiers.length - 1 + expansionsMade][0] = x;
			temp[frontiers.length - 1 + expansionsMade][0] = y + 1;
			temp[frontiers.length - 1 + expansionsMade][0] = frontiers[path][2] + 1;
			expansionsMade++;
		}
		if(west){
			temp[frontiers.length - 1 + expansionsMade][0] = x - 1;
			temp[frontiers.length - 1 + expansionsMade][0] = y;
			temp[frontiers.length - 1 + expansionsMade][0] = frontiers[path][2] + 1;
			expansionsMade++;
		}
		if(east){
			temp[frontiers.length - 1 + expansionsMade][0] = x + 1;
			temp[frontiers.length - 1 + expansionsMade][0] = y;
			temp[frontiers.length - 1 + expansionsMade][0] = frontiers[path][2] + 1;
			expansionsMade++;
		}
		
		frontiers = temp;
	}
}
