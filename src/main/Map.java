package main;
//Ckeck
public class Map {
	
	public int sizex; //Amount of blocks on the x-axis
	public int sizey; //amount of blocks on the z-axis
	public int players;
	public int[][] startPositions;
	public boolean[][] wallsV;
	public boolean[][] wallsH;
	
	
	public Map(int players){
		sizex = (int)(Math.random() * 10 + 5);
		sizey = (int)(Math.random() * 10 + 5);
		wallsV = new boolean[sizex + 1][sizey];
		wallsH = new boolean[sizex][sizey + 1];
		startPositions = new int[players][2];
		this.players = players;
		constructRandomMap();
	}
	
	public void constructRandomMap(){ //x and y represents number of blocks on the x and y axis
		for(int x = 0; x < sizex; x++){
			for(int y = 1; y < sizey; y++){
				if(Math.random() < 0.5) wallsH[x][y] = true;
			}
		}
		for(int x = 1; x < sizex; x++){
			for(int y = 0; y < sizey; y++){
				if(Math.random() < 0.5) wallsV[x][y] = true;
			}
		}
		for(int x = 0; x < sizex; x++){
			wallsH[x][sizey] = true;
		}
		for(int x = 0; x < sizex; x++){
			wallsH[x][0] = true;
		}
		for(int y = 0; y < sizey; y++){
			wallsV[sizex][y] = true;
		}
		for(int y = 0; y < sizey; y++){
			wallsV[0][y] = true;
		}
		createRandomStartPoints();
	}
	public void createRandomStartPoints(){
		for(int i = 0; i < players; i++){
			boolean currentBlockIsAlreadyTaken = true;
			while(currentBlockIsAlreadyTaken){
				startPositions[i][0] = (int) (Math.random() * sizex);
				startPositions[i][1] = (int) (Math.random() * sizey);
				//System.out.println(startPositions[i][0] + ", " + startPositions[i][1]);
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
		
		System.out.println(startPositions[0][0] + "  " + startPositions[0][1]);
		int[][] frontier = {{startPositions[0][0],startPositions[0][1],1}};
		boolean[][] searched = new boolean[sizex][sizey];
		
		boolean allPlayersHasBeenFound = false;
		while(!allPlayersHasBeenFound && frontier.length > 0){
			//find the best frontier to follow (the one with the shortest distance from the start point)
			int pathToTake = determineBestPath(frontier);
			
			//Set the frontier being explored to have been searched
			searched[frontier[pathToTake][0]][frontier[pathToTake][1]] = true;
			
			//Check if there is any player at the frontier and set that player to found
			int[] frontierPosition = {frontier[pathToTake][0], frontier[pathToTake][1]};
			for(int i = 1; i < reachesPlayer.length; i++){
				reachesPlayer[i] = (startPositions[i] == frontierPosition);
			}
			
			//expand the frontier, adding the available frontiers that has not already been explored to the list of frontiers
			expandFrontier(frontier, pathToTake, searched);
			
			System.out.println("");
			System.out.println("Checking for Match");
			for(int i = 0; i < frontier.length; i++){
				System.out.println("New Frontier " + i + ":				" + frontier[i][0] + "    " + frontier[i][1]);
			}
			//System.out.println(frontier.length);
		}
		
		allPlayersHasBeenFound = true;
		for(int i = 0; i < reachesPlayer.length; i++){
			if(!reachesPlayer[i]) allPlayersHasBeenFound = false;
		}
		
		//System.out.println("Doesn't reach all players");
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
		int x = frontiers[path][0] - 1;
		int y = frontiers[path][1] - 1;
		
		int pathsToExpand = 0;
		
		boolean north = false,
				south = false,
				east  = false,
				west  = false;
		
		//check if you might go north
		north = !wallsH[x][y] && y != 0;
		if(north){
			if(!searched[x][y - 1]) pathsToExpand++;
			else north = false;
		}
		//south
		south = !wallsH[x][y + 1]  && y != sizey - 1;
		if(south){
			if(!searched[x][y + 1]) pathsToExpand++;
			else south = false;
		}
		//west
		west = !wallsV[x][y] && x != 0;
		if(west){
			if(!searched[x - 1][y]) pathsToExpand++;
			else west = false;
		}
		//east
		east = !wallsV[x + 1][y] && x != sizex - 1;
		if(east){
			if(!searched[x + 1][y]) pathsToExpand++;
			else east = false;
		}
		
		System.out.println(north + "  " + south + "   " + west + "   " + east);
		
		//Create array that is going to fit in all the new frontiers but not the old one
		int[][] temp = new int[frontiers.length + pathsToExpand - 1][3];
		
		System.out.println(frontiers.length + "   " + pathsToExpand);
		
		//Copy the old frontier array into the new array with exception of the used frontier
		boolean foundDiscardableFrontier = false;
		for(int i = 0; i < frontiers.length - 1; i++){
			if(i == path)	foundDiscardableFrontier = true;
			if(foundDiscardableFrontier){
				temp[i][0] = frontiers[i + 1][0];
				temp[i][1] = frontiers[i + 1][1];
				temp[i][2] = frontiers[i + 1][2];
			}
			else{
				temp[i][0] = frontiers[i][0];
				temp[i][1] = frontiers[i][1];
				temp[i][2] = frontiers[i][2];
			}
		}
		
		
		{//Add the new frontiers
			int pathsExpanded = 0;
			
			//North
			if(north){
				temp[temp.length - pathsToExpand + pathsExpanded][0] = frontiers[path][0];
				temp[temp.length - pathsToExpand + pathsExpanded][1] = frontiers[path][1] - 1;
				temp[temp.length - pathsToExpand + pathsExpanded][2] = frontiers[path][2] + 1;
				pathsExpanded++;
			}
			//south
			if(south){
				temp[temp.length - pathsToExpand + pathsExpanded][0] = frontiers[path][0];
				temp[temp.length - pathsToExpand + pathsExpanded][1] = frontiers[path][1] + 1;
				temp[temp.length - pathsToExpand + pathsExpanded][2] = frontiers[path][2] + 1;
				pathsExpanded++;
			}
			//west
			if(west){
				temp[temp.length - pathsToExpand + pathsExpanded][0] = frontiers[path][0] - 1;
				temp[temp.length - pathsToExpand + pathsExpanded][1] = frontiers[path][1];
				temp[temp.length - pathsToExpand + pathsExpanded][2] = frontiers[path][2] + 1;
				pathsExpanded++;
			}
			//east
			if(east){
				temp[temp.length - pathsToExpand + pathsExpanded][0] = frontiers[path][0] + 1;
				temp[temp.length - pathsToExpand + pathsExpanded][1] = frontiers[path][1];
				temp[temp.length - pathsToExpand + pathsExpanded][2] = frontiers[path][2] + 1;
				pathsExpanded++;
			}
		}//done adding new frontiers
		
		System.out.println("Frontier values:			" + frontiers[path][0] + "    " + frontiers[path][1]);
		//set frontiers to new true frontier
		frontiers = temp;

		for(int i = 0; i < frontiers.length; i++){
			System.out.println("New Frontier " + i + ":				" + frontiers[i][0] + "    " + frontiers[i][1]);
		}
	}
}
