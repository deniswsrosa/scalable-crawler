package com.denix.scalable.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleParser {
	
	private String defaultSearchURL =  "http://www.google.com/search?q=";

	public List<String> search(String query) throws UnsupportedEncodingException, IOException {
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; 

		Elements links = Jsoup.connect(defaultSearchURL + URLEncoder.encode(query, "UTF-8")).userAgent(userAgent).get()
				.select(".g>.r>a");
		
		List<String> results = new ArrayList<String>();

		for (Element link : links) {
			String url = link.absUrl("href"); 
			// "http://www.google.com/url?q=<URL>&sa=U&ei=<someKey>".
			url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

			//removing adds
			if (!url.startsWith("http")) {
				continue; 
			}
			results.add(url);
		}
		
		return results;
	}

	public String getDefaultSearchURL() {
		return defaultSearchURL;
	}

	public void setDefaultSearchURL(String defaultSearchURL) {
		this.defaultSearchURL = defaultSearchURL;
	}
	
}
