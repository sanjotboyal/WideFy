package com.twilio.com;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrape {
	static String numbers;
	static String urls;
	static int count;
	
	Scrape(){
	}

	static void IMDB(String Number) throws Exception {
		numbers = Number;
		final Document document = Jsoup.connect("http://www.imdb.com/chart/top").get();
		
		Elements rows = document.select("table.chart.full-width tr");
		
		for(int i = 0 ; i<10; i++) {
			Element row = rows.get(i);
			final String title = row.select(".titleColumn a").text();
			final String rating = row.select(".imdbRating").text();
			
			System.out.println(title + "->Rating: " + rating);
			ReceiveSms.SendSMS(numbers, title + "->Rating: " + rating);
		}
	}
	
	static void Weather (String Number, String Location)throws Exception{
		numbers = Number;
		String locations = Location;
		
		System.out.println("okayWeather");
		
		String url = "https://thisdavej.stdlib.com/weather/?deg=C%20value&loc="+ locations + "%20value";
		final Document document = Jsoup.connect(url).ignoreContentType(true).get();
        String json = document.body().html();
        JSONObject result = new JSONObject(json);
        String temperature = result.getString("temperature");
        int temp = Integer.parseInt(temperature);
        double temps = (temp-32)*5/9;
        String Description = result.getString("skytext");
        String Day = result.getString("day");
        String location = result.getString("observationpoint");
        String humidity = result.getString("humidity")+"%";
        String windspeed = result.getString("windspeed");
        
        System.out.println("original: " + temperature + " Temp: " + temp + "Cels: " + temps);
        
        String weather = ("Weather for: " + location + " On: " + Day + "\n" + "Temperature: " + temps + " Celcius" + "\n" + "Forecast: " + Description + "\n" + "Humidity: " + humidity + "\n" + "Windspeed: " + windspeed);
        
		System.out.println(weather);
		
		ReceiveSms.SendSMS(numbers, weather);
		
	}

}
