/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
 
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 *
 * @author creamcodifier
 */

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
class IndirizziDipartimenti
{
    String indirizzo;
    String nome;
    List<IndirizziCorsi> indirizziCorsi;
    IndirizziDipartimenti(String indirizzo,String nome)
    {
        this.indirizzo=indirizzo;
        this.nome=nome;
        indirizziCorsi=new ArrayList<>();
    }
    public void AggiungiCorso(IndirizziCorsi indirizzoCorso)
    {
        indirizziCorsi.add(indirizzoCorso);
    }
}



public class DatiPolo {
    private RiepilogoPolo prospetto;
    final private String urlRichiestePost="http://webapps.unitn.it/Orari/it/Web/AjaxCds";
    final String urlIniziale="http://webapps.unitn.it/Orari/it/Web/CalendarioCds"; 
    
    private Document DownloadHTML(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        return doc;
    }
    
    private static JSONObject DownloadJSON(String url) throws Exception{
	URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

 
	// Send post request
	con.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
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
        con.disconnect();
        
    
	return (JSONObject)JSONValue.parse(response.toString());
 
}
    
    private String CalcolaAnnoAccademico(Document doc) {
        Elements options = doc.getElementsByAttributeValue("name", "id");
        
        Elements anni = options.get(0).getElementsByTag("option");
              
        Elements annoselected = anni.get(0).getElementsByAttributeValue("selected", "selected");

        String anno_id= annoselected.get(0).attributes().get("value");
        
        return anno_id;
    }
    
    
    private List<IndirizziDipartimenti> CalcolaIndirizziDipartimenti(Document doc) throws Exception
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
    
    
    String MandaRichiestaPostCorsi(String parametri) throws Exception
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
    
    void CalcolaIndirizziCorsi(List<IndirizziDipartimenti> indirizziDipartimenti, String anno) throws Exception
    {

        List<JSONArray> json;
        
        json=new ArrayList<>();
        
        Object obj; 

       for(int i=0;i<indirizziDipartimenti.size();i++)
       {
           obj=JSONValue.parse(MandaRichiestaPostCorsi("id="+anno+"&id2="+indirizziDipartimenti.get(i).indirizzo));
           json.add((JSONArray)obj);
       }
       for(int i=0;i<json.size();i++)
       {
            for(int j=0;j<json.get(i).size();j++)
            {
                indirizziDipartimenti.get(i).AggiungiCorso(new IndirizziCorsi(((JSONObject)json.get(i).get(j)).get("Id").toString(),((JSONObject)json.get(i).get(j)).get("Descrizione").toString()));
            }
       }
    }
    
    void OttieniOrarioCorso(String indirizzoCorso,int anno,String inizioSettimana,String fineSettimana) throws Exception
    {
        String url="http://webapps.unitn.it/Orari/it/Web/AjaxEventi/c/"+indirizzoCorso+"-"+anno+"/agendaWeek?_=&start="+inizioSettimana+"&end="+fineSettimana;
        JSONObject json=DownloadJSON(url);
        System.out.println(url);
        JSONArray array=(JSONArray)json.get("Eventi");
        JSONObject oggetto;
        for(int i=0;i<array.size();i++)
        {
            oggetto=(JSONObject)array.get(i);
            if(oggetto!=null)
                System.out.print(oggetto.get("title").toString());
                System.out.println(" "+oggetto.get("start")+" "+oggetto.get("end"));
                System.out.println();
        }
        
    }
    
    void OttieniOrari(List<IndirizziDipartimenti> indirizziDipartimenti, String settimana)throws Exception
    {
        for(int j=0;j<indirizziDipartimenti.size();j++)
        {
            for(int i=0;i<indirizziDipartimenti.get(j).indirizziCorsi.size();i++)
            {
                for(int l=1;l<=5;l++)
                    OttieniOrarioCorso(indirizziDipartimenti.get(j).indirizziCorsi.get(i).indirizzo,l,"1393801200","1394406000");
            }
        }
    }
    
    public DatiPolo(String nomePolo,java.util.Date data) throws Exception
    {
        List<Aula> aulePolo = null;
        Document doc = DownloadHTML(urlIniziale);
        
        List<IndirizziDipartimenti> dipartimenti=CalcolaIndirizziDipartimenti(doc);
        //qui inserire il filtro!
        CalcolaIndirizziCorsi(dipartimenti,CalcolaAnnoAccademico(doc));
        // http://webapps.unitn.it/Orari/it/Web/AjaxEventi/c/10133-3/agendaWeek?_=&start=1393801200&end=1394406000
        OttieniOrari(dipartimenti,"1393801200");
        
        /*for(int i=0;i<dipartimenti.size();i++)
       {
           System.out.println(dipartimenti.get(i).nome+" "+dipartimenti.get(i).indirizzo);
            for(int j=0;j<dipartimenti.get(i).indirizziCorsi.size();j++)
            {
                System.out.println("      -"+dipartimenti.get(i).indirizziCorsi.get(j).nome+" "+dipartimenti.get(i).indirizziCorsi.get(j).indirizzo);
            }
       }*/
        
        prospetto=new RiepilogoPolo(aulePolo);
    }
    
    public RiepilogoPolo ProspettoPolo()
    {
        return prospetto;
    }
}

