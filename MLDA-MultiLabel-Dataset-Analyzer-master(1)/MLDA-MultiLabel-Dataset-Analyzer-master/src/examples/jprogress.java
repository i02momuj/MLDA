/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
*
* @author OctiCoCo
*/
public class jprogress extends Thread implements ActionListener{

private JProgressBar barra = new JProgressBar();
private JFrame ventana = new JFrame("Ejemplo de JProgressBarr");
private JButton iniciar = new JButton("Iniciar");
private JLabel accion = new JLabel("Aguardando interacción del usuario");
private JLabel porcentaje = new JLabel("000%");
private JTable tabla = new JTable();
private JScrollPane scrollPane = new JScrollPane(tabla);
private DefaultTableModel dtm;
private final int cantidad = 100;

public jprogress(){
ventana.setLayout(null);
setActionListeners(this);
//comenzamos dando los tamaños a los elementos y los ubicamos en la ventana
ventana.setSize(450, 420);
scrollPane.setBounds(20, 60, 400, 200); //UTILIZAMOS EL SCROLL Y NO LA TABLA para poder mostrar los titulos de la tabla y las barras de scroll
barra.setBounds(20, 350, 400, 20);
iniciar.setBounds(270, 20, 150, 30);
accion.setBounds(20, 20, 250, 20);
porcentaje.setBounds(20, 300, 400, 20);
porcentaje.setHorizontalAlignment(porcentaje.CENTER);
//ahora ubicamos los elementos en nuestro frame:
ventana.add(accion);
ventana.add(iniciar);
ventana.add(scrollPane);
ventana.add(porcentaje);
ventana.add(barra);
//le damos el maximo valor que puede tomar la barra
barra.setMaximum(cantidad);

//mostramos la ventana y la posicionamos al centro
ventana.setVisible(true);
ventana.setLocationRelativeTo(null); //con esto se centra en la pantalla

//ya tenemos la interface lista, ahora mostremos algun contenido...
//por ejemplo los primeros 100 numeros primos (se usara cantidad como constante del 100).
//y carguemos estos en la tabla

//para facilitar el manejo de la tabla se puede usar un DefaultTableModel se utiliza de la siguiente manera:

String[] titulos = {"indice", "Numero primo encontrado"};//estos seran los titulos de la tabla.
Object[][] datos = {}; //en esta matriz bidimencional cargaremos los datos
dtm = new DefaultTableModel(datos, titulos);
tabla.setModel(dtm);
//ahora esta todos listo para ir cargando los datos en la tabla y a medida que se cargen actualizaremos la barra

}

// Aqui indicamos todos los objetos a los cuales no interece tomar la accion del click
public void setActionListeners(ActionListener lis){
iniciar.addActionListener(lis);
}

//en este metodo vemos a quien se le hizo click y realizamos la accion
@Override
public void actionPerformed(ActionEvent e) {
if (e.getSource() == iniciar){ // verificamos si hizo click en el boton
iniciar.setText("Iniciado");
accion.setText("Obteniendo nº primos");
this.start(); //Esto es lo que ejecuta el thread, es decir lo que hace la concurrencia
//el start() de una clase que extiende threads llama a la funcion run() de esa clase
//por lo cual ahora continuamos con la funcion run()
//en esta se deberan cargar los datos en la tabla y actualizar el valor de la barra
}
}

public void run(){

int indice = 1; //indice contara los numeros primos que vamos encontrando
int i = 1; //sera nuestra variable de recorrido de los numeros
while (indice < cantidad+1){ //mientras no tengamos los 100 nummeros primos
if (esPrimo(i)){
// i es primo por lo cual aumentamos el indice y guardamos los datos en la tabla y aumentamos la barra
Object[] otraFilaDeLaTabla = {indice, i}; //creamos la fila
dtm.addRow(otraFilaDeLaTabla); //y la añadimos a la tabla
barra.setValue(indice); //aumentamos la barra
porcentaje.setText(((indice * 100) / cantidad) + "%");
indice++;
//con las lineas siguiente damos un tiempo para que se vea el funcionamiento de la barra
try {
Thread.sleep(100);
} catch (InterruptedException ex) {
Logger.getLogger(jprogress.class.getName()).log(Level.SEVERE, null, ex);
}
}
i++;
}
//terminamos de encontrar los numero por lo que ponemos la barra en el 100% que seria el ultimo valor de indice;
barra.setValue(indice);
porcentaje.setText("100%");
iniciar.setText("Finalizado");
accion.setText("Se encontraron "+cantidad+" nº primos");
//ventana.dispose(); //cone esto cerrariamos la ventana.
}


/**
* Metodo que retorna true si un numero es primo, false en otro caso (no sea primo)
* @param numero este parametro es por el numero que queremos preguntar si es primo
* @return true si un numero es primo, false caso contrario
*/
public static boolean esPrimo(int numero){
int contador=0;
for(int j=numero; j>=1; j--){
if((numero%j)==0)
contador++;
}
if(contador==2)
return true;
else
return false;
}

//metodo principal (de ejecucion)
public static void main (String[] arg){
//creamos una instancia para poder correr el program
new jprogress();
}

} 