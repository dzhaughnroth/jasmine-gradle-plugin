/*
 * Jasmine gradle plugin - http://purrpkg-gradle.sourceforge.net
 * Copyright (C) 2011 John Charles Roth
 *
 * This is free software, licensed under the terms of the GNU GPL 
 * Version 2 or, at your option, any later version. You should have 
 * received a copy of the license with this file. See the above web address
 * for more information, or contact the Free Software Foundation, Boston, MA. 
 * It is distributed WITHOUT WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.github.dzhaughnroth.jasmine;

import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.DefaultTask;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import groovy.xml.MarkupBuilder


class HttpFileServer {

    HttpServer server;
    File rootDir;
    int port = 36018;

    HttpFileServer( File root ) {
        rootDir = root;
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        server.createContext( "/", new FileHandler( rootDir ) );
        server.start();
    }
    
    def destroy() {
        println ("Stopping");
        server.stop( 2 );
    }

    public static void main( String[] args ) {
        HttpFileServer s = new HttpFileServer( new File("/home/jroth") );
        println( "Started" );
        Thread.sleep( 20000 );
        s.destroy();
    }
}

class FileHandler implements HttpHandler {
    
    public FileHandler( File root ) {
        rootDir = root;
    }
    
    File rootDir;

    def sendString( HttpExchange t, int code, String answer ) throws IOException {
        byte[] bytes = answer.getBytes();
        t.sendResponseHeaders(code, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write( bytes );
        os.close();
    }
        
    void handle( HttpExchange hex ) {
        try {
        println( "${hex.requestMethod} ${hex.requestURI}")
        if ( "GET" != hex.requestMethod ) {
            sendString( hex, HttpURLConnection.HTTP_BAD_METHOD, "GET only" );
        }
        File f = new File( rootDir, hex.requestURI.toString().substring(1) );        
        if ( f.exists() && ! f.isDirectory() ) {
            sendString( hex, HttpURLConnection.HTTP_OK, f.getText() );
        }
        else {
            sendString( hex, HttpURLConnection.HTTP_NOT_FOUND, "No finda yora fila");
        }
        }
        catch( e ){
            e.printStackTrace();
        }
    };
}



