import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a route from one tile to another
 */
public class Route implements Comparable<Route> {
	private final Tile start;
	private final Tile end;
	private final int distance;
	
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
    
    public LinkedList<Tile> getPath(Ants ants, Tile start, Tile end) {
    	LinkedList<Tile> foundPath = new LinkedList<Tile>();
        LinkedList<Tile> opensList= new LinkedList<Tile>();
        LinkedList<Tile> closedList= new LinkedList<Tile>();
        Hashtable<Tile, Integer> gscore = new Hashtable<Tile, Integer>();
        Hashtable<Tile, Tile> cameFrom = new Hashtable<Tile, Tile>();
        
        Tile x = new Tile(0, 0);
        
        gscore.put(start, 0);
        opensList.add(start);
        
        while (!opensList.isEmpty()) {
        	
        	int min = -1;
        	
        	for (Tile t : opensList) {
        		if (min == -1) {
        			min = gscore.get(t) + getH(t, end);
        			x = t;
        		} else {
        			int currf = gscore.get(t) + getH(t, end);
        			
        			if (min > currf) {
        				min = currf;
        				x = t;
        			}
        		}
        	}
        	
        	if (x == end) {
        		Tile curr = end;
        		
        		while (curr != start) {
        			foundPath.addFirst(curr);
        			curr = cameFrom.get(curr);
        		}
        		return foundPath;
        	}
        	
        	opensList.remove(x);
        	closedList.add(x);
        	
        	Tile tileEast = ants.getTile(x, Aim.EAST);
        	Tile tileWest = ants.getTile(x, Aim.WEST);
        	
        	List<Tile> neighbours = Arrays.asList(
        			ants.getTile(x, Aim.NORTH),
        			ants.getTile(x, Aim.SOUTH),
        			ants.getTile(x, Aim.EAST),
        			ants.getTile(x, Aim.WEST),
        			x,
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
        		
        		int tentGScore = gscore.get(x) + getDist(ants, x, neighbour);
        		boolean distIsBetter = false;
        		
        		if (!opensList.contains(neighbour)) {
        			opensList.add(neighbour);
        			distIsBetter = true;
        		} else if (tentGScore < gscore.get(neighbour)) {
        			distIsBetter = true;
        		}
        		
        		if (distIsBetter) {
        			cameFrom.put(neighbour, x);
        			gscore.put(neighbour, tentGScore);
        		}
        	}
        }
        
        Logger.writeLog(foundPath);
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
