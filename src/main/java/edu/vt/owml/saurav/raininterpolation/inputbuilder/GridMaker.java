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

import edu.vt.owml.saurav.raininterpolation.GUI.GUIInputStore;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Envelopes;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.Grids;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.TextSymbolizer;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author saurav
 */
public class GridMaker {

    private int gridNumber;
    GUIInputStore input;
    MapContent map;
    FeatureSource grid, points;
    InputDataCoordinates idc;

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    public GridMaker(GUIInputStore input) {
        this.input = input;
        this.gridNumber = input.getGridNumber();
        map = new MapContent();
        idc = new InputDataCoordinates();
    }

    private static double getGridSizeEstimate(ReferencedEnvelope envelope, int divideby) throws IOException {
        return (Math.min(Math.abs(envelope.getMaxX() - envelope.getMinX()) / divideby, Math.abs(envelope.getMaxY() - envelope.getMinY()) / divideby));
    }

    public void displayShapeFile() throws IOException, FactoryException, MismatchedDimensionException, TransformException {

        // create layer and render watershed
        FileDataStore store = FileDataStoreFinder.getDataStore(input.getWatershedFile());
        FeatureSource featureSource = store.getFeatureSource();
        Style style = createStyle(featureSource);
        Layer layer = new FeatureLayer(featureSource, style);
        //MapContent map = new MapContent(featureSource.getSchema().getGeometryDescriptor().getType().getCoordinateReferenceSystem());
        layer.setTitle("Watershed");
        map.addLayer(layer);

        //create grid layer
        double gridSize = getGridSizeEstimate(featureSource.getBounds(), gridNumber);
        ReferencedEnvelope gridBounds = Envelopes.expandToInclude(featureSource.getBounds(), gridSize);
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("grid");
        tb.add(GridFeatureBuilder.DEFAULT_GEOMETRY_ATTRIBUTE_NAME,
                Polygon.class, gridBounds.getCoordinateReferenceSystem());
        tb.add("id", Integer.class);
        SimpleFeatureType TYPE = tb.buildFeatureType();
        GridFeatureBuilder builder = new IntersectionBuilder(TYPE, featureSource);
        grid = Grids.createHexagonalGrid(gridBounds, gridSize, -1, builder);
        style = createPolygonStyle2();
        TextSymbolizer ts = styleFactory.createTextSymbolizer();
        ts.setFill(styleFactory.createFill(
                filterFactory.literal(Color.GRAY),
                filterFactory.literal(0.5)));
        ts.setFont(styleFactory.getDefaultFont());
        AttributeExpressionImpl ae = new AttributeExpressionImpl("id");
        //TODO change label placemant
        ts.setLabel(ae);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(ts);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        style.featureTypeStyles().add(fts);
        layer = new FeatureLayer(grid, style);
        layer.setTitle("Grid");
        map.addLayer(layer);

        // Create  layer and render points
        store = FileDataStoreFinder.getDataStore(input.getStationsFile());
        points = store.getFeatureSource();
        //getCenters(featureSource);
        style = createStyle(points);
        ts = styleFactory.createTextSymbolizer();
        ts.setFill(styleFactory.createFill(
                filterFactory.literal(Color.BLACK),
                filterFactory.literal(1)));
        ts.setFont(styleFactory.getDefaultFont());
        ae = new AttributeExpressionImpl(input.getAttributeForStationLabel());
        ts.setLabel(ae);
        rule = styleFactory.createRule();
        rule.symbolizers().add(ts);
        fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        style.featureTypeStyles().add(fts);

        layer = new FeatureLayer(points, style);
        layer.setTitle("Stations");
        map.addLayer(layer);

        // Now display the map
        map.setTitle("Watershed, Stations, and Grid");
        JMapFrame show = new JMapFrame(map);
        // do not close all application on close
        show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        show.setSize(900, 600);
        // zoom in, zoom out, pan, show all
        show.enableToolBar(true);
        // location of cursor and bounds of current
        show.enableStatusBar(true);
        // list layers and set them as visible + selected
        show.enableLayerTable(true);
        // display
        show.setVisible(true);
        show.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    saveCenters();
                } catch (IOException | FactoryException | MismatchedDimensionException | TransformException ex) {
                    Logger.getLogger(GridMaker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    private void saveCenters() throws IOException, FactoryException, MismatchedDimensionException, TransformException {
        // put grid
        FeatureCollection features = grid.getFeatures();
        FeatureIterator it = features.features();
        while (it.hasNext()) {
            Feature next = it.next();
            Geometry geometry2 = (Geometry) ((SimpleFeature) next).getDefaultGeometry();
            System.out.println(((SimpleFeature) next).getAttribute("id") + ": " + geometry2.getCentroid().getX() + "\t" + geometry2.getCentroid().getY());
            idc.addGridPoints((int) ((SimpleFeature) next).getAttribute("id"), geometry2.getCentroid().getX(), geometry2.getCentroid().getY());
        }

        //transform as put grid
        CoordinateReferenceSystem dataCRS = points.getBounds().getCoordinateReferenceSystem();
        CoordinateReferenceSystem worldCRS = grid.getBounds().getCoordinateReferenceSystem();
        boolean lenient = true; // allow for some error due to different datums
        MathTransform transform = CRS.findMathTransform(dataCRS, worldCRS, lenient);
        features = points.getFeatures();
        it = features.features();
        while (it.hasNext()) {
            Feature next = it.next();
            Geometry geometry = (Geometry) ((SimpleFeature) next).getDefaultGeometry();
            Geometry geometry2 = JTS.transform(geometry, transform);
            System.out.print(((SimpleFeature) next).getAttribute(input.getAttributeForStationLabel()) + ": " + geometry.getCentroid().getX() + "\t" + geometry.getCentroid().getY() + "\t");
            System.out.println(geometry2.getCentroid().getX() + "\t" + geometry2.getCentroid().getY());
            idc.addStation((String) ((SimpleFeature) next).getAttribute(input.getAttributeForStationLabel()), geometry2.getCentroid().getX(), geometry2.getCentroid().getY());
        }

    }

    /**
     * This methods works out what sort of feature geometry we have in the
     * shapefile and then delegates to an appropriate style creating method.
     */
    private Style createStyle(FeatureSource featureSource) {
        SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();

        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            return createPolygonStyle();

        } else if (LineString.class.isAssignableFrom(geomType)
                || MultiLineString.class.isAssignableFrom(geomType)) {
            return createLineStyle();

        } else {
            return createPointStyle();
        }
    }

    /**
     * Create a Style to draw polygon features with a thin blue outline and a
     * cyan fill
     */
    private Style createPolygonStyle() {

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.CYAN),
                filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    private Style createPolygonStyle2() {

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.GRAY),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.BLACK),
                filterFactory.literal(0.1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a Style to draw line features as thin blue lines
     */
    private Style createLineStyle() {
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a Style to draw point features as circles with blue outlines and
     * cyan fill
     */
    private Style createPointStyle() {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(styleFactory.createStroke(
                filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.ORANGE)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    public int getGridSize() {
        return gridNumber;
    }

    public void setGridSize(int gridSize) {
        this.gridNumber = gridSize;
    }

    public GUIInputStore getInput() {
        return input;
    }

    public void setInput(GUIInputStore input) {
        this.input = input;
    }

    public int getGridNumber() {
        return gridNumber;
    }

    public void setGridNumber(int gridNumber) {
        this.gridNumber = gridNumber;
    }

    public MapContent getMap() {
        return map;
    }

    public void setMap(MapContent map) {
        this.map = map;
    }

    public FeatureSource getGrid() {
        return grid;
    }

    public void setGrid(FeatureSource grid) {
        this.grid = grid;
    }

    public FeatureSource getPoints() {
        return points;
    }

    public void setPoints(FeatureSource points) {
        this.points = points;
    }

    public InputDataCoordinates getIdc() {
        return idc;
    }

    public void setIdc(InputDataCoordinates idc) {
        this.idc = idc;
    }

    public static StyleFactory getStyleFactory() {
        return styleFactory;
    }

    public static void setStyleFactory(StyleFactory styleFactory) {
        GridMaker.styleFactory = styleFactory;
    }

    public static FilterFactory getFilterFactory() {
        return filterFactory;
    }

    public static void setFilterFactory(FilterFactory filterFactory) {
        GridMaker.filterFactory = filterFactory;
    }

    public static void main(String[] args) throws IOException, FactoryException, MismatchedDimensionException, TransformException {
        GUIInputStore input = new GUIInputStore();
        input.setStationsFile(new File("C:\\watershed_analysis\\Rain\\project\\input\\GIS\\rainPoints.shp"));
        input.setWatershedFile(new File("C:\\watershed_analysis\\Rain\\project\\input\\GIS\\occWatPolyNAD83.shp"));
        input.setAttributeForStationLabel("STA");
        new GridMaker(input).displayShapeFile();
    }

}
