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
        
        

        
        DatiPolo dati=new DatiPolo("Povo1",data);
    }
}
