package com.denix.scalable.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.denix.scalabe.dedup.Cluster;
import com.denix.scalabe.dedup.StringClustering;

public class JavascriptLibCrawler {
	
	private Boolean ignoreVersioning;
	
	public JavascriptLibCrawler() {}
	
	public JavascriptLibCrawler(Boolean ignoreVersioning) {
		this.ignoreVersioning = ignoreVersioning;
	}

	public List<Cluster> rankLibsByTerm(String query) throws Exception {

		List<String> urls = new GoogleParser().search(query);

		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<List<String>>> futures = new ArrayList<Future<List<String>>>();

		urls.forEach(url -> futures.add(executor.submit(new CallableCrawler(url))));

		List<String> foundLibs = new ArrayList<>();

		for (Future<List<String>> future : futures) {
			foundLibs.addAll(future.get());
		}

		StringClustering sc = new StringClustering();
		sc.ignoreVersioning(ignoreVersioning);
		return sc.generateClusters(foundLibs);
	}

	class CallableCrawler implements Callable<List<String>> {

		private String url;

		public CallableCrawler(String url) {
			this.url = url;
		}

		public List<String> call() throws Exception {
			return new JavaScriptLibParser().search(url);
		}

	}
}
