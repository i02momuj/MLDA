package conversion;

public class Attribute {
    String name;
    String type;
    
    Attribute()
    {
        name = new String();
	type = new String();
    }
	
    Attribute(String nombre, String tipo)
    {
        this.name = nombre;
	this.type = tipo;
    }
}
