package convertir;

public class Main {
	
	public static void mostrarFuncionamiento()
	{
		System.out.println("You must pass 3 arguments:");
		System.out.println("\t1. -f meka/mulan: input file format");
		System.out.println("\t2. -i inputFileName: input file name (without extension)");
		System.out.println("\t3. -o outputFileName: output file name (without extension)");
	}

	public static void main(String [] args){
		
		int argc = args.length;
		
		boolean fflag=false, iflag=false, oflag=false;
		String fvalue = new String();
		String ivalue = new String();
		String ovalue = new String();
		
		
		if(argc != 6)
		{
			System.out.println("Wrong arguments.");
			mostrarFuncionamiento();
		}
		else{
			for(int i=0; i<argc; i++)
			{
				if(args[i].equals("-f"))
				{
					fflag = true;
					fvalue = args[i+1];
					i++;
				}
				else if(args[i].equals("-i"))
				{
					iflag = true;
					ivalue = args[i+1];
					i++;
				}
				else if(args[i].equals("-o"))
				{
					oflag = true;
					ovalue = args[i+1];
					i++;
				}
				else
				{
					System.out.println("Wrong arguments.");
					mostrarFuncionamiento();
					System.exit(-1);
				}
			}
			
			if((fflag == false) || (iflag == false) || (oflag == false))
			{
				System.out.println("Argumentos en linea de ordenes incorrectos.");
				mostrarFuncionamiento();
				System.exit(-1);
			}
			
			System.out.println("fvalue: " + fvalue);
			System.out.println("ivalue: " + ivalue);
			System.out.println("ovalue: " + ovalue);
			
			if(ivalue.equals(ovalue))
			{
				System.out.println("Input and output files must be different.");
				mostrarFuncionamiento();
				System.exit(-1);
			}
			else if(fvalue.toLowerCase().equals("meka"))
			{
				MekaToMulan m = new MekaToMulan();
				m.convertir(ivalue, ovalue);
			}
			else if(fvalue.toLowerCase().equals("mulan"))
			{
				MulanToMeka m = new MulanToMeka();
				m.convertir(ivalue, ovalue);
			}
			else{
				mostrarFuncionamiento();
			}
		}
	}

}
