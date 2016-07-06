package conversion;


public class Attribute {
	String nombre;
	String tipo;
	
	Attribute()
	{
		nombre = new String();
		tipo = new String();
	}
	
	Attribute(String nombre, String tipo)
	{
		this.nombre = nombre;
		this.tipo = tipo;
	}
}
