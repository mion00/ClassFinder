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
import java.net.URLConnection;
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

import com.ning.http.client.*;
import java.util.Collection;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import org.slf4j.Logger;
/**
 *
 * @author creamcodifier
 */

class Corsi
{
    String indirizzo;
    String nome;
    Corsi(String indirizzo,String nome)
    {
        this.indirizzo=indirizzo;
        this.nome=nome;
    }
}
class Dipartimenti
{
    String indirizzo;
    String nome;
    List<Corsi> indirizziCorsi;
    Dipartimenti(String indirizzo,String nome)
    {
        this.indirizzo=indirizzo;
        this.nome=nome;
        indirizziCorsi=new ArrayList<>();
    }
    public void AggiungiCorso(Corsi indirizzoCorso)
    {
        indirizziCorsi.add(indirizzoCorso);
    }
}

class DownloadCallable implements Callable<Response> {
    AsyncHttpClient client;
    String url;
    Response resp;
    
    @Override
    public Response call() throws IOException, InterruptedException, ExecutionException {
        resp = client.prepareGet(url).execute().get();
        return resp;
    }

    public DownloadCallable(AsyncHttpClient client, String url) {
        this.client = client;
        this.url = url;
    }
    
    
}

class DownloadThread implements Runnable {
    ListenableFuture<Response> listfut;
    Response resp;
    
    @Override
    public synchronized void run() {
        try {
            resp = listfut.get();
            System.out.println(resp.getContentType());
        } catch (InterruptedException | ExecutionException ex) {
            java.util.logging.Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DownloadThread(ListenableFuture<Response> listfut) {
        this.listfut = listfut; 
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
    
    void openConnection(String url_string) throws IOException, InterruptedException, ExecutionException {
        ExecutorService service = Executors.newCachedThreadPool();
        CompletionService<Response> excompl = new ExecutorCompletionService<Response>(service);
        
        Collection<String> urls = new ArrayList<>();
        for (int i=0; i<100; i++) {
            urls.add("http://webapps.unitn.it/Orari/it/Web/AjaxEventi/c/10232-1/agendaWeek?_=&start=1393801200&end=1394406000");
        }
        
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        
        //////////////////////////////////////////////////////////////
        for (String s : urls) {
            DownloadCallable downloadcallable = new DownloadCallable(asyncHttpClient, s);;
            excompl.submit(downloadcallable);
        }
        
        int nr_tasks = urls.size();
        
        List<Response> list_resp = new ArrayList<>();
        
        for (int i=0; i < nr_tasks; i++) {
            Response r = excompl.take().get();
            System.out.println(r.getStatusCode()+" "+i);
            list_resp.add(r);
        }
        
        for (int i=0; i < nr_tasks;i++) {
//            System.out.println(list_resp.get(i).getStatusCode()+" "+i);
        }
        
        service.shutdown();
        /////////////////////////////////////////////////////////////
//       USING LISTENER ON FUTURE
//       AsyncHttpClient.BoundRequestBuilder request = asyncHttpClient.prepareGet(url_string); 
//        for (int i=0; i < 50; i++) {
//            
//        DownloadThread down = new DownloadThread(request.execute());
//        
//                
//        Thread t = new Thread(down);
//             
//        down.listfut.addListener(t, service);
//        }
        ///////////////////////////////////////////////////////////////////
//        for (int i=0; i< 100; i++) {    
//           excompl.submit(asyncHttpClient.prepareGet(url_string).execute(), resp);    
//        }
        
        
//        for (int i=0; i<100;i++) {
//            Future<Response> response = excompl.take();
//            System.out.println(response.get().getContentType());
//        }
        
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        List<Future<Response>> risposte = new ArrayList<>();
//        for (int i=0; i<100; i++) {
//            Future<Response> f = asyncHttpClient.prepareGet(url_string).execute();
//            risposte.add(f);
//        }
//        for (int i=0; i<100; i++) {
//            Response r = risposte.get(i).get();
//            System.out.println(r.getContentType());
//        }
         
    }
    
    private static JSONObject DownloadJSON(String url) throws Exception{
	URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setUseCaches(true);

 
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
    
    
    private List<Dipartimenti> InserisciDipartimenti(Document doc) throws Exception
    {
        List<Dipartimenti> indirizzi;
        indirizzi = new ArrayList<>();
        
        Elements labels = doc.getElementsByAttributeValue("name", "id2");

        Elements dipartimenti = labels.get(0).getElementsByTag("option");
                
        for (int i = 1; i<dipartimenti.size(); i++) {
            //indirizzi.add()
            indirizzi.add(new Dipartimenti(dipartimenti.get(i).attributes().get("value"),dipartimenti.get(i).html()));
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
    
    void InserisciCorsi(List<Dipartimenti> indirizziDipartimenti, String anno) throws Exception
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
                indirizziDipartimenti.get(i).AggiungiCorso(new Corsi(((JSONObject)json.get(i).get(j)).get("Id").toString(),((JSONObject)json.get(i).get(j)).get("Descrizione").toString()));
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
                System.out.print(" "+oggetto.get("start")+" "+oggetto.get("end"));
                System.out.println();
        }
        
    }
    
    void OttieniOrari(List<Dipartimenti> indirizziDipartimenti, String settimana)throws Exception
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
        
        openConnection("http://www.google.com");
        
        List<Aula> aulePolo = null;
        Document doc = DownloadHTML(urlIniziale);
        //List<Dipartimenti> dipartimenti=InserisciDipartimenti(doc);
        //qui inserire il filtro!
        //InserisciCorsi(dipartimenti,CalcolaAnnoAccademico(doc));
        // http://webapps.unitn.it/Orari/it/Web/AjaxEventi/c/10133-3/agendaWeek?_=&start=1393801200&end=1394406000
        //OttieniOrari(dipartimenti,"1393801200");
        
        
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

