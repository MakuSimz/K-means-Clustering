/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imsproject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucks
 */
public class CSVDoc {

    private List<String> wifIds = new ArrayList<>();
    private List<String> wifiName = new ArrayList<>();
    private List<Double> wifiGeoX = new ArrayList<>();
    private List<Double> wifiGeoY = new ArrayList<>();
    private List<Point> wifiGeoPoints = new ArrayList<>();
    public static double highestX = 100000;
    public static double lowestX = -1;
    public static double highestY = 100000;
    public static double lowestY = -1;

    public void readCSV(String filename) {
        File file = new File(filename);

        //File file = new File("Filename.csv"); 
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                Point point = new Point();
                String[] array = line.split(";");
                String[] linetext = array[0].split(",");
                wifIds.add(linetext[0].toString());
                wifiName.add(linetext[1].toString());
                if (!linetext[2].toString().equals("x")) {
                    wifiGeoX.add(Double.parseDouble(linetext[2].toString()));
                    wifiGeoY.add(Double.parseDouble(linetext[3].toString()));
                    point.setX(Double.parseDouble(linetext[2].toString()));
                    point.setY(Double.parseDouble(linetext[3].toString()));
                    wifiGeoPoints.add(point);
                    if (Double.parseDouble(linetext[2].toString()) < highestX) {
                        highestX = Double.parseDouble(linetext[2].toString());
                    } else if (Double.parseDouble(linetext[2].toString()) > lowestX) {
                        lowestX = Double.parseDouble(linetext[2].toString());
                    } else if (Double.parseDouble(linetext[3].toString()) < highestY) {
                        highestY = Double.parseDouble(linetext[3].toString());
                    } else if (Double.parseDouble(linetext[3].toString()) > lowestY) {
                        lowestY = Double.parseDouble(linetext[3].toString());
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVDoc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<String> getWifIds() {
        return wifIds;
    }

    public List<String> getWifiName() {
        return wifiName;
    }

    public List<Double> getWifiGeoX() {
        return wifiGeoX;
    }

    public List<Double> getWifiGeoY() {
        return wifiGeoY;
    }

    public List<Point> getWifiGeoPoints() {
        return wifiGeoPoints;
    }

    @Override
    public String toString() {
        return "CSVDoc{" + "wifIds=" + wifIds + ", wifiName=" + wifiName + ", wifiGeoX=" + wifiGeoX + ", wifiGeoY=" + wifiGeoY + ", wifiGeoPoints=" + wifiGeoPoints + '}';
    }

}
