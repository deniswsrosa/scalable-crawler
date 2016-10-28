package com.denix.scalabe.dedup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringClustering {

	private boolean ignoreVersioning = true;
	public double NO_VERSIONING_THRESHOLD = 0.88;
	public double VERSIONING_THRESHOLD = 0.94;

	public void ignoreVersioning(boolean ignore) {
		this.ignoreVersioning = ignore;
	}

	public String nameCleansing(String libName) {

		String newLibName = removeLibPath(libName.toLowerCase());
		if (ignoreVersioning) {
			return removeVersioning(newLibName);
		}
		return newLibName;
	}

	private String removeVersioning(String lib) {
		if (lib == null) {
			return lib;
		}
		return lib.replaceAll("[0-9]", "");
	}

	private String removeLibPath(String libPath) {
		if (libPath == null || libPath.trim().lastIndexOf("/") == -1) {
			return libPath;
		}
		String lib = libPath.substring(libPath.trim().lastIndexOf("/") + 1);

		if (lib.contains("?")) {
			return lib.substring(0, lib.indexOf("?"));
		} else {
			return lib;
		}
	}

	public boolean isRelative(String relative, String target) {

		double comparison = StringUtils.getJaroWinklerDistance(nameCleansing(relative), nameCleansing(target));
		
		double penalty = 0.0;

		// the first number usually is the version number, so if the number is
		// not equal in both
		// strings we add a penalty to the threshold
		if (!ignoreVersioning) {
			Matcher matcher1 = Pattern.compile("\\d").matcher(relative);
			Matcher matcher2 = Pattern.compile("\\d").matcher(target);
			
			if (matcher1.find() && matcher2.find() && Integer.valueOf(matcher1.group()) != Integer.valueOf(matcher2.group())) {
				penalty = 0.05;
			}
		}

		double threshold = penalty + (ignoreVersioning ? NO_VERSIONING_THRESHOLD : VERSIONING_THRESHOLD);
		if (comparison >= threshold) {
			return true;
		} else {
			return false;
		}
	}

	public List<Cluster> generateClusters(List<String> libs) {
		List<Cluster> clusters = new ArrayList<Cluster>();

		if (libs == null || libs.size() == 0) {
			return clusters;
		}

		clusters.add(new Cluster(removeLibPath(libs.get(0).toLowerCase())));
		libs.remove(0);

		Iterator<String> it = libs.iterator();

		while (it.hasNext()) {

			String target = it.next();

			boolean newCluster = true;
			for (Cluster cluster : clusters) {
				if (isRelative(cluster.getOwner(), target)) {
					newCluster = false;
					cluster.getRelatives().add(removeLibPath(target));
					it.remove();
					break;
				}
			}

			if (newCluster) {
				Cluster cluster = new Cluster(removeLibPath(target.toLowerCase()));
				clusters.add(cluster);
			}

		}

		// sorting by cluster size
		Comparator<Cluster> comparator = new Comparator<Cluster>() {
			@Override
			public int compare(Cluster c1, Cluster c2) {
				return c2.getRelatives().size() - c1.getRelatives().size(); // use
																			// your
																			// logic
			}
		};
		Collections.sort(clusters, comparator);
		return clusters;
	}
}
