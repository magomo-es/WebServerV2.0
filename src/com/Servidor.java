// - - - - - - - - - - - - - - - - - - - - -
// Archivos - Servidor
// PSP - UF3: Sòcols i serveis
// MaGoMo - 26/04/2020 - CEP
// - - - - - - - - - - - - - - - - - - - - -
package com;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 5000;
	
    public static void main(String[] args) {

	// - - - - - variables de trabajo
	String ip = "", lintext = "";
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	HttpRequest solicitud = null;	
	boolean mustCont = true, isError = false;
	Exception ex = null;
	
        // - - - - - - - - - - - - - - - - - - - - abre socket servidor ->
        try { 
	    // - - - - - Abre puerto
	    serverSocket = new ServerSocket(PORT);
	    System.out.print("Socket servidor abierto esperando conexion ... ");
        }
        catch(Exception e) { System.out.println("- catch conexion " + e.getCause()); ex = e; }

	// - - - - - - - - - - - - - - - - - - - - abre socket servidor //

	// - - - - - ciclo permanente 
	while (ex==null) {
	    
	    // - - - - - - - - - - - - - - - - - - - - inicio conexion ->
	    try { 
		// - - - - - Acepta conexion de cliente
		clientSocket = serverSocket.accept();
		System.out.println( clientSocket.getInetAddress().getHostAddress() + " Conectado !!\n");
	    }
	    catch(Exception e) { System.out.println("- catch conexion " + e.getCause()); ex = e; }

	    // http://localhost:5000/inici.html
	    // - - - - - - - - - - - - - - - - - - - - inicio thread //
	    new HttpRequest( clientSocket ).start();
		
	}

	// - - - - - - - - - - - - - - - - - - - - cierra conexion ->
	try { serverSocket.close(); } 
	catch (IOException ex1) { System.out.println("Salida por catch . " + ex1.getMessage()); }
	// - - - - - - - - - - - - - - - - - - - - cierra conexion //

	System.out.println("Ejecución cancelada...");
    }

}
