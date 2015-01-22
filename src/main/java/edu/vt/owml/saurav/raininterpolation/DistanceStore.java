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

import edu.vt.owml.saurav.raininterpolation.inputbuilder.InputDataCoordinates;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author saurav
 */
public class DistanceStore {

    Map< String, Map<Integer, Double>> distances;

    double getDistance(String station, Integer gridID) {
        return distances.get(station).get(gridID);
    }

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
            }
            distances.put(stationkey, gridMap);

        }

    }

}
