import java.util.List;


public class Mission {
	
	private List<Tile> path;
	private int progression;
	
	public Mission(List<Tile> path) {
		this.path = path;
		this.progression = 0;
	}
	
}
