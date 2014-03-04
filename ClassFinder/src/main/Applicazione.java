/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




package main;



/**
 *
 * @author mion00
 */
public class Applicazione {
    public static void main(String args[])
    {
        Applicazione app=new Applicazione();
    }
    
    Applicazione()
    {
        java.util.Date data=new java.util.Date();
        DatiPolo dati=new DatiPolo("Ingegneria",data);
        System.out.println("fine");
    }
}
