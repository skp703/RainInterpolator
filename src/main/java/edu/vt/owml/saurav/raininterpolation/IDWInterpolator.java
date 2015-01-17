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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author saurav
 */
public class IDWInterpolator implements RainInterpolator {

    public static String powerParam = "POWER";
    public static double defaultPower = 2;
    public static double defaultCutOff = -1;
    /**
     * power parameter of basic IDW
     * {@link http://en.wikipedia.org/wiki/Inverse_distance_weighting}
     */
    private double power;
    /**
     * Station more than cut-off distance from the evaluation point will not be
     * considered. Set this to <0 for no cutoff;
     */
    private double cutoffDistance;

    public IDWInterpolator(double power) {
        this.power = power;
        this.cutoffDistance = defaultCutOff;
    }

    public IDWInterpolator(double power, double cutoffDistance) {
        this.power = power;
        this.cutoffDistance = cutoffDistance;
    }

    public IDWInterpolator() {
        this.power = defaultPower;
        this.cutoffDistance = defaultCutOff;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getCutoffDistance() {
        return cutoffDistance;
    }

    public void setCutoffDistance(double cutoffDistance) {
        this.cutoffDistance = cutoffDistance;
    }

    /**
     * Does magic to find/ interpolate value at a given location using basic
     * inverse distance weighting
     * {@link http://en.wikipedia.org/wiki/Inverse_distance_weighting}
     *
     * @param stations is a array of coordinates where rain data is available.
     * Note that all coordinates values should be provided using an imaginary
     * box with top left corner is 0,0. X coordinate increases right and Y
     * coordinate increases downwards. See Java 2D coordinates
     * {@Link http://docs.oracle.com/javase/tutorial/2d/overview/coordinate.html}
     * @param rainValues observed rain data. Length of this array should be same
     * as the length of {@code coordinates} array
     * @param locations location[:][0] is the X and location[:][1] is Y
     * coordinates of the points where data is desired. Note this uses the same
     * reference as the first argument {@code coordinates}
     * @return rain estimates in an array for the locations
     */
    @Override
    public double[] findValueAt(double[][] stations, double[] rainValues, double[][] locations) {
        long startTime = System.currentTimeMillis();
        List<double[]> distances = getDistances(stations, locations);

        double[] values = new double[locations.length];
        for (int i = 0; i < distances.size(); i++) {
            double num = 0;
            double din = 0;
            int j = 0;
            for (double d : distances.get(i)) {
                if (d == 0) {
                    num = rainValues[j];
                    din = 1;
                    break;
                }
                num = num + rainValues[j] / Math.pow(d, power);
                din = din + 1 / Math.pow(d, power);
                j++;
            }
            values[i] = num / din;
        }
        System.out.println("FIND VALUE AT FINISHED IN: (ms)" + Long.toString(System.currentTimeMillis() - startTime));
        return values;
    }

    /**
     * compute distances for a set of locations from a set of stations.
     *
     * @param stations a two column array for station locations with [0] as X
     * coordinate and [1] as Y coordinate
     * @param locations a two column array for grid points with [0] as X
     * coordinate and [1] as Y coordinate
     * @return a list, where each element in the list is a double array with
     * same length as stations array, representing the length from the station
     * at the same index in the {@code stations} input array
     */
    public List<double[]> getDistances(double[][] stations, double[][] locations) {
        System.out.println(stations.length + " ???" + locations.length);
        long startTime = System.currentTimeMillis();
        List<double[]> distances = new ArrayList(locations.length);

        for (double[] l : locations) {
            double[] distance = new double[stations.length];
            int i = 0;
            for (double[] c : stations) {
                double dx = l[0] - c[0];
                double dy = l[1] - c[1];
                distance[i] = Math.sqrt(dx * dx + dy * dy);
                i++;
            }
            distances.add(distance);
        }
        System.out.println("FIND DISTANCE FINISHED IN: (ms)" + Long.toString(System.currentTimeMillis() - startTime));
        return distances;
    }

}
