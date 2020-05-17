// - - - - - - - - - - - - - - - - - - - - -
// Archivos - FileAction: clase que contiene archivo a enviar/recibir y accion a realizar
// PSP - UF3: Sòcols i serveis
// MaGoMo - 26/04/2020 - CEP
// - - - - - - - - - - - - - - - - - - - - -
package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.nio.file.FileSystems;

public class FileAction implements Serializable {

    public static final String WORKDIR = FileSystems.getDefault().getPath("").toAbsolutePath().toString() + "\\www";

    // - - - - - - - - - - attributes
    
    File m_file;
    int m_action; // 0: upload | 1: download

    // - - - - - - - - - - constructor
    
    public FileAction( String name, int action ) {
	this.m_file = new File (WORKDIR + "\\" + name);
	this.m_action = action;
	System.out.println("this.m_file: " + this.m_file);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - sendContent ->
    
    public void sendContent(Socket skt) {
	// - - - - - ciclo de lectura de stream de archivo y envio
	FileInputStream fis = null;
	int content;
	try {
	    fis = new FileInputStream(this.m_file);
	    System.out.println("FileAction - sendContent: START " + this.m_file.getName() );
	    Conexion.outputDataByte(skt, fis); 
	    System.out.println("FileAction - sendContent: CLOSE " + this.m_file.getName() );
	    fis.close();
	} 
	catch (IOException e) { System.out.println("FileAction - sendContent: " + e.getMessage()); } 
	finally { 
	    try { if (fis != null) fis.close(); } 
	    catch (IOException ex) { System.out.println("FileAction - sendContent (closing): " + ex.getMessage()); }
	}
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - sendContent //
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - receiveContent ->
    
    public void receiveContent(Socket skt) {
	// - - - - - define archivo localarchivociclo de lectura de stream de archivo y envio

	int[] content;
	// - - - - - ciclo de recepcion de stream de archivo y almacenamiento
	FileOutputStream fop = null;

	// - - - - - alimina archivo si existe
	if ( this.m_file.exists() ) { this.m_file.delete(); }

	// - - - - - ciclo de lectura de stream de archivo y recepcion
	try {
	    // - - - - - creacion de archivo
	    this.m_file.createNewFile();
	    // - - - - - output	    
	    fop = new FileOutputStream(this.m_file);
	    content = Conexion.inputDataByte(skt);
	    // System.out.println("FileAction - receiveContent: START" );
	    for(int b: content) { fop.write(b); }
	    fop.flush();
	    fop.close();
	    // System.out.println("FileAction - sendContent: CLOSE " );
	} 
	catch (IOException e) { System.out.println("FileAction - receiveContent: " + e.getMessage()); } 
	finally { 
	    if (fop != null) { 
	        try { fop.close(); } 
	        catch (IOException ex) { System.out.println("FileAction - receiveContent (closing): " + ex.getMessage()); }
	    }
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - receiveContent //    
    
}
