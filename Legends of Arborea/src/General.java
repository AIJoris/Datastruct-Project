/* 
 * This class implements the general unit
 */
public class General extends Unit {

	/*
	 * Constructor
	 */
	public General(Tile ownTile) {
		tile = ownTile;
		hitPoints = 5;
		weaponSkill = 8;
		name = "General";
		team = "Humans";
		weapon = "sword";
		moveLeft = false;
		attackLeft = false;
	}
}
