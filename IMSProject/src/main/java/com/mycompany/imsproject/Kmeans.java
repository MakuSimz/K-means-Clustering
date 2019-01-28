/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imsproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 *
 * @author lucks
 */
public class Kmeans {

    public Point[] CentroidsPos = null;
    public Point[] oldCentroidsPos = null;
    

    private double calculateEucledianDistance(Point point1, Point point2) {
        double distance;
        distance = Math.sqrt(Math.pow((point1.getX() - point2.getX()), 2) + Math.pow((point1.getY() - point2.getY()), 2));
        return distance;
    }

    private Point[] randomPlaceCentroids(int k) {
        Point[] rndPoints = new Point[k];
        for (int i = 0; i < k; i++) {
            Point point = new Point();
            double x = getRandomNumberInRange(CSVDoc.lowestX, CSVDoc.highestX);
            double y = getRandomNumberInRange(CSVDoc.lowestY, CSVDoc.highestY);
            System.out.println(x + "," + y);
            point.setX(x);
            point.setY(y);

            rndPoints[i] = point;
        }

        return rndPoints;
    }

    private double getRandomNumberInRange(double min, double max) {

        if (min <= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    private MultiValueMap<Integer, Point> placePointInCentroid(Point[] Centroids, List<Point> wifiGeoPoints) {
        //ArrayList<Point>[] clustersList = new ArrayList[Centroids.length];
        MultiValueMap<Integer, Point> clustersList = new MultiValueMap<>();
        for (Point point : wifiGeoPoints) {
            double minDistance = 10000000.0;
            int selectedCentroid = 0;
            for (int i = 0; i < Centroids.length; i++) {
                double distance = calculateEucledianDistance(Centroids[i], point);
                //System.out.println("Distance: "+distance);
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedCentroid = i;
                }
            }
            // System.out.println("Point: " + point.toString() + " Is close to Centroid: " + selectedCentroid);
            clustersList.put(selectedCentroid, point);

        }
        return clustersList;
    }

    public Point calculateClusterMean(int key, MultiValueMap<Integer, Point> clustersList) {
        Point newPoints = new Point();
        double sumX = 0.0;
        double sumY = 0.0;
        double averageX = 0.0;
        double averageY = 0.0;
        int lstSize = 0;

        List<Point> list;
        list = (List) clustersList.get(key);
       // System.out.println(">>>>>>>>>>Centroid " +key+": "+ list.toString());
        lstSize = list.size();
        //System.out.println(">>>>>>>>>>Size " +lstSize);
        for (int j = 0; j < lstSize; j++) {
            // System.out.println("\t" + key + "\t  " + list.get(j));
            sumX = sumX + list.get(j).getX();
            sumY = sumY + list.get(j).getY();

        }
        averageX = sumX / lstSize;
        averageY = sumY / lstSize;
        newPoints.setX(averageX);
        newPoints.setY(averageY);
        return newPoints;
    }

    public Point[] calculateNewClusterPosition(int k, MultiValueMap<Integer, Point> clustersList) {
        Point[] newPoints = new Point[k];
        for (int i = 0; i < k; i++) {
            newPoints[i] = calculateClusterMean(i, clustersList);
        }
        return newPoints;
    }

    public MultiValueMap<Integer, Point> clusterData(int k, List<Point> wifiGeoPoints, int iterations,double err) {
        CentroidsPos = randomPlaceCentroids(k);
       
        MultiValueMap<Integer, Point> clustersList = placePointInCentroid(CentroidsPos, wifiGeoPoints);

        System.out.println("Lowest Value of X: " + CSVDoc.highestX);
        System.out.println("Highest Value of X: " + CSVDoc.lowestX);
        System.out.println("Lowest Value of Y: " + CSVDoc.highestY);
        System.out.println("Highest Value of Y: " + CSVDoc.lowestY);

        System.out.println("Total Classes: " + clustersList.size());
        System.out.println("==============================Before K-means:================================= ");
        for (int key : clustersList.keySet()) {

            System.out.println(key + " - [" + clustersList.get(key) + "]");
        }
         while(clustersList.size()!=k){
            System.out.println("Retrying Genrating Centroid Position and clusters");
            CentroidsPos = randomPlaceCentroids(k); 
            clustersList = placePointInCentroid(CentroidsPos, wifiGeoPoints);
        }
         oldCentroidsPos=CentroidsPos;
        for (int i = 0; i < iterations; i++) {
            CentroidsPos = calculateNewClusterPosition(k, clustersList);
            System.out.println("Centroid Position:"+CentroidsPos[1].toString());
            clustersList = placePointInCentroid(CentroidsPos, wifiGeoPoints);
            System.out.println("Diff: "+calculateEucledianDistance(oldCentroidsPos[0], CentroidsPos[0]));
            if(calculateEucledianDistance(oldCentroidsPos[0], CentroidsPos[0])==err){
                if(calculateEucledianDistance(oldCentroidsPos[1], CentroidsPos[1])==err){
                    
                    if(calculateEucledianDistance(oldCentroidsPos[2], CentroidsPos[2])==err){
                         System.out.println("Stopped ar Iteration: " + i );
                        break;
                    }
                }
                
            }
            oldCentroidsPos=CentroidsPos;
           

        }
        System.out.println("==============================After K-means:================================= ");
        List<Point> list;
        for (int key : clustersList.keySet()) {
            list=(List)clustersList.get(key);
            System.out.println("Size of Cluster: "+key+" = "+list.size());
            System.out.println(key + " - [" + clustersList.get(key) + "]");
        }
        System.out.println("==============================After K-means Centroid new Positions:================================= ");
        for (int x = 0; x < CentroidsPos.length; x++) {
            System.out.println("[" + CentroidsPos[x].getX() + "," + CentroidsPos[x].getY() + "]");
        }
        return clustersList;
    }
}
