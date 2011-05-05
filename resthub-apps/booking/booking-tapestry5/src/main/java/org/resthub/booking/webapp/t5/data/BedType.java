package org.resthub.booking.webapp.t5.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * Represent bed types supported by the application. Initialy copied from
 * Tapestry5 booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * This component manage i18n messages.
 * 
 * @author ccordenier
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public class BedType extends AbstractSelectModel {
	private List<OptionModel> options = new ArrayList<OptionModel>();

	public BedType(Messages messages) {
	    super();
	    
		options.add(new OptionModelImpl(messages.get("BedType.KING_SIZE"), 1));
		options.add(new OptionModelImpl(messages.get("BedType.TWO_BEDS"), 2));
		options.add(new OptionModelImpl(messages.get("BedType.THREE_BEDS"), 3));
	}

	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	public List<OptionModel> getOptions() {
		return options;
	}

}
