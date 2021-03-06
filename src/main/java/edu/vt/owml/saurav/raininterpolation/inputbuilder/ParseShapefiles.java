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

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author saurav
 */
public class ParseShapefiles {

    public enum GeometryTypeString {

        POINT, LINE, POLYGON
    };

    public static ShapeFileDesc findDescription(File file) throws IOException {
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureType schema = store.getSchema();
        GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();
        GeometryType type = geometryDescriptor.getType();
        CoordinateReferenceSystem coordinateReferenceSystem = geometryDescriptor.getCoordinateReferenceSystem();
        List<AttributeDescriptor> attributeDescriptors = schema.getAttributeDescriptors();
        List<String> labels = new ArrayList();
        for (AttributeDescriptor a : attributeDescriptors) {
            labels.add(a.getLocalName());
            //labels
            System.out.println(a.getLocalName());
        }
        //projection system
//        System.out.println(coordinateReferenceSystem.getCoordinateSystem().getName().toString());
        //type
        System.out.println(parseGeometry(type));
        double w = store.getFeatureSource().getBounds().getWidth();
        double h = store.getFeatureSource().getBounds().getHeight();

        return new ShapeFileDesc(coordinateReferenceSystem == null ? null : coordinateReferenceSystem.getCoordinateSystem().getName().toString(),
                parseGeometry(type), labels, Math.sqrt(w * w + h * h));
    }

    public static List<String> readAttributes(File file, String attribute) throws IOException {
        List<String> values = new ArrayList();
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = store.getFeatureReader()) {
            while (featureReader.hasNext()) {
                SimpleFeature next = featureReader.next();
                values.add(next.getAttribute(attribute).toString());
                // values
                System.out.println(next.getAttribute(attribute).toString());
            }
        }

        return values;
    }

    private static GeometryTypeString parseGeometry(GeometryType type) {
        GeometryTypeString geometryType;
        //Geometry type
        Class<?> clazz = type.getBinding();

        if (Polygon.class.isAssignableFrom(clazz)
                || MultiPolygon.class.isAssignableFrom(clazz)) {
            geometryType = GeometryTypeString.POLYGON;

        } else if (LineString.class.isAssignableFrom(clazz)
                || MultiLineString.class.isAssignableFrom(clazz)) {
            geometryType = GeometryTypeString.LINE;

        } else {
            geometryType = GeometryTypeString.POINT;
        }
        return geometryType;
    }

    public static class ShapeFileDesc {

        String projectionSystem;
        GeometryTypeString type;
        List<String> labels;
        double distance;

        public ShapeFileDesc(String projectionSystem, GeometryTypeString type, List<String> labels, double distance) {
            this.projectionSystem = projectionSystem;
            this.type = type;
            this.labels = labels;
            this.distance = distance;
        }

        public String getProjectionSystem() {
            return projectionSystem;
        }

        public void setProjectionSystem(String projectionSystem) {
            this.projectionSystem = projectionSystem;
        }

        public GeometryTypeString getType() {
            return type;
        }

        public void setType(GeometryTypeString type) {
            this.type = type;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

    }

}
