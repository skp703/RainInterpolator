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
package edu.vt.owml.saurav.raininterpolation.inputbuilder;

import java.io.File;

/**
 *
 * @author saurav
 */
public class InputStore {

    File stationsFile, watershedFile, dataFile;
    String attributeForStationLabel;

    public File getStationsFile() {
        return stationsFile;
    }

    public void setStationsFile(File stationsFile) {
        this.stationsFile = stationsFile;
    }

    public File getWatershedFile() {
        return watershedFile;
    }

    public void setWatershedFile(File watershedFile) {
        this.watershedFile = watershedFile;
    }

    public File getDataFile() {
        return dataFile;
    }

    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public String getAttributeForStationLabel() {
        return attributeForStationLabel;
    }

    public void setAttributeForStationLabel(String attributeForStationLabel) {
        this.attributeForStationLabel = attributeForStationLabel;
    }

}
