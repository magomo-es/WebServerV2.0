// - - - - - - - - - - - - - - - - - - - - -
// Archivos - Request
// PSP - UF3: Sòcols i serveis
// MaGoMo - 01/05/2020 - CEP
// - - - - - - - - - - - - - - - - - - - - -
package com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HttpRequest {

    // https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html
    // https://tools.ietf.org/html/rfc2616#section-8.2.3
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - static final attributes

    public static final String BLOCKLIMIT = "\r\n\r\n";

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - class attributes
    
    private String m_request;
    private String m_startline;
    private String m_action;
    private String m_filename;
    private String m_httpversion;
    private ArrayList<String> m_headers;
    private String m_body;
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - constructor

    HttpRequest(Socket cs) {
	this.m_request = "";

	// captura solicitud http
	try { 
	    
	    InputStream is = cs.getInputStream();
	    int content;
	    while ( ((content = is.read()) != (-1)) && (is.available()>0) ) {
		this.m_request += (char)content;
	    }
	    
	} 
	catch (IOException ex) { System.out.println("HttpRequest - IOException: " + ex.getMessage()); } 
	System.out.println("HTTP Request:\n" + this.toString() );

	// Split de mensaje por lineas
	String[] splitary = this.m_request.split("\n");
	System.out.println("splitary.length: " + splitary.length);

	// control si el mensaje tiene al menos una linea
	if (splitary.length > 0) {

	    // split de startline para captura tipo de solicitud
	    this.m_startline = splitary[0].trim();
	    String[] startlinary = this.m_startline.split(" ");
	    System.out.println("startlinary.length: " + startlinary.length);

	    // si tiene al menos 1 linea
	    if (startlinary.length > 0) {
		this.m_action = startlinary[0].trim();
		System.out.println("this.m_action: " + this.m_action);

		// control si tiene nombre de archivo
		if (startlinary.length>1) { 
		    this.m_filename = startlinary[1].trim(); 
		    System.out.println("this.m_filename: "+this.m_filename);
		
		    // control nombre archivo no vacio
		    if (this.m_filename.length()==0) { 
			this.m_filename = "\\index.html"; 
		    }
		    else if (this.m_filename.substring(this.m_filename.length()-1,this.m_filename.length()).equals("/")) { 
		        this.m_filename += "index.html"; 
		    }
		}

		// control si tiene httpversion
		if (startlinary.length>2) { this.m_httpversion = startlinary[2].trim(); }
		
		// si la accion es GET
		if (this.m_action.equals("GET") && this.m_filename!=null) { 
		    System.out.println("GET Request: " + this.m_filename );  

		    // crea el objeto de trabajo
		    FileAction thefile = new FileAction(this.m_filename, 1);
		    // control si archivo existe
		    OutputStream os;
		    String responsrStatus = "";
		    try { 
			if (thefile.m_file.exists()) {
			    responsrStatus = "HTTP/1.1 "+responseHttpStatus(200) + responseMimeType(this.m_filename) + BLOCKLIMIT;
			    System.out.println("responsrStatus: " + responsrStatus);
			    os = cs.getOutputStream(); 
			    os.write(toByteArray(responsrStatus));
			    thefile.sendContent(cs);
			} else  { 
			    os = cs.getOutputStream(); 
			    responsrStatus = "HTTP/1.1 "+responseHttpStatus(404)+ BLOCKLIMIT;
			    System.out.println("responsrStatus: " + responsrStatus);
			    os.write(toByteArray(responsrStatus));
			}
		    } 
		    catch (IOException ex) { System.out.println("HttpRequest - IOException - GET responsing: " + ex.getMessage()); }
		}
	    }
	}
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - get & set
    
    public String getRequest() { return this.m_request; }
    public String getAction() { return this.m_action; }
    public void setRequest(String request) { this.m_request = request; }
    public void setAction(String action) { this.m_action = action; }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - toString
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
	sb.append( this.m_request ).append( "\n" );
	return sb.toString();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - HTTP status return
        
    public byte[] toByteArray(String cadena) {
	byte[] retubyte = cadena.getBytes(StandardCharsets.UTF_8);
	return retubyte;
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - HTTP status return
    
    public String responseHttpStatus(int value) {
	String retu = "";
	switch (value) {
	    case 100: retu = "100 Continue"; break;
	    case 101: retu = "101 Switching Protocols"; break;
	    case 102: retu = "102 Processing"; break;
	    case 103: retu = "103 Checkpoint"; break;
	    case 200: retu = "200 OK"; break;
	    case 201: retu = "201 Created"; break;
	    case 202: retu = "202 Accepted"; break;
	    case 203: retu = "203 Non-Authoritative Information"; break;
	    case 204: retu = "204 No Content"; break;
	    case 205: retu = "205 Reset Content"; break;
	    case 206: retu = "206 Partial Content"; break;
	    case 207: retu = "207 Multi-Status"; break;
	    case 208: retu = "208 Already Reported"; break;
	    case 300: retu = "300 Multiple Choices"; break;
	    case 301: retu = "301 Moved Permanently"; break;
	    case 302: retu = "302 Found"; break;
	    case 303: retu = "303 See Other"; break;
	    case 304: retu = "304 Not Modified"; break;
	    case 305: retu = "305 Use Proxy"; break;
	    case 306: retu = "306 Switch Proxy"; break;
	    case 307: retu = "307 Temporary Redirect"; break;
	    case 308: retu = "308 Permanent Redirect"; break;
	    case 400: retu = "400 Bad Request"; break;
	    case 401: retu = "401 Unauthorized4​"; break;
	    case 402: retu = "402 Payment Required"; break;
	    case 403: retu = "403 Forbidden"; break;
	    case 404: retu = "404 Not Found"; break;
	    case 405: retu = "405 Method Not Allowed"; break;
	    case 406: retu = "406 Not Acceptable"; break;
	    case 407: retu = "407 Proxy Authentication Required"; break;
	    case 408: retu = "408 Request Timeout"; break;
	    case 409: retu = "409 Conflict"; break;
	    case 410: retu = "410 Gone"; break;
	    case 411: retu = "411 Length Required"; break;
	    case 412: retu = "412 Precondition Failed"; break;
	    case 413: retu = "413 Request Entity Too Large"; break;
	    case 414: retu = "414 Request-URI Too Long"; break;
	    case 415: retu = "415 Unsupported Media Type"; break;
	    case 416: retu = "416 Requested Range Not Satisfiable"; break;
	    case 417: retu = "417 Expectation Failed"; break;
	    case 418: retu = "418 I'm a teapot"; break;
	    case 422: retu = "422 Unprocessable Entity"; break;
	    case 423: retu = "423 Locked"; break;
	    case 424: retu = "424 Failed Dependency"; break;
	    case 425: retu = "425 Unassigned"; break;
	    case 426: retu = "426 Upgrade Required"; break;
	    case 428: retu = "428 Precondition Required"; break;
	    case 429: retu = "429 Too Many Requests"; break;
	    case 431: retu = "431 Request Header Fields Too Large"; break;
	    case 451: retu = "451 Unavailable for Legal Reasons"; break;
	    case 500: retu = "500 Internal Server Error"; break;
	    case 501: retu = "501 Not Implemented"; break;
	    case 502: retu = "502 Bad Gateway"; break;
	    case 503: retu = "503 Service Unavailable"; break;
	    case 504: retu = "504 Gateway Timeout"; break;
	    case 505: retu = "505 HTTP Version Not Supported"; break;
	    case 506: retu = "506 Variant Also Negotiates"; break;
	    case 507: retu = "507 Insufficient Storage"; break;
	    case 508: retu = "508 Loop Detected"; break;
	    case 509: retu = "509 Bandwidth Limit Exceeded"; break;
	    case 510: retu = "510 Not Extended"; break;
	    case 511: retu = "511 Network Authentication Required"; break;
	    case 512: retu = "512 Not updated"; break;
	    case 521: retu = "521 Version Mismatch"; break;
        }
	return retu;
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - HTTP status return
    
    public String responseMimeType(String filename) {

	String retu = "text/plain";
	String[] filenamary = filename.split("\\.");
	
	if (filenamary.length>0) {
	    String ext = filenamary[filenamary.length-1].toLowerCase();
	    switch (ext) {
		case "aac": retu = "audio/aac"; break;
		case "arc": retu = "application/octet-stream"; break;
		case "avi": retu = "video/x-msvideo"; break;
		case "bin": retu = "application/octet-stream"; break;
		case "bz": retu = "application/x-bzip"; break;
		case "bz2": retu = "application/x-bzip2"; break;
		case "css": retu = "text/css"; break;
		case "csv": retu = "text/csv"; break;
		case "doc": retu = "application/msword"; break;
		case "epub": retu = "application/epub+zip"; break;
		case "gif": retu = "image/gif"; break;
		case "htm": retu = "text/html"; break;
		case "html": retu = "text/html"; break;
		case "ico": retu = "image/x-icon"; break;
		case "ics": retu = "text/calendar"; break;
		case "jpeg": retu = "image/jpeg"; break;
		case "jpg": retu = "image/jpeg"; break;
		case "js": retu = "application/javascript"; break;
		case "json": retu = "application/json"; break;
		case "mid": retu = "audio/midi"; break;
		case "midi": retu = "audio/midi"; break;
		case "mpeg": retu = "video/mpeg"; break;
		case "mpkg": retu = "application/vnd.apple.installer+xml"; break;
		case "odp": retu = "application/vnd.oasis.opendocument.presentation"; break;
		case "ods": retu = "application/vnd.oasis.opendocument.spreadsheet"; break;
		case "odt": retu = "application/vnd.oasis.opendocument.text"; break;
		case "oga": retu = "audio/ogg"; break;
		case "ogv": retu = "video/ogg"; break;
		case "ogx": retu = "application/ogg"; break;
		case "pdf": retu = "application/pdf"; break;
		case "ppt": retu = "application/vnd.ms-powerpoint"; break;
		case "rar": retu = "application/x-rar-compressed"; break;
		case "rtf": retu = "application/rtf"; break;
		case "svg": retu = "image/svg+xml"; break;
		case "tar": retu = "application/x-tar"; break;
		case "tif": retu = "image/tiff"; break;
		case "tiff": retu = "image/tiff"; break;
		case "ttf": retu = "font/ttf"; break;
		case "wav": retu = "audio/x-wav"; break;
		case "weba": retu = "audio/webm"; break;
		case "webm": retu = "video/webm"; break;
		case "webp": retu = "image/webp"; break;
		case "woff": retu = "font/woff"; break;
		case "woff2": retu = "font/woff2"; break;
		case "xls": retu = "application/vnd.ms-excel"; break;
		case "xml": retu = "application/xml"; break;
		case "zip": retu = "application/zip"; break;
		case "7z": retu = "application/x-7z-compressed"; break;
	    }
	}
	return "\r\nContent-Type: " + retu;
    }
}