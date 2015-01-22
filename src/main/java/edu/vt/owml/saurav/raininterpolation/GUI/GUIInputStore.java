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
package edu.vt.owml.saurav.raininterpolation.GUI;

import edu.vt.owml.saurav.raininterpolation.inputbuilder.InputDataCoordinates;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author saurav
 */
public class GUIInputStore {

    File stationsFile, watershedFile, dataFile;
    String attributeForStationLabel;
    int gridNumber;
    Date startDate, endDate;
    int numberOfThreads;
    int power;
    String rainTable, resultTable, summaryTable;
    List<Long> dates;
    InputDataCoordinates idc;
    List<Integer> grids;
    double maxDistance;

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

    public int getGridNumber() {
        return gridNumber;
    }

    public void setGridNumber(int gridNumber) {
        this.gridNumber = gridNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getRainTable() {
        return rainTable;
    }

    public void setRainTable(String rainTable) {
        this.rainTable = rainTable;
    }

    public String getResultTable() {
        return resultTable;
    }

    public void setResultTable(String resultTable) {
        this.resultTable = resultTable;
    }

    public List<Long> getDates() {
        return dates;
    }

    public void setDates(List<Long> dates) {
        this.dates = dates;
    }

    public InputDataCoordinates getIdc() {
        return idc;
    }

    public void setIdc(InputDataCoordinates idc) {
        this.idc = idc;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean showViewGrid() {
        return stationsFile != null && watershedFile != null && dataFile != null && attributeForStationLabel != null;

    }

    public boolean showGenerateDaily() {
        return stationsFile != null && watershedFile != null && dataFile != null && attributeForStationLabel != null && idc != null;
    }

    public List<Integer> getGrids() {
        return grids;
    }

    public void setGrids(List<Integer> grids) {
        this.grids = grids;
    }

    public String getSummaryTable() {
        return summaryTable;
    }

    public void setSummaryTable(String summaryTable) {
        this.summaryTable = summaryTable;
    }

    @Override
    public String toString() {
        return "GUIInputStore{" + "stationsFile=" + stationsFile + ", watershedFile=" + watershedFile + ", dataFile=" + dataFile + ", attributeForStationLabel=" + attributeForStationLabel + ", gridNumber=" + gridNumber + ", startDate=" + startDate + ", endDate=" + endDate + ", numberOfThreads=" + numberOfThreads + ", power=" + power + ", rainTable=" + rainTable + ", resultTable=" + resultTable + ", dates=" + dates + ", idc=" + idc + '}';
    }

}
