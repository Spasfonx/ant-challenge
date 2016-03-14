import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents a route from one tile to another
 */
public class Route implements Comparable<Route> {
	private final Tile start;
	private final Tile end;
	private final int distance;
	
	private LinkedList<Tile> opensList= new LinkedList<Tile>();
    private LinkedList<Tile> closedList= new LinkedList<Tile>();
	
	private LinkedList<Tile> path;
	
	public Route(Tile start, Tile end, int distance) {
		this.start = start;
		this.end = end;
        this.distance = distance;
	}
	
	public Tile getStart() {
        return start;
    }

    public Tile getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }
    
    @Override
    public int compareTo(Route route) {
    	return distance - route.distance; 
    }
    
    @Override
    public int hashCode() {
    	return start.hashCode() * Ants.MAX_MAP_SIZE * Ants.MAX_MAP_SIZE + end.hashCode();
    }
    
    public List<Aim> getNextDirection(Ants ants, Tile start, Tile end) {        
    	/* Initialisation */
    	Tile current = start;
    	
    	opensList.add(current);
    	ajouterListeFermee(current);
        ajouterCasesAdjacentes(ants, current, end);
        
        while (!current.equals(end) && !opensList.isEmpty()) {
        	current = meilleurNoeud(opensList);
        	
        	ajouterListeFermee(current);
        	ajouterCasesAdjacentes(ants, current, end);
        }
        
        if (current.equals(end)) {
        	return ants.getDirections(
        			closedList.getFirst(),
        			closedList.get(1)
        		);
        }
        
        return null;
    }
    
    
    private void ajouterCasesAdjacentes(Ants ants, Tile current, Tile end) {
    	List<Tile> neighbours = getThrowableNeighbours(ants, current);
    	
    	for (Tile neighbour : neighbours) {
    		if (!closedList.contains(neighbour)) {
    			
    			int cout = ants.getDistance(neighbour, end);
    			
    			if (opensList.contains(neighbour)) {
    				int indexOfNeighbour = opensList.indexOf(neighbour);
    				
    				if (cout < opensList.get(indexOfNeighbour).getCout()) {
    					opensList.get(indexOfNeighbour).setCout(cout);
    				}
    			} else {
    				opensList.add(neighbour);
    			}
    		}
    	}
	}
    
    private Tile meilleurNoeud(List<Tile> list) {
		int cout = Integer.MAX_VALUE;
		int i = 0;
		int pointeur = -1;
		
		for(Tile t : list) {
			if (t.getCout() < cout) {
				pointeur = i;
				cout = t.getCout();
			}
			i++;
		}
		
		return list.get(pointeur);
    }
    
    private void ajouterListeFermee(Tile tile) {
    	Tile n = opensList.get(opensList.indexOf(tile));
    	
    	closedList.add(n);
    	opensList.remove(n);
    }
    
    private List<Tile> getThrowableNeighbours(Ants ants, Tile tile) {
    	/*
    	 * On récupère tous les voisins (les X) :
    	 * 	   [X]
    	 * 	[X][C][X]
    	 * 	   [X] 
    	 */
    	List<Tile> neighbours = new ArrayList<Tile>();
    	
    	Tile tileNorth = ants.getTile(tile, Aim.NORTH);
    	Tile tileSud = ants.getTile(tile, Aim.SOUTH);
    	Tile tileEst = ants.getTile(tile, Aim.EAST);
    	Tile tileOuest = ants.getTile(tile, Aim.WEST);
    	
    	if (ants.getIlk(tileNorth).isUnoccupied()) {
    		neighbours.add(tileNorth);
    	}
    	if (ants.getIlk(tileSud).isUnoccupied()) {
    		neighbours.add(tileSud);
    	}
    	if (ants.getIlk(tileEst).isUnoccupied()) {
    		neighbours.add(tileEst);
    	}
    	if (ants.getIlk(tileOuest).isUnoccupied()) {
    		neighbours.add(tileOuest);
    	}
    	
    	return neighbours;
    }

	public LinkedList<Tile> getPath(Ants ants, Tile start, Tile end) {
    	LinkedList<Tile> foundPath = new LinkedList<Tile>();
        LinkedList<Tile> opensList= new LinkedList<Tile>();
        LinkedList<Tile> closedList= new LinkedList<Tile>();
        Hashtable<Tile, Integer> gscore = new Hashtable<Tile, Integer>();
        Hashtable<Tile, Tile> cameFrom = new Hashtable<Tile, Tile>();
        
        Tile current = null;
        
        gscore.put(start, 0);
        opensList.add(start);
        
        while (!opensList.isEmpty()) {
        	
        	int min = -1;
        	
        	for (Tile t : opensList) {
        		if (min == -1) {
        			min = gscore.get(t) + getH(t, end);
        			current = t;
        		} else {
        			int currf = gscore.get(t) + getH(t, end);
        			
        			if (min > currf) {
        				min = currf;
        				current = t;
        			}
        		}
        	}
        	
        	if (current == end) {
        		Tile curr = end;
        		
        		while (curr != start) {
        			foundPath.addFirst(curr);
        			curr = cameFrom.get(curr);
        		}
        		return foundPath;
        	}
        	
        	opensList.remove(current);
        	closedList.add(current);
        	
        	Tile tileEast = ants.getTile(current, Aim.EAST);
        	Tile tileWest = ants.getTile(current, Aim.WEST);
        	
        	/*
        	 * On récupère tous les voisins (les X) :
        	 * 	[X][X][X]
        	 * 	[X][C][X]
        	 * 	[X][X][X] 
        	 */
        	List<Tile> neighbours = Arrays.asList(
        			ants.getTile(current, Aim.NORTH),
        			ants.getTile(current, Aim.SOUTH),
        			ants.getTile(current, Aim.EAST),
        			ants.getTile(current, Aim.WEST),
        			current,
        			ants.getTile(tileEast, Aim.NORTH),
        			ants.getTile(tileEast, Aim.SOUTH),
        			ants.getTile(tileWest, Aim.NORTH),
        			ants.getTile(tileWest, Aim.SOUTH)
        	);
        	
        	for (Tile neighbour : neighbours) {
        		if (!ants.getIlk(neighbour).isUnoccupied()
        				|| closedList.contains(neighbour)) {
        			continue;
        		}
        		
        		int tentGScore = gscore.get(current) + getDist(ants, current, neighbour);
        		boolean distIsBetter = false;
        		
        		if (!opensList.contains(neighbour)) {
        			opensList.add(neighbour);
        			distIsBetter = true;
        		} else if (tentGScore < gscore.get(neighbour)) {
        			distIsBetter = true;
        		}
        		
        		if (distIsBetter) {
        			cameFrom.put(neighbour, current);
        			gscore.put(neighbour, tentGScore);
        		}
        	}
        }
        
        Logger.writeLog(String.format("getPath(%s.%s, %s.%s)", start.getRow(), start.getCol(), end.getRow(), end.getCol()));
        Logger.writeLog(foundPath);
        Logger.writeLog("fin getPath()");
        
        return foundPath;
    }
    
    private int getH(Tile start, Tile end){
        int x;
        int y;
        x = start.getRow() - end.getRow();
        y = start.getCol() - end.getCol();
        if(x<0){
            x = x* (-1);
        }
        if(y<0){
            y = y * (-1);
        }
        return x + y;
    }
    
    private int getDist(Ants ants, Tile start, Tile end){
        int ret = 0;
        
        if(ants.getIlk(end).isUnoccupied()){
            ret = 8;
        }else if(start.getRow() == end.getRow() || start.getCol() == end.getCol()){
            ret = 10;
        }else{
            ret = 14;
        }

        return ret;
    }
    
    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Route) {
            Route route = (Route)o;
            result = start.equals(route.start) && end.equals(route.end);
        }
        return result;
    }
}
