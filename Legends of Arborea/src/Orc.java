/* 
 * This class implements the orc unit
 */
public class Orc extends Unit {
	
	/*
	 * Constructor
	 */
	public Orc(Tile ownTile) {
		tile = ownTile;
		hitPoints = 10;
		weaponSkill = 8;
		name = "Orc";
		team = "Beasts";
		weapon = "axe";
		moveLeft = false;
		attackLeft = false;
	}

}
