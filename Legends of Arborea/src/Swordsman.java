/* 
 * This class implements the swordsman unit
 */
public class Swordsman extends Unit {
	
	/*
	 * Constructor set default values
	 */
	public Swordsman(Tile ownTile) {
		tile = ownTile;
		hitPoints = 4;
		weaponSkill = 6;
		name = "Swordsman";
		team = "Humans";
		weapon = "sword";
		moveLeft = false;
		attackLeft = false;
	}

}
