package conversion;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
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
