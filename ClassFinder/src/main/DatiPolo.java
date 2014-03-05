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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 *
 * @author creamcodifier
 */
class IndirizziDipartimenti
{
    String indirizzo;
    String nome;
    IndirizziDipartimenti(String indirizzo,String nome)
    {
        this.indirizzo=indirizzo;
        this.nome=nome;
    }
}

class IndirizziCorsi
{
    String indirizzo;
    String nome;
    IndirizziCorsi(String indirizzo,String nome)
    {
        this.indirizzo=indirizzo;
        this.nome=nome;
    }
}

public class DatiPolo {
    private RiepilogoPolo prospetto;
    
    private final String urlIniziale="http://webapps.unitn.it/Orari/it/Web/CalendarioCds";
    
    private List<IndirizziDipartimenti> CalcolaIndirizziDipartimenti() throws Exception
    {
        List<IndirizziDipartimenti> indirizzi;
        indirizzi = new ArrayList<>();
        
        Document doc = Jsoup.connect(urlIniziale).get();
        
        Elements labels = doc.getElementsByAttributeValue("name", "id2");
        
        Elements dipartimenti = labels.get(0).getElementsByTag("option");
                
        for (int i = 1; i<dipartimenti.size(); i++) {
            //indirizzi.add()
            indirizzi.add(new IndirizziDipartimenti(dipartimenti.get(i).attributes().get("value"),dipartimenti.get(i).html()));
//            TEST
        }
        
        
        return indirizzi;
    }
    
    List<IndirizziCorsi> CalcolaIndirizziCorsi(List<IndirizziDipartimenti> indirizziDipartimenti) throws Exception
    {
        List<IndirizziCorsi> indirizzi;
        indirizzi=new ArrayList<>();
        return indirizzi;
    }
    
    public DatiPolo(String nomePolo,java.util.Date data) throws Exception
    {
        List<Aula> aulePolo = null;
        List<IndirizziDipartimenti> indirizziDipartimenti=CalcolaIndirizziDipartimenti();
        List<IndirizziCorsi> indirizziCorsi=CalcolaIndirizziCorsi(indirizziDipartimenti);
        
        prospetto=new RiepilogoPolo(aulePolo);
    }
    
    public RiepilogoPolo ProspettoPolo()
    {
        return prospetto;
    }
}

