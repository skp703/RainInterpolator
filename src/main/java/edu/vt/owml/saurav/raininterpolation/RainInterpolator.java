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

/**
 * Any interpolator has to implement this.
 *
 * @author saurav
 */
interface RainInterpolator {

    public static double[] cannotCompute = null;

    /**
     * Does magic to find/ interpolate value at a given location.
     *
     * @param coordinates is a array of coordinates where rain data is
     * available. Note that all coordinates values should be provided using an
     * imaginary box with top left corner is 0,0. X coordinate increases right
     * and Y coordinate increases downwards. See Java 2D coordinates
     * {@Link http://docs.oracle.com/javase/tutorial/2d/overview/coordinate.html}
     * @param rainValues observed rain data. Length of this array should be same
     * as the length of {@code coordinates} array
     * @param locations location[:][0] is the X and location[:][1] is Y
     * coordinates of the points where data is desired. Note this uses the same
     * reference as the first argument {@code coordinates}
     * @return rain estimates in an array for the locations
     */
    public double[] findValueAt(double[][] coordinates, double[] rainValues, double locations[][]);
}
