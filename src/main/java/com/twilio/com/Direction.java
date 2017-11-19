package com.twilio.com;

//// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Direction
{
 public static void call_me(String Number, String Location) throws Exception{
	 
	 String numbers = Number;
	 String locations = Location;
	 System.out.println(locations);

	 String url = "http://www.mapquestapi.com/directions/v2/route?key=hVFeNPqxGPaxUWVMBVFyTzIkyOXsRGDK&from=Clarendon%20Blvd,Arlington,VA&to=2400+S+Glebe+Rd,+Arlington,+VA";
	 final Document document = Jsoup.connect(url).ignoreContentType(true).get();
     String json = document.body().html();
     JSONObject result = new JSONObject(json);
     System.out.println(json);
     JSONObject response = result.getJSONObject("route");
     JSONArray res = (JSONArray)response.get("legs");
     JSONArray list =(JSONArray)(res.getJSONObject(0).get("maneuvers"));
     
     for(int i =0; i <list.length();i++){
     String direct =  list.getJSONObject(i).getString("narrative");
     //ReceiveSms.SendSMS(numbers,direct);
     System.out.println(direct);
     }
 }
}
