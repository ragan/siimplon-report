package pl.siimplon.desktop;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.precision.GeometryPrecisionReducer;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.vector.ClipProcess;
import org.opengis.feature.simple.SimpleFeature;
import pl.siimplon.reporter.analyzer.AnalyzeItem;

public class SimpleFeatureAnalyzeItem implements AnalyzeItem {

    private final SimpleFeature feature;

    private final Geometry geometry;

    public SimpleFeatureAnalyzeItem(SimpleFeature feature) {
        this.feature = feature;
        this.geometry = (Geometry) feature.getDefaultGeometry();
    }

    public Object getAttribute(String name) {
        return feature.getAttribute(name);
    }

    public double getArea() {
        return geometry.getArea();
    }

    public double getLength() {
        return geometry.getLength();
    }

    public boolean crosses(AnalyzeItem item) {
        return getGeometry().crosses(getItem(item).getGeometry());
    }

    public boolean contains(AnalyzeItem item) {
        return getGeometry().contains(getItem(item).getGeometry());
    }

    public boolean covers(AnalyzeItem item) {
        return getGeometry().covers(getItem(item).getGeometry());
    }

    public boolean coveredBy(AnalyzeItem item) {
        return getGeometry().coveredBy(getItem(item).getGeometry());
    }

    public boolean intersects(AnalyzeItem item) {
        return getGeometry().intersects(getItem(item).getGeometry());
    }

    public double clipArea(AnalyzeItem item) {
        SimpleFeature clippedFeature = getClippedFeature(feature, item);
        return ((Geometry) clippedFeature.getDefaultGeometry()).getArea();
    }

    public double clipLength(AnalyzeItem item) {
        SimpleFeature clippedFeature = null;
        try {
            clippedFeature = getClippedFeature(feature, item);
        } catch (Exception e) {
            clippedFeature = null;
        }
        if (clippedFeature != null) {
            return ((Geometry) clippedFeature.getDefaultGeometry()).getLength();
        } else {
            return 0.0;
        }
    }

    public Geometry getGeometry() {
        return geometry;
    }

    private SimpleFeatureAnalyzeItem getItem(AnalyzeItem item) {
        if (!(item instanceof SimpleFeatureAnalyzeItem)) {
            throw new IllegalArgumentException("Unable to identify item object.");
        }
        return ((SimpleFeatureAnalyzeItem) item);
    }

    private SimpleFeature getClippedFeature(SimpleFeature feature, AnalyzeItem clipper) {
        Geometry otherGeom = getItem(clipper).getGeometry();
        feature.setDefaultGeometry(GeometryPrecisionReducer.reduce((Geometry) feature.getDefaultGeometry(), new PrecisionModel(1000)));
        SimpleFeatureCollection result = new ClipProcess()
                .execute(DataUtilities.collection(feature),
                        GeometryPrecisionReducer.reduce(otherGeom, new PrecisionModel(1000)), false);
        return result.features().next();
    }
}
