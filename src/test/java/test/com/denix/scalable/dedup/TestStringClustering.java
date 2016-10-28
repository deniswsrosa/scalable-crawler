package test.com.denix.scalable.dedup;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.denix.scalabe.dedup.Cluster;
import com.denix.scalabe.dedup.StringClustering;

public class TestStringClustering {

	@Test
	public void testPathNameCleansing() {
		
		StringClustering st = new StringClustering();
		assertEquals("jquery.js", st.nameCleansing("/javascripts/jquery.js"));	
		assertEquals("jquery.js", st.nameCleansing("../../jquery.js"));
		assertEquals("my_jquery.js", st.nameCleansing("my_jquery.js"));
		assertEquals("jquery.tablesorter.js", st.nameCleansing("/javascripts/jquery.tablesorter.js"));
	}
	
	@Test
	public void testCaseInsensitiveCleansing() {
		StringClustering st = new StringClustering();
		assertEquals("jquery.js", st.nameCleansing("/javascripts/jQuery.js"));	
		assertEquals("jquery.js", st.nameCleansing("../../JQUERY.js"));
		assertEquals("my_jquery.js", st.nameCleansing("MY_jquery.js"));
		assertEquals("jquery.tablesorter.js", st.nameCleansing("/javaSCRIPTS/jquery.tablesorter.JS"));
	}
	
	@Test
	public void testVersionInsensitiveCleansing() {
		StringClustering st = new StringClustering();
		assertEquals("jquery-...js", st.nameCleansing("/javascripts/jquery-1.5.3.js"));
		assertEquals("jquery-...min.js", st.nameCleansing("../../jquery-3.2.1.min.js"));
	}
	
	@Test
	public void testVersionCleansing() {
		StringClustering st = new StringClustering();
		st.ignoreVersioning(false);
		assertEquals("jquery-1.5.3.js", st.nameCleansing("/javascripts/jquery-1.5.3.js"));
		assertEquals("jquery-3.2.1.min.js", st.nameCleansing("../../jquery-3.2.1.min.js"));
	}
	
	
	@Test
	public void testSuccessLibNameComparisonNoVersioning() {
		StringClustering st = new StringClustering();
		assertTrue( st.isRelative("jquery-1.5.3.js", "/homee/jquery-1.5.7.js"));
		assertTrue( st.isRelative("jquery-1.5.3.js", "/js/jquery-1.5.7.min.js"));
		assertTrue( st.isRelative("jquery-2.2.4.js", "/js/jquery-1.5.7.min.js"));
	}
	
	@Test
	public void testFailLibNameComparisonNoVersioning() {
		StringClustering st = new StringClustering();
		assertFalse( st.isRelative("jquery-1.5.3.js", "/homee/jquery-1.5.3.fileupload.js"));
		assertFalse( st.isRelative("jquery-1.5.3.js", "/js/jquery-1.5.7.ui.colorbox.js"));
		assertFalse( st.isRelative("jquery-1.5.3.js", "/js/angular-1.5.3.js"));
	}
	
	@Test
	public void testSuccessLibNameComparison() {
		StringClustering st = new StringClustering();
		st.ignoreVersioning(false);
		
		assertFalse(st.isRelative("jquery-1.11.3.js","jquery-3.0.0.min.js"));
		assertTrue( st.isRelative("jquery-1.5.3.js", "/js/JQuery-1.5.2.js"));
		assertFalse( st.isRelative("jquery-1.5.3.js", "/js/JQuery-2.5.3.js"));
		assertTrue( st.isRelative("jquery-1.5.3.js", "/js/JQuery-1.6.3.js"));
	}
	
	@Test
	public void testClusteringNoVersion() {
		
		List<String> alist = new ArrayList<String>();
		alist.add("/js/JQuery-1.5.2.js");
		alist.add("/js/JQuery-2.5.js");
		alist.add("/homee/jquery-1.5.3.fileupload.js");
		alist.add("/js/jquery-1.5.7.ui.colorbox.js");
		alist.add("/js/jquery-1.4.3.ui.colorbox.js");
		alist.add("/2.0.0-beta.17/angular2.js");
		alist.add("/angular2.min.js");
		StringClustering st = new StringClustering();
		List<Cluster> clusters =  st.generateClusters(alist);
		
		for(Cluster cluster: clusters) {
			System.out.println(cluster.getOwner());
		}
		
	}




}
