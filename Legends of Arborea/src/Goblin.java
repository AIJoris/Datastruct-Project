/* 
 * This class implements the goblin unit
 */
public class Goblin extends Unit {
	
	/*
	 * Constructor sets default values
	 */
	public Goblin(Tile ownTile) {
		tile = ownTile;
		hitPoints = 3;
		weaponSkill = 4;
		name = "Goblin";
		team = "Beasts";
		weapon = "axe";
		moveLeft = false;
		attackLeft = false;
	}

}
