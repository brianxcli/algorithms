package brian.algorithm.AucklandRoadSystem.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import brian.algorithm.AucklandRoadSystem.algorithms.Dictionary;
import brian.algorithm.AucklandRoadSystem.algorithms.RoadGraph;
import brian.algorithm.AucklandRoadSystem.structs.Location;
import brian.algorithm.AucklandRoadSystem.structs.Polygon;
import brian.algorithm.AucklandRoadSystem.structs.Road;
import brian.algorithm.AucklandRoadSystem.structs.Segment;

public class DataLoader {
	public static final void loadFiles(DataLoaderListener listener, File nodes, File roads, File segments, File polygons) {
		final File nodeFile = nodes;
		final File roadFile = roads;
		final File segmentFile = segments;
		final File polygonFile = polygons;
		final DataLoaderListener l = listener;
		
		LoadScheduler.instance().execute(new Runnable() {
			public void run() {
				loadNodes(l, nodeFile);
				loadRoads(roadFile);
				loadSegments(segmentFile);
				loadPolygons(polygonFile);				
				l.onLoadEnd();
			}
		});
	}
	
	private static final void loadNodes(DataLoaderListener listener, File file) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] infos = line.split("\t");				
				int id = Integer.parseInt(infos[0]);				
				double lat = Double.parseDouble(infos[1]);
				double lon = Double.parseDouble(infos[2]);
				
				Location.updateAucklandCityRegion(lat, lon);
				RoadGraph.getInstance().addNode(id, lat, lon);				
			}			
			
			listener.onLoadNodeFinish();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			handleReaderClose(reader);
		}
	}
	
	private static final void loadRoads(File file) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			
			// skip the first line
			reader.readLine();
			
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				String[] infos = line.split("\t");
				
				Road road = new Road();
				road.id = Long.parseLong(infos[0]);
				road.type = Integer.parseInt(infos[1]);
				road.label = infos[2];
				road.city = infos[3];
				road.oneway = Integer.parseInt(infos[4]);
				road.speed = Integer.parseInt(infos[5]);
				road.roadclass = Integer.parseInt(infos[6]);
				road.notforcar = Integer.parseInt(infos[7]);
				road.notforpede = Integer.parseInt(infos[8]);
				road.notforbicy = Integer.parseInt(infos[9]);
				
				RoadGraph.getInstance().addRoad(road);
				Dictionary.getInstance().insert(road.label);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			handleReaderClose(reader);
		}
	}
	
	private static final void loadSegments(File file) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			
			// skip the first line
			reader.readLine();
						
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				String[] infos = line.split("\t");
				
				Segment seg = new Segment();
				
				seg.roadId = Long.parseLong(infos[0]);				
				seg.length = Double.parseDouble(infos[1]);
				seg.startNodeId = Long.parseLong(infos[2]);
				seg.endNodeId = Long.parseLong(infos[3]);
				
				int length = infos.length;
				int i = 4;
				for (; i < (length - 1); i++) {
					double lat = Double.parseDouble(infos[i++]);
					double lon = Double.parseDouble(infos[i]);
					Location location = Location.newFromLatLon(lat, lon);
					seg.coor.add(location);
				}
				
				RoadGraph.getInstance().addSegment(seg);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			handleReaderClose(reader);
		}
	}
	
	private static final void loadPolygons(File file) {
		RoadGraph graph = RoadGraph.getInstance();
		Scanner scanner = null;
		int id = 0;
		
		try {
			scanner = new Scanner(file);
			
			while (scanner.hasNextLine() && scanner.nextLine().equals("[POLYGON]")) {
				id++;
				Polygon poly = new Polygon();
				
				String read = null;
				while (!(read = scanner.nextLine()).equals("[END]")) {
					String[] attrs = read.split("=");
					if (attrs[0].equals("Type")) {
						// poly.type = Integer.valueOf(attrs[1]);
						poly.type = Integer.parseInt(attrs[1].substring(2), 16);
					} else if (attrs[0].equals("Label")) {
						poly.label = attrs[1];
					} else if (attrs[0].equals("EndLevel")) {
						poly.endLevel = Integer.valueOf(attrs[1]);
					} else if (attrs[0].equals("CityIdx")) {
						poly.cityIndex = Integer.valueOf(attrs[1]);
					} else if (attrs[0].equals("Data0")) {
						poly.data = getLocationsFromString(attrs[1]);
					}
				}
				graph.addPolygon(id, poly);
				scanner.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
	
	private static ArrayList<Location> getLocationsFromString(String data) {
		ArrayList<Location> list = new ArrayList<Location>();
		String[] locStrs = data.split(",");
		
		for (int i = 0; i < locStrs.length; i++) {
			double lat = Double.valueOf(locStrs[i].substring(1));
			i++;
			double lon = Double.valueOf(locStrs[i].substring(0, (locStrs[i].length() - 1)));
			list.add(Location.newFromLatLon(lat, lon));
		}
		
		return list;
	}
	
	private static void handleReaderClose(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				reader = null;
			}
		}
	}
}



