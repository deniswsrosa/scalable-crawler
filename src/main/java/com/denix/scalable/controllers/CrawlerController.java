package com.denix.scalable.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.denix.scalabe.dedup.Cluster;
import com.denix.scalable.crawler.JavascriptLibCrawler;

@RestController
public class CrawlerController {

	@RequestMapping(value = "/searchLibs", method = RequestMethod.GET)
	public List<Cluster> searchLibs(@RequestParam(value = "term") String term) {
		try {
			return new JavascriptLibCrawler().rankLibsByTerm(term);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@RequestMapping(value = "/searchLibsResult", method = RequestMethod.GET)
	public String searchLibsResult(@RequestParam(value = "term") String term,
			@RequestParam(value = "limit", defaultValue = "5") Integer limit,
			@RequestParam(value = "ignoreVersioning", defaultValue = "true") Boolean ignoreVersioning) {
		
		try {
			StringBuffer sb = new StringBuffer("");
			List<Cluster> clusters = new JavascriptLibCrawler(ignoreVersioning).rankLibsByTerm(term);

			if (clusters.size() > limit) {
				clusters = clusters.subList(0, limit);
			}

			// rendering results
			int rankingCounter = 1;
			for (Cluster cluster : clusters) {
				sb.append("<br/>");
				sb.append("<h3>" + rankingCounter + ") " + cluster.getOwner() + "</h3><hr/><b>occurences ="
						+ (cluster.getRelatives().size()) + "</b><br>");
				for (String relative : cluster.getRelatives()) {
					sb.append("--" + relative + "<br/>");
				}
				rankingCounter++;
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Could not find the most used libs for the term " + e.getMessage();
		}
	}

}
