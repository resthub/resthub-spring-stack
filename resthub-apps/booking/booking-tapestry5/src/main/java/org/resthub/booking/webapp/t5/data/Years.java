package org.resthub.booking.webapp.t5.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * Used to display a calculated list of years. Tapestry5 booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * This component manage i18n messages.
 * 
 * @author ccordenier
 * 
 */
public class Years extends AbstractSelectModel {

    private List<OptionModel> options = new ArrayList<OptionModel>();

    public Years() {
        super();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 6; i++) {
            options.add(new OptionModelImpl(String.valueOf(year + i)));
        }
    }

    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    public List<OptionModel> getOptions() {
        return options;
    }

}
