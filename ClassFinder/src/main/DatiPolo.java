/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
 
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
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
    private static Document doc;
    
    private Document DownloadHTML() throws IOException {
        
        final String urlIniziale="http://webapps.unitn.it/Orari/it/Web/CalendarioCds";    

        doc = Jsoup.connect(urlIniziale).get();

        return doc;
    }
    
    private String CalcolaAnnoAccademico() {
        Elements options = doc.getElementsByAttributeValue("name", "id");
        
        Elements anni = options.get(0).getElementsByTag("option");
              
        Elements annoselected = anni.get(0).getElementsByAttributeValue("selected", "selected");

        String anno_id= annoselected.get(0).attributes().get("value");
        
        return anno_id;
    }
    
    
    private List<IndirizziDipartimenti> CalcolaIndirizziDipartimenti() throws Exception
    {
        List<IndirizziDipartimenti> indirizzi;
        indirizzi = new ArrayList<>();
        
        Elements labels = doc.getElementsByAttributeValue("name", "id2");

        Elements dipartimenti = labels.get(0).getElementsByTag("option");
                
        for (int i = 1; i<dipartimenti.size(); i++) {
            //indirizzi.add()
            indirizzi.add(new IndirizziDipartimenti(dipartimenti.get(i).attributes().get("value"),dipartimenti.get(i).html()));
//            TEST
        }
        
        
        return indirizzi;
    }
    
    
    String MandaRichiestaPost(String parametri) throws Exception
    {
        
        URL obj = new URL(urlRichiestePost);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

 
	// Send post request
	con.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	wr.writeBytes(parametri);
	wr.flush();
	wr.close();
 
	int responseCode = con.getResponseCode();
 
	BufferedReader in = new BufferedReader(
	        new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();
 
        while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
        in.close();
        return response.toString();
    }
    
    List<IndirizziCorsi> CalcolaIndirizziCorsi(List<IndirizziDipartimenti> indirizziDipartimenti, String anno) throws Exception
    {
        List<IndirizziCorsi> indirizzi;
        List<JSONObject> json;
        indirizzi=new ArrayList<>();
        json=new ArrayList<>();
        
       for(int i=1;i<indirizziDipartimenti.size();i++)
       {
           //json.add(new JSONObject(MandaRichiestaPost("id="+anno+"&id2="+indirizziDipartimenti.get(i).indirizzo)));
           
       }
 
	//add reuqest header
	
        return indirizzi;
    }
    
    public DatiPolo(String nomePolo,java.util.Date data) throws Exception
    {
        List<Aula> aulePolo = null;
        doc = DownloadHTML();
        
        List<IndirizziDipartimenti> indirizziDipartimenti=CalcolaIndirizziDipartimenti();
        List<IndirizziCorsi> indirizziCorsi=CalcolaIndirizziCorsi(indirizziDipartimenti,"2013");
        
        prospetto=new RiepilogoPolo(aulePolo);
    }
    
    public RiepilogoPolo ProspettoPolo()
    {
        return prospetto;
    }
}

