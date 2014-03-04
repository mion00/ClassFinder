/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
 
import javax.net.ssl.HttpsURLConnection;


/**
 *
 * @author creamcodifier
 */
public class DatiPolo {
    private RiepilogoPolo prospetto;
    
    private final String urlIniziale="http://webapps.unitn.it/Orari/it/Web/CalendarioCds";
    
    public DatiPolo(String nomePolo,java.util.Date data)
    {
        prospetto=new RiepilogoPolo();
    }
    
    public RiepilogoPolo ProspettoPolo()
    {
        return prospetto;
    }
}
