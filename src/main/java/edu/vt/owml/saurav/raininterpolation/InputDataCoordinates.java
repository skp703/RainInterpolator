/*
 * Copyright (C) 2015 saurav
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.vt.owml.saurav.raininterpolation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder for stations and gridPoints Maps. Points are represented by a 2
 * element double array.
 *
 * @author saurav
 */
public class InputDataCoordinates implements Serializable {

    Map<String, double[]> stations;
    Map<Integer, double[]> gridPoints;

    public InputDataCoordinates() {
        stations = new HashMap();
        gridPoints = new HashMap();
    }

    public void addStation(String id, double X, double Y) {
        stations.put(id, new double[]{X, Y});
    }

    public void addStation(int id, double X, double Y) {
        stations.put(Integer.toString(id), new double[]{X, Y});
    }

    public void addStation(double id, double X, double Y) {
        stations.put(Double.toString(id), new double[]{X, Y});
    }

    public void addGridPoints(int id, double X, double Y) {
        gridPoints.put(id, new double[]{X, Y});
    }

    public Map<String, double[]> getStations() {
        return stations;
    }

    public void setStations(Map<String, double[]> stations) {
        this.stations = stations;
    }

    public Map<Integer, double[]> getGridPoints() {
        return gridPoints;
    }

    public void setGridPoints(Map<Integer, double[]> gridPoints) {
        this.gridPoints = gridPoints;
    }

}
