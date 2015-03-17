/* 
 * This class implements the general unit
 */
public class General extends Unit {

	/*
	 * Constructor sets default values
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
