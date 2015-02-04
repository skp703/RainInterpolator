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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * As the name suggests DistanceStore is a class to store distances of stations
 * from grid segments. The distances are stored in a Map. See code for details.
 * When the object is created distance map is created. Reuse this class and
 * avoid multiple initiations
 *
 * @author saurav
 */
public class DistanceStore {

    private Map< String, Map<Integer, Double>> distances;

    /**
     * Get distance between station and the grid cell
     *
     * @param station station id/name
     * @param gridID integer grid ID
     * @return
     */
    double getDistance(String station, Integer gridID) {
        return distances.get(station).get(gridID);
    }

    /**
     *
     * @param idc an object with coordinates of input stations and grid cell.
     */
    public DistanceStore(InputDataCoordinates idc) {
        distances = new HashMap();
        Set<Map.Entry<String, double[]>> stationEntrySet = idc.getStations().entrySet();

        for (Map.Entry<String, double[]> m : stationEntrySet) {
            String stationkey = m.getKey();
            double[] stationC = m.getValue();
            Set<Map.Entry<Integer, double[]>> gridEntrySet = idc.getGridPoints().entrySet();
            Map<Integer, Double> gridMap = new HashMap();
            for (Map.Entry<Integer, double[]> n : gridEntrySet) {
                Integer gridkey = n.getKey();
                double[] value = n.getValue();
                gridMap.put(gridkey, IDWInterpolator.getDistance(stationC, value));
                //output for test only
                if (gridkey % 180 == 0) {
                    LOG.fine(String.format(logFormat, gridkey, stationkey, gridMap.get(gridkey)));
                    // System.out.println(gridkey + "," + stationkey + ":" + IDWInterpolator.getDistance(stationC, value));
                }
            }
            distances.put(stationkey, gridMap);
        }

    }
    private static final String logFormat = "GridID:%1$d, StationID:%2$s, Distance:%3$e";
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(DistanceStore.class.getName());

}
