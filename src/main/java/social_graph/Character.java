package social_graph;

public class Character {
	String name;
	int distance;
	
	public Character(String name, int distance)
	{
		this.name = name;
		this.distance = distance;
	}
	
	String getName(){
		return this.name;
	}
	
	int getDistance(){
		return this.distance;
	}
}
