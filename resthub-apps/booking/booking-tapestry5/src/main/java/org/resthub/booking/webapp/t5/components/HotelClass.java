package org.resthub.booking.webapp.t5.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;

/**
 * Use this component with beandisplay to display an images for hotel's class.
 * It will display the image that corresponds to the number of stars of the
 * hotel.
 * 
 * @author ccordenier
 */
public class HotelClass {
    private static final String PATH_TO_STARS_IMAGES = "/static/images/%d-star.gif";

    @Parameter(required = true)
    @Property
    private Long stars;

    @Inject
    private AssetSource assetSource;

    /**
     * Get path to star image in function of the number of stars
     */
    public Asset getHotelClass() {
        return assetSource.getContextAsset(
                String.format(PATH_TO_STARS_IMAGES, stars), null);
    }
}
