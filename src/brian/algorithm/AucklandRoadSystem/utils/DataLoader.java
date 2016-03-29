package brian.algorithm.AucklandRoadSystem.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import brian.Task2.RoadSystem.algorithms.Dictionary;
import brian.Task2.RoadSystem.algorithms.RoadGraph;
import brian.Task2.RoadSystem.structs.Location;
import brian.Task2.RoadSystem.structs.Road;
import brian.Task2.RoadSystem.structs.Segment;

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
	
	private static final void loadPolygons(File file) {
		
	}
}



