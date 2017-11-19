package com.twilio.com;

import static spark.Spark.post;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReceiveSms {
	static String Message_Search; 
	
	static String From;
	
	static Boolean run = false;
	static Boolean ok = true;
	static Boolean Query = false;
	static Boolean CheckQuery = false;
	static Boolean weatherLoc = false;
	static Boolean CheckWeather = false;
	
	static Boolean direct = false;
	static Boolean checkDirect = false;
	
	public static void main(String[] args) {
		post("/receive-sms",(req,res) -> {
			
			Message sms = new Message.Builder()
				.body(new Body("Test"))
				.build();
			
			MessagingResponse twiml = new MessagingResponse.Builder()
				.message(sms)
				.build();
			
			From = req.queryParams("From");
			Message_Search = req.queryParams("Body");
			
			
			System.out.println("From: " + From + " Message: " + Message_Search);
		
			
			if(Query){
				CheckQuery = true;
			}
			
			if(weatherLoc){
				CheckWeather = true;
			}
			
			if(direct){
				checkDirect = true;
			}
			
			run = true;
			
			
			return twiml.toXml();
		});
		
		while(ok){
		
		while(run){
		switch(Message_Search){
			case "Start":{
				SendSMS(From,"Select one of the Menu Options: " + "\n" +  "1. Search" + "\n" + "2. Top Movies" + "\n" + "3. Current Weather" + "\n" + "4. Directions" + "\n" + "5. Request Uber");
				System.out.println("okay lol2" + From);
				break;
			}
			
			case "1":{
				SendSMS(From, "Query: ");
				Query = true;
		        break;
			}
			
			case "2":{
				try {
					Scrape.IMDB(From);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			}
			
			case "3":{
				SendSMS(From, "Location: ");
				weatherLoc = true;
				
				break;
			}
			
			case "4":{
				SendSMS(From,"Directions to: ");
				direct = true;
				
				break;
			}
			
			case "5":{
				SendSMS(From,"Still in Development. Coming Soon");
				break;
			}
			
			
			default:{
				break;
			}
		}
		
		if (CheckQuery){
			System.out.println("Truth pass");
			QuerySent(Message_Search);
		}
		
		if (CheckWeather){
			Weather(Message_Search);
		}
		
		if(checkDirect){
			Direct(Message_Search);
		}
		
		System.out.println("Thank You");
		run = false;
		}
		}
		}
	
	static void QuerySent(String Querys){
		String querySearch= Querys;
		if (BingWebSearch.subscriptionKey.length() != 32) {
            System.out.println("Invalid Bing Search API subscription key!");
            System.out.println("Please paste yours into the source code.");
            System.exit(1);
        }

        try {
            System.out.println("Searching the Web for: " + querySearch);
            
            SearchResults result = BingWebSearch.SearchWeb(querySearch);
            
            JSONObject jsonobject = new JSONObject(result.jsonResponse);
            JSONObject response = jsonobject.getJSONObject("webPages");
            JSONArray list = (JSONArray) response.get("value");
            
            for (int i =0; i<5; i++){
            String name = list.getJSONObject(i).getString("name");
            String snippet = list.getJSONObject(i).getString("snippet");
            String URL = list.getJSONObject(i).getString("url");
            System.out.println("Name: " + name + "\n" + "Snippet: " + snippet);
            String resulting = ("Article Name: " + name + "\n" + "Short Description: " + snippet + "\n");
          
            SendSMS(From,resulting);
       
            }
                
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            System.exit(1);
        }
     
        Query = false;
        CheckQuery = false;
	}
	
	static void Weather(String Location){
		String locations = Location;
		
		try {
			Scrape.Weather(From,locations);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		weatherLoc = false;
		CheckWeather = false;
	}
	
	static void Direct(String Dest){
		String destination = Dest;
		
		try {
			Direction.call_me(From,destination);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		direct = false;
		checkDirect = false;
	}
	
	@SuppressWarnings({ "deprecation", "resource" })
	static void SendSMS(String Number, String Messages){
		String Numbers = Number;
		String message = Messages;
		String sendMsg;
		
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost requests = new HttpPost("https://utils.lib.id/sms/");
		System.out.println("try:" + Numbers);
        JSONObject object = new JSONObject();
        

        try {
			object.put("to",Numbers);
			object.put("body",message);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        ;
        sendMsg= object.toString();
        requests.setEntity(new StringEntity(sendMsg,"UTF8"));
        requests.setHeader("Content-type","application/json");
        try {
			HttpResponse resp = httpclient.execute(requests);
			if (resp!=null){
				System.out.println("works");
			}
			
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
