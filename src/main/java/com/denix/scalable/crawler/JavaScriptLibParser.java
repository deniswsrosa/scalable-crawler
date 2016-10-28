package com.denix.scalable.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
	
public class JavaScriptLibParser {
	
	public List<String> search(String url) throws IOException {
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; 
		Elements scripts = Jsoup.connect(url ).userAgent(userAgent).get()
				.select("script");
		
		List<String> results = new ArrayList<String>();
		for(Element script : scripts) {
			String lib = script.absUrl("src");
			
			if(lib !=null && !"".equals(lib.trim())) {
				results.add(lib.trim());
			}
		}
		
		return results;
	}

}
