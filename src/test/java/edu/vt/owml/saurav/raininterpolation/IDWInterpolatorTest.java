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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author saurav
 */
public class IDWInterpolatorTest {

    public IDWInterpolatorTest() {
    }

    @Before
    public void setUp() {
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

    /**
     * Test of findValueAt method, of class IDWInterpolator.
     */
    @Test
    public void testFindValueAt() {
        System.out.println("findValueAt");
        List<Double> rainValues = null;
        List<Double> distances = null;
        IDWInterpolator instance = new IDWInterpolator();
        double expResult = 0.0;
        double result = instance.findValueAt(rainValues, distances);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDistance method, of class IDWInterpolator.
     */
    @Test
    @Ignore
    public void testGetDistance() {
        System.out.println("getDistance");
        double[] station = null;
        double[] location = null;
        double expResult = 0.0;
        double result = IDWInterpolator.getDistance(station, location);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
