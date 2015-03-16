/*
 * This class determines the move and it rank
 */
public class RankedMove {
	double rank;
	Tile startTile;
	Tile goalTile;
	
	public RankedMove(Tile startTile1, Tile goalTile1, double rank1){
		startTile = startTile1;
		goalTile = goalTile1;
		rank = rank1;
	}
}