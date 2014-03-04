/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author mion00
 */
public class Applicazione {
    public static void main(String args[]) throws Exception
    {
        Applicazione app=new Applicazione();
    }
    
    Applicazione() throws Exception
    {
        java.util.Date data=new java.util.Date();
        
        Document doc = Jsoup.connect("http://webapps.unitn.it/Orari/it/Web/CalendarioCds").get();
        
        Elements labels = doc.getElementsByAttributeValue("name", "id2");
        
        Elements dipartimenti = labels.get(0).getElementsByTag("option");
        
                
        for (int i = 0; i<dipartimenti.size(); i++) {
            System.out.println(dipartimenti.get(i).attributes() + " " + dipartimenti.get(i).html());
//            TEST
        }

        
        DatiPolo dati=new DatiPolo("Povo1",data);
    }
}
