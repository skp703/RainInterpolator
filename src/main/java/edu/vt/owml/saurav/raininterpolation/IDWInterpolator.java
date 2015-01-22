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

import java.util.List;

/**
 *
 * @author saurav
 */
public class IDWInterpolator {

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
     *
     * @param rainValues
     * @param distances
     * @return
     */
    public double findValueAt(List<Double> rainValues, List<Double> distances) {

        double num = 0;
        double din = 0;
        int j = 0;
        for (double d : distances) {
            if (d == 0) {
                num = rainValues.get(j);
                din = 1;
                break;
            }
            num = num + rainValues.get(j) / Math.pow(d, power);
            din = din + 1 / Math.pow(d, power);
            j++;
        }
        return num / din;
    }

    /**
     *
     * @param station
     * @param location
     * @return
     */
    public static double getDistance(double[] station, double[] location) {

        double dx = station[0] - location[0];
        double dy = station[1] - location[1];
        return Math.sqrt(dx * dx + dy * dy);

    }

}
