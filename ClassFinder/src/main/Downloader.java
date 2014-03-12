/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author mion00
 */
public class Downloader {
    
    class DownloadCallable implements Callable<String> {
    String url;
    String resp;
    
    @Override
    public String call() throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.body().text();
    }

    public DownloadCallable(String url) {
        this.url = url;
    }
    
    
}
    List<String> DownloadUrls (List<String> urls) throws InterruptedException, ExecutionException {
        
        ExecutorService service = Executors.newCachedThreadPool();
        CompletionService<String> excompl = new ExecutorCompletionService<String>(service);
  
        List<String> resp_list = new ArrayList<>();
        //////////////////////////////////////////////////////////////
        for (String s : urls) {
            DownloadCallable task= new DownloadCallable(s);;
            excompl.submit(task);
        }
        
        int nr_tasks = urls.size();
        
        for (int i=0; i < nr_tasks; i++) {
            String resp = excompl.take().get();
            System.out.println(resp+" "+i);
            resp_list.add(resp);
        }
        
        service.shutdown(); 
        
        return resp_list;
    }
}
