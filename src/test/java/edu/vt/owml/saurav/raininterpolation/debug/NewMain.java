package edu.vt.owml.saurav.raininterpolation.debug;

import edu.vt.owml.saurav.raininterpolation.IDWInterpolator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;

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
/**
 *
 * @author saurav
 */
public class NewMain {

    public static void printArray(double[][] dd) {
        for (double[] d : dd) {
            for (double q : d) {
                System.out.print(q + "\t");
            }
            System.out.println("\n");
        }
    }

    public static void printArray(double[] dd) {
        for (double d : dd) {

            System.out.println(d);

        }
    }

    public static void printArray(List<double[]> dd) {
        for (double[] d : dd) {
            for (double q : d) {
                System.out.print(q + "\t");
            }
            System.out.println("\n");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            Workbook wb;
            wb = WorkbookFactory.create(NewMain.class.getResourceAsStream("/unit_test.xlsx"));

            // retrieve the named range
            String cellname = "stations";
            int namedCellIdx = wb.getNameIndex(cellname);
            Name aNamedCell = wb.getNameAt(namedCellIdx);
            // retrieve the cell at the named range and test its contents
            AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
            CellReference[] crefs = (CellReference[]) aref.getAllReferencedCells();
            int index = 0;
            int columns = 2;
            double[][] stations = new double[(int) crefs.length / columns][2];
            for (CellReference cref : crefs) {
                Sheet s = wb.getSheet(cref.getSheetName());
                Row r = s.getRow(cref.getRow());
                Cell c = r.getCell(cref.getCol());
                System.out.println(c.getNumericCellValue());
                //2 col array
                stations[(int) (index / columns)][index % columns] = c.getNumericCellValue();
                index++;
            }
            printArray(stations);

            //rain
            cellname = "gridpts";
            namedCellIdx = wb.getNameIndex(cellname);
            aNamedCell = wb.getNameAt(namedCellIdx);
            // retrieve the cell at the named range and test its contents
            aref = new AreaReference(aNamedCell.getRefersToFormula());
            crefs = (CellReference[]) aref.getAllReferencedCells();
            index = 0;
            columns = 2;
            double[][] locations = new double[(int) crefs.length / columns][2];
            for (CellReference cref : crefs) {
                Sheet s = wb.getSheet(cref.getSheetName());
                Row r = s.getRow(cref.getRow());
                Cell c = r.getCell(cref.getCol());
                System.out.println(c.getNumericCellValue());
                //2 col array
                locations[(int) (index / columns)][index % columns] = c.getNumericCellValue();
                index++;
            }
            printArray(locations);

            //rain
            cellname = "rainVal";
            namedCellIdx = wb.getNameIndex(cellname);
            aNamedCell = wb.getNameAt(namedCellIdx);
            // retrieve the cell at the named range and test its contents
            aref = new AreaReference(aNamedCell.getRefersToFormula());
            crefs = (CellReference[]) aref.getAllReferencedCells();
            index = 0;
            double[] rainValues = new double[crefs.length];
            for (CellReference cref : crefs) {
                Sheet s = wb.getSheet(cref.getSheetName());
                Row r = s.getRow(cref.getRow());
                Cell c = r.getCell(cref.getCol());
                System.out.println(c.getNumericCellValue());
                //2 col array
                rainValues[index] = c.getNumericCellValue();
                index++;
            }
            printArray(rainValues);

            //vals
            cellname = "estimates";
            namedCellIdx = wb.getNameIndex(cellname);
            aNamedCell = wb.getNameAt(namedCellIdx);
            // retrieve the cell at the named range and test its contents
            aref = new AreaReference(aNamedCell.getRefersToFormula());
            crefs = (CellReference[]) aref.getAllReferencedCells();
            index = 0;
            double[] vals = new double[crefs.length];
            for (CellReference cref : crefs) {
                Sheet s = wb.getSheet(cref.getSheetName());
                Row r = s.getRow(cref.getRow());
                Cell c = r.getCell(cref.getCol());
                System.out.println(c.getNumericCellValue());
                //2 col array
                vals[index] = c.getNumericCellValue();
                index++;
            }
            printArray(vals);

            //distances
            cellname = "distances";
            namedCellIdx = wb.getNameIndex(cellname);
            aNamedCell = wb.getNameAt(namedCellIdx);
            // retrieve the cell at the named range and test its contents
            aref = new AreaReference(aNamedCell.getRefersToFormula());
            crefs = (CellReference[]) aref.getAllReferencedCells();
            index = 0;
            columns = stations.length;
            double[] d = new double[stations.length];
            List<double[]> distances = new ArrayList();
            for (CellReference cref : crefs) {

                Sheet s = wb.getSheet(cref.getSheetName());
                Row r = s.getRow(cref.getRow());
                Cell c = r.getCell(cref.getCol());
                System.out.println(c.getNumericCellValue());
                d[index % columns] = c.getNumericCellValue();
                if (index % columns == columns - 1) {
                    distances.add(d);
                    d = new double[stations.length];
                }
                index++;

            }
            printArray(distances);

            IDWInterpolator idw = new IDWInterpolator();
            // printArray(idw.getDistances(stations, locations));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
