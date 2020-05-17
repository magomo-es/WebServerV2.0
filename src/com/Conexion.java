// - - - - - - - - - - - - - - - - - - - - -
// Archivos - Conexion
// PSP - UF3: Sòcols i serveis
// MaGoMo - 26/04/2020 - CEP
// - - - - - - - - - - - - - - - - - - - - -
package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Conexion {

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - OUTPUT/INPUT BYTES ->
    // - - - - - output ->
    public static void outputDataByte(Socket socket, FileInputStream fis) throws IOException {
	OutputStream os = socket.getOutputStream(); 
	int content;
	while ( (content = fis.read()) != (-1)) {
	    os.write(content);
	    // System.out.println("FileAction - sendContent - fis.read: " + content);
	}
	os.close();
    }
    // - - - - - output //
    // - - - - - input ->
    public static int[] inputDataByte(Socket socket) throws IOException {
	InputStream is = socket.getInputStream();
	ArrayList<Integer> lisa = new ArrayList<>();
	int content;
	while ( (content = is.read()) != (-1)  && (is.available()>0) ) {
	    // System.out.println("FileAction - receiveContent - fop.write: " + content);
	    lisa.add(content);
	}
	int[] retu = new int[lisa.size()];
	for(int i=0; i<lisa.size(); i++) { 
	    retu[i] = (int)lisa.get(i); 
	}
	is.close();
	return retu;
    }
    // - - - - - input //
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - OUTPUT/INPUT BYTES //

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - OUTPUT/INPUT OBJET ->
    // - - - - - output ->   
    public static void outputData(Socket socket, Object cont) throws IOException {
	OutputStream os = socket.getOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(os); // ObjectOutputStream: envia objetos
	oos.writeObject(cont);
    }
    // - - - - - output //
    // - - - - - input ->
    public static Object inputDataObject(Socket socket) throws IOException, ClassNotFoundException {
	Object cont = null;
	InputStream is = socket.getInputStream();
	ObjectInputStream ois = new ObjectInputStream(is); // ObjectOutputStream: recibe objetos
	cont = ois.readObject();
	return cont;
    }
    // - - - - - input //
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - OUTPUT/INPUT OBJET //

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - OUTPUT/INPUT INT ->
    // - - - - - output ->
    public static void outputData(Socket socket, int cont) throws IOException {
	OutputStream os = socket.getOutputStream();
	DataOutputStream oos = new DataOutputStream(os); // DataOutputStream: envia datos primitivos 
	oos.write(cont);
    }
    // - - - - - output //
    // - - - - - input ->
    public static int inputDataInt(Socket socket) throws IOException {
	int cont = 0;
	InputStream is = socket.getInputStream();
	DataInputStream dis = new DataInputStream(is); // DataInputStream: recibe datos primitivos 
	cont = (int)dis.readInt();
	return cont;
    }
    // - - - - - input ->
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - OUTPUT/INPUT INT //
    
}
