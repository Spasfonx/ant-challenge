import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AStar {
	
	private LinkedList<Tile> opensList= new LinkedList<Tile>();
    private LinkedList<Tile> closedList= new LinkedList<Tile>();
	
	public List<Tile> getPath(Ants ants, Tile start, Tile end) {
    	/* Initialisation */
    	Tile current = start;
    	
    	current.setCout(ants.getDistance(current, end));
    	
    	opensList.add(current);
    	ajouterListeFermee(current);
        ajouterCasesAdjacentes(ants, current, end);
        
        while (!current.equals(end) && !opensList.isEmpty()) {
        	current = meilleurNoeud(opensList, end);
        	
        	ajouterListeFermee(current);
        	ajouterCasesAdjacentes(ants, current, end);
        }
                
        if (current.equals(end)) {
        	return closedList;
        }
        
        return null;
    }
    
    
    private void ajouterCasesAdjacentes(Ants ants, Tile current, Tile end) {
    	List<Tile> neighbours = getThrowableNeighbours(ants, current);
    	
    	for (Tile neighbour : neighbours) {
    		if (!closedList.contains(neighbour)) {
    			
    			Tile tmp = new Tile(neighbour.getRow(), neighbour.getCol());
    			tmp.setCout(ants.getDistance(neighbour, end));
    			
    			if (opensList.contains(neighbour)) {
    				int indexOfNeighbour = opensList.indexOf(neighbour);
    				
    				if (tmp.getCout() < opensList.get(indexOfNeighbour).getCout()) {
    					opensList.set(indexOfNeighbour, tmp);
    				}
    			} else {
    				neighbour.setCout(tmp.getCout());
    				opensList.add(neighbour);
    			}
    		}
    	}
	}
    
    private Tile meilleurNoeud(List<Tile> list, Tile end) {
		int cout = Integer.MAX_VALUE;
		int i = 0;
		int pointeur = -1;
		
		for(Tile t : list) {
			if (t.getCout() <= cout) {
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
    	//opensList.remove(tile);
    	opensList.clear();
    	
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
    	
    	if (ants.getIlk(tileNorth).isNotThrowable()) {
    		neighbours.add(tileNorth);
    	}
    	if (ants.getIlk(tileSud).isNotThrowable()) {
    		neighbours.add(tileSud);
    	}
    	if (ants.getIlk(tileEst).isNotThrowable()) {
    		neighbours.add(tileEst);
    	}
    	if (ants.getIlk(tileOuest).isNotThrowable()) {
    		neighbours.add(tileOuest);
    	}
    	
    	return neighbours;
    }

}
