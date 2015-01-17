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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author saurav
 */
public class IDWInterpolatorTest {

    double[][] stations;
    double[] rainValues;
    double[][] locations;
    List<double[]> distances;
    double[] vals;
    IDWInterpolator instance;

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

    public IDWInterpolatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws FileNotFoundException, IOException, InvalidFormatException {
        instance = new IDWInterpolator();

        Workbook wb;
        wb = WorkbookFactory.create(IDWInterpolatorTest.class.getResourceAsStream("/unit_test.xlsx"));

        // retrieve the named range
        String cellname = "stations";
        int namedCellIdx = wb.getNameIndex(cellname);
        Name aNamedCell = wb.getNameAt(namedCellIdx);
        // retrieve the cell at the named range and test its contents
        AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
        org.apache.poi.ss.util.CellReference[] crefs = (org.apache.poi.ss.util.CellReference[]) aref.getAllReferencedCells();
        int index = 0;
        int columns = 2;
        stations = new double[(int) crefs.length / columns][2];
        for (org.apache.poi.ss.util.CellReference cref : crefs) {
            Sheet s = wb.getSheet(cref.getSheetName());
            Row r = s.getRow(cref.getRow());
            Cell c = r.getCell(cref.getCol());
            //  System.out.println(c.getNumericCellValue());
            //2 col array
            stations[(int) (index / columns)][index % columns] = c.getNumericCellValue();
            index++;
        }
        //printArray(stations);

        //rain
        cellname = "gridpts";
        namedCellIdx = wb.getNameIndex(cellname);
        aNamedCell = wb.getNameAt(namedCellIdx);
        // retrieve the cell at the named range and test its contents
        aref = new AreaReference(aNamedCell.getRefersToFormula());
        crefs = (org.apache.poi.ss.util.CellReference[]) aref.getAllReferencedCells();
        index = 0;
        columns = 2;
        locations = new double[(int) crefs.length / columns][2];
        for (org.apache.poi.ss.util.CellReference cref : crefs) {
            Sheet s = wb.getSheet(cref.getSheetName());
            Row r = s.getRow(cref.getRow());
            Cell c = r.getCell(cref.getCol());
            // System.out.println(c.getNumericCellValue());
            //2 col array
            locations[(int) (index / columns)][index % columns] = c.getNumericCellValue();
            index++;
        }
       // printArray(locations);

        //rain
        cellname = "rainVal";
        namedCellIdx = wb.getNameIndex(cellname);
        aNamedCell = wb.getNameAt(namedCellIdx);
        // retrieve the cell at the named range and test its contents
        aref = new AreaReference(aNamedCell.getRefersToFormula());
        crefs = (org.apache.poi.ss.util.CellReference[]) aref.getAllReferencedCells();
        index = 0;
        rainValues = new double[crefs.length];
        for (org.apache.poi.ss.util.CellReference cref : crefs) {
            Sheet s = wb.getSheet(cref.getSheetName());
            Row r = s.getRow(cref.getRow());
            Cell c = r.getCell(cref.getCol());
            //  System.out.println(c.getNumericCellValue());
            //2 col array
            rainValues[index] = c.getNumericCellValue();
            index++;
        }
      //  printArray(rainValues);

        //vals
        cellname = "estimates";
        namedCellIdx = wb.getNameIndex(cellname);
        aNamedCell = wb.getNameAt(namedCellIdx);
        // retrieve the cell at the named range and test its contents
        aref = new AreaReference(aNamedCell.getRefersToFormula());
        crefs = (org.apache.poi.ss.util.CellReference[]) aref.getAllReferencedCells();
        index = 0;
        vals = new double[crefs.length];
        for (org.apache.poi.ss.util.CellReference cref : crefs) {
            Sheet s = wb.getSheet(cref.getSheetName());
            Row r = s.getRow(cref.getRow());
            Cell c = r.getCell(cref.getCol());
            //   System.out.println(c.getNumericCellValue());
            //2 col array
            vals[index] = c.getNumericCellValue();
            index++;
        }
      //  printArray(vals);

        //distances
        cellname = "distances";
        namedCellIdx = wb.getNameIndex(cellname);
        aNamedCell = wb.getNameAt(namedCellIdx);
        // retrieve the cell at the named range and test its contents
        aref = new AreaReference(aNamedCell.getRefersToFormula());
        crefs = (org.apache.poi.ss.util.CellReference[]) aref.getAllReferencedCells();
        index = 0;
        columns = stations.length;
        distances = new ArrayList();
        double[] d = new double[stations.length];
        for (org.apache.poi.ss.util.CellReference cref : crefs) {

            Sheet s = wb.getSheet(cref.getSheetName());
            Row r = s.getRow(cref.getRow());
            Cell c = r.getCell(cref.getCol());
            //    System.out.println(c.getNumericCellValue());
            d[index % columns] = c.getNumericCellValue();
            if (index % columns == columns - 1) {
                distances.add(d);
                d = new double[stations.length];
            }
            index++;

        }
        // printArray(distances);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getDistances method, of class IDWInterpolator.
     */
    @Test
    public void testGetDistances() {
        System.out.println("getDistances");
        List<double[]> result = instance.getDistances(stations, locations);
        int i = 0;
        for (double[] d : result) {
//            printArray(distances.get(i));
//            printArray(d);
            assertArrayEquals(distances.get(i), d, 0.0001);
            i++;
        }

    }

    /**
     * Test of findValueAt method, of class IDWInterpolator.
     */
    @Test
    public void testFindValueAt() {
        System.out.println("findValueAt");
        // printArray(stations);
        // printArray(locations);
        // printArray(rainValues);
        double[] result = instance.findValueAt(stations, rainValues, locations);
        assertArrayEquals(vals, result, 0.00001);
    }

    /**
     * Test of getPower method, of class IDWInterpolator.
     */
    @Test
    @Ignore
    public void testGetPower() {
        System.out.println("getPower");
        IDWInterpolator instance = new IDWInterpolator();
        double expResult = 0.0;
        double result = instance.getPower();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPower method, of class IDWInterpolator.
     */
    @Test
    @Ignore
    public void testSetPower() {
        System.out.println("setPower");
        double power = 0.0;
        IDWInterpolator instance = new IDWInterpolator();
        instance.setPower(power);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCutoffDistance method, of class IDWInterpolator.
     */
    @Test
    @Ignore
    public void testGetCutoffDistance() {
        System.out.println("getCutoffDistance");
        IDWInterpolator instance = new IDWInterpolator();
        double expResult = 0.0;
        double result = instance.getCutoffDistance();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCutoffDistance method, of class IDWInterpolator.
     */
    @Test
    @Ignore
    public void testSetCutoffDistance() {
        System.out.println("setCutoffDistance");
        double cutoffDistance = 0.0;
        IDWInterpolator instance = new IDWInterpolator();
        instance.setCutoffDistance(cutoffDistance);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
