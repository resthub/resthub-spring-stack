package org.resthub.booking.webapp.t5.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * Used to display a list of months in card expiracy select list. Tapestry5
 * booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * This component manage i18n messages.
 * 
 * @author ccordenier
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public class Months extends AbstractSelectModel {

    private List<OptionModel> options = new ArrayList<OptionModel>();

    public Months(Messages messages) {
        super();

        options.add(new OptionModelImpl(messages.get("Months.january"), 1));
        options.add(new OptionModelImpl(messages.get("Months.february"), 2));
        options.add(new OptionModelImpl(messages.get("Months.march"), 3));
        options.add(new OptionModelImpl(messages.get("Months.april"), 4));
        options.add(new OptionModelImpl(messages.get("Months.may"), 5));
        options.add(new OptionModelImpl(messages.get("Months.june"), 6));
        options.add(new OptionModelImpl(messages.get("Months.july"), 7));
        options.add(new OptionModelImpl(messages.get("Months.august"), 8));
        options.add(new OptionModelImpl(messages.get("Months.september"), 9));
        options.add(new OptionModelImpl(messages.get("Months.october"), 10));
        options.add(new OptionModelImpl(messages.get("Months.november"), 11));
        options.add(new OptionModelImpl(messages.get("Months.december"), 12));
    }

    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    public List<OptionModel> getOptions() {
        return options;
    }

}