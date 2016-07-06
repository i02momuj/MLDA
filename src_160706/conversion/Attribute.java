package conversion;


public class Attribute {
	String name;
	String type;
	
	Attribute()
	{
		name = new String();
		type = new String();
	}
	
	Attribute(String name, String type)
	{
		this.name = name;
		this.type = type;
	}
}
