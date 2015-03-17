import java.util.ArrayList;

/* 
 * This class describes what every unit should have and be able to do
 */
public class Unit {
	int hitPoints;
	int weaponSkill;
	String name;
	String team;
	boolean selected;
	String weapon;
	int buffer;
	Tile tile;
	boolean attackLeft;
	boolean moveLeft;
	ArrayList<Unit> surroundingHostiles;
	
	/*
	 * Constructor is left empty since the child classes extend it
	 */
	public Unit() {
	}
	
	/*
	 * This method checks if and how many friendly units 
	 * are present at tiles nearby, and calculates the buffer 
	 */
	public int getBuffer() {
		buffer = 0;
		// Specify who are friendly and who are hostile
		String friendlyGeneral;
		String friendlyInfantry;
		String hostileGeneral;
		String hostileInfantry;
		if (team.equals("Humans")) {
			friendlyGeneral = "General";
			friendlyInfantry = "Swordsman";
			hostileGeneral = "Orc";
			hostileInfantry = "Goblin";
		}
		else {
			friendlyGeneral = "Orc";
			friendlyInfantry = "Goblin";
			hostileGeneral = "General";
			hostileInfantry = "Swordsman";
		}
		
		// Build up buffer based on hostile/friendly units nearby
		ArrayList<Tile> adjacentTiles = tile.adjacentTiles;
		int nrTiles = adjacentTiles.size();
		for (int i = 0; i < nrTiles; i++) {
			if (adjacentTiles.get(i) != null & adjacentTiles.get(i).unit != null) {
				String unitName = adjacentTiles.get(i).unit.name;
				if (unitName.equals(friendlyGeneral)){
					buffer += 2;
				}
				else if (unitName.equals(friendlyInfantry)){
					buffer += 1;
				}
				else if (unitName.equals(hostileGeneral)) {
					buffer -= 2;
				}
				else if (unitName.equals(hostileInfantry)) {
					buffer -= 1;
				}
			}
		}
		return buffer;
	}
	
	
	/*
	 * This method returns all hostile forces around a position
	 */
	public ArrayList<Unit> surroundingHostiles() {
		surroundingHostiles = new ArrayList<Unit>();
		Unit surroundingUnit;
		ArrayList<Tile> adjacentTiles = tile.adjacentTiles;
		// Loop over adjacent tiles to find hostile units
		for (Tile adjacentTile : adjacentTiles) {
			if (adjacentTile == null | adjacentTile.unit == null) {
				continue;
			}
			surroundingUnit = adjacentTile.unit; 
			if (!surroundingUnit.team.equals(team)) {
				surroundingHostiles.add(surroundingUnit);
			}
		}
		return surroundingHostiles;
	}
	
	/*
	 * Check if the tile contains a hostile
	 */
	public boolean isHostile(Tile tile) {
		try {
			return surroundingHostiles().contains(tile.unit);
		} 
		catch (NullPointerException e){
			return false;
		}
	}

}
