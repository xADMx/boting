/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author ADM
 */
public class HttpCilent {

  private String cookies;
  private final HttpClient client = HttpClientBuilder.create().build();
  private final String USER_AGENT = "Mozilla/5.0";
  private final String PublicUrl = "https://poloniex.com/public";
  private Map<String, Ticker> tiker;
  private final List<ChartData> chartdata;
  private final Map<String, PairCurrencies> paircurrencies;

    public HttpCilent() {
        this.paircurrencies = new HashMap<>();
        this.tiker = new HashMap<>();
        this.chartdata = new ArrayList<>();
    }
    
  boolean GetTicker() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        URIBuilder b;
        
        try {
            b = new URIBuilder(PublicUrl)
                    .addParameter("command", "returnTicker");
            actualObj = mapper.readTree(b.build().toURL());
         } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return false; 
        }
           
        Iterator<?> keys = actualObj.getFieldNames();
        while(keys.hasNext() ) {
            String key = (String)keys.next();
            try {
                tiker.put(key, mapper.readValue(actualObj.get(key).toString(), Ticker.class));     
            } catch (IOException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true; 
  }
  boolean GetPairCurrencies() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        URIBuilder b;
        
        try {
            b = new URIBuilder(PublicUrl)
                    .addParameter("command", "returnCurrencies");
            actualObj = mapper.readTree(b.build().toURL());
         } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return false; 
        }
           
        Iterator<?> keys = actualObj.getFieldNames();
        while(keys.hasNext() ) {
            String key = (String)keys.next();
            try {
                paircurrencies.put(key, mapper.readValue(actualObj.get(key).toString(), PairCurrencies.class));                
            } catch (IOException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true; 
  }
  boolean GetChartDataIn(String currencyPair, String start, String end, String period) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arraychartdata = null;
        URIBuilder b;

        try {
            b = new URIBuilder(PublicUrl)
                    .addParameter("command", "returnChartData")
                    .addParameter("currencyPair", currencyPair)
                    .addParameter("start", start)
                    .addParameter("end", end)
                    .addParameter("period", period);
            arraychartdata = mapper.readTree(b.build().toURL());
         } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return false; 
        }
           
        Iterator<?> chartdataiterator = arraychartdata.iterator();

        int i = 0;
        while(chartdataiterator.hasNext() ) {
            try {
                chartdata.add(mapper.readValue(chartdataiterator.next().toString(), ChartData.class));                
            } catch (IOException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;

  }
  
  Map<String, double[]> GetChartData(String currencyPair, String start, String end, String period) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arraychartdata = null;
        URIBuilder b;

        try {
            b = new URIBuilder(PublicUrl)
                    .addParameter("command", "returnChartData")
                    .addParameter("currencyPair", currencyPair)
                    .addParameter("start", start)
                    .addParameter("end", end)
                    .addParameter("period", period);
            arraychartdata = mapper.readTree(b.build().toURL());
         } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return null; 
        }
           
        Iterator<?> chartdataiterator = arraychartdata.iterator();
        Map<String, double[]> tempcd = new HashMap<>();
        double[] timeStamps = new double[arraychartdata.size()];
        double[] highData = new double[arraychartdata.size()];
        double[] lowData = new double[arraychartdata.size()];
        double[] openData = new double[arraychartdata.size()];
        double[] closeData = new double[arraychartdata.size()];
        double[] volData = new double[arraychartdata.size()];
        
        int i =0;
        while(chartdataiterator.hasNext() ) {
            try {
                ChartData temp = mapper.readValue(chartdataiterator.next().toString(), ChartData.class);
                 timeStamps[i] = temp.getDate();
                 highData[i] = temp.getHigh();
                 lowData[i] = temp.getLow();
                 openData[i] = temp.getOpen();
                 closeData[i] = temp.getClose();
                 volData[i] = temp.getVolume();
                 i++;         
            } catch (IOException ex) {
                Logger.getLogger(HttpCilent.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        tempcd.put("timeStamps", timeStamps);
        tempcd.put("highData", highData);
        tempcd.put("lowData", lowData);
        tempcd.put("openData", openData);
        tempcd.put("closeData", closeData);
        tempcd.put("volData", volData);
        
        return tempcd; 
  }
 /* public void main(String[] args) throws Exception {

	String url = "https://accounts.google.com/ServiceLoginAuth";
	String gmail = "https://mail.google.com/mail/";

	// make sure cookies is turn on
	CookieHandler.setDefault(new CookieManager());

	HttpCilent http = new HttpCilent();

	String page = http.GetPageContent(url);

	List<NameValuePair> postParams =
               http.getFormParams(page, "username","password");

	http.sendPost(url, postParams);

	String result = http.GetPageContent(gmail);
	System.out.println(result);

	System.out.println("Done");
  }
*/
    public Map<String, Ticker> getTiker() {
        return tiker;
    }

    public void setTiker(Map<String, Ticker> tiker) {
        this.tiker = tiker;
    }
  private void sendPost(String url, List<NameValuePair> postParams)
        throws Exception {

	HttpPost post = new HttpPost(url);

	// add header
	post.setHeader("Host", "accounts.google.com");
	post.setHeader("User-Agent", USER_AGENT);
	post.setHeader("Accept",
             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	post.setHeader("Accept-Language", "en-US,en;q=0.5");
	post.setHeader("Cookie", getCookies());
	post.setHeader("Connection", "keep-alive");
	post.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
	post.setHeader("Content-Type", "application/x-www-form-urlencoded");

	post.setEntity(new UrlEncodedFormEntity(postParams));

	HttpResponse response = client.execute(post);

	int responseCode = response.getStatusLine().getStatusCode();

	System.out.println("\nSending 'POST' request to URL : " + url);
	System.out.println("Post parameters : " + postParams);
	System.out.println("Response Code : " + responseCode);

	BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

	StringBuilder result = new StringBuilder();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}

	// System.out.println(result.toString());

  }

  private String GetPageContent(String url) throws Exception {

	HttpGet request = new HttpGet(url);

	request.setHeader("User-Agent", USER_AGENT);
	request.setHeader("Accept",
		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	request.setHeader("Accept-Language", "en-US,en;q=0.5");

	HttpResponse response = client.execute(request);
	int responseCode = response.getStatusLine().getStatusCode();

	System.out.println("\nSending 'GET' request to URL : " + url);
	System.out.println("Response Code : " + responseCode);

	BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

	StringBuilder result = new StringBuilder();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}

	// set cookies
	setCookies(response.getFirstHeader("Set-Cookie") == null ? "" :
                     response.getFirstHeader("Set-Cookie").toString());

	return result.toString();

  }

  public String getCookies() {
	return cookies;
  }

  public void setCookies(String cookies) {
	this.cookies = cookies;
  }

}    
