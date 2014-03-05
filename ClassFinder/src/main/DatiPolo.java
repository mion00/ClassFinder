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
import java.util.ArrayList;
import java.util.List;
 
import javax.net.ssl.HttpsURLConnection;


/**
 *
 * @author creamcodifier
 */

public class DatiPolo {
    private RiepilogoPolo prospetto;
    
    private final String urlIniziale="http://webapps.unitn.it/Orari/it/Web/CalendarioCds";
    
    private List<Integer> CalcolaIndirizzi(String nomePolo) throws Exception
    {
        List<Integer> indirizzi=new ArrayList<Integer>();
        
        URL sitoOrari = new URL(urlIniziale);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(sitoOrari.openStream()));

        String codiceSito;
        
        //print di debug
        while ((codiceSito = buffer.readLine()) != null)
            System.out.println(codiceSito);
        buffer.close();
        
        switch(nomePolo)
        {
            case "Povo1":
                indirizzi.add(1);
                break;
        }
        return indirizzi;
    }
    
    
    public DatiPolo(String nomePolo,java.util.Date data) throws Exception
    {
        List<Aula> aulePolo = null;
        List<Integer> indirizzi=CalcolaIndirizzi(nomePolo);
  
        System.out.println(indirizzi.size());
        
        prospetto=new RiepilogoPolo(aulePolo);
    }
    
    public RiepilogoPolo ProspettoPolo()
    {
        return prospetto;
    }
}
