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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.grid.GridElement;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.PolygonElement;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * copied from: {@link
 * http://docs.geotools.org/latest/userguide/extension/grid.html}
 *
 * @author
 */
public class IntersectionBuilder extends GridFeatureBuilder {

    final FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2();
    final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

    final FeatureSource source;
    int id = 0;

    public IntersectionBuilder(SimpleFeatureType type, FeatureSource source) {
        super(type);
        this.source = source;
    }

    @Override
    public void setAttributes(GridElement ge, Map<String, Object> attributes) {
        attributes.put("id", ++id);
    }

    @Override
    public boolean getCreateFeature(GridElement el) {
        Coordinate c = ((PolygonElement) el).getCenter();
        Geometry p = gf.createPoint(c);
        Filter filter = ff2.intersects(ff2.property("the_geom"), ff2.literal(p));
        boolean result = false;

        try {
            result = !source.getFeatures((org.opengis.filter.Filter) filter).isEmpty();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        return result;
    }

}
