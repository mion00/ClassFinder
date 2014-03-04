/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author creamcodifier
 */
public class DatiPolo {
    private RiepilogoPolo prospetto;
    
    public DatiPolo(String nomePolo,java.util.Date data)
    {
        prospetto=new RiepilogoPolo();
    }
    
    public RiepilogoPolo ProspettoPolo()
    {
        return prospetto;
    }
}
