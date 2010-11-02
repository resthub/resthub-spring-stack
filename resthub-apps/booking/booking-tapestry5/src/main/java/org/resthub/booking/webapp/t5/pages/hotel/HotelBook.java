package org.resthub.booking.webapp.t5.pages.hotel;

import java.util.Calendar;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.resthub.booking.model.Booking;
import org.resthub.booking.model.Hotel;
import org.resthub.booking.model.User;
import org.resthub.booking.service.BookingService;
import org.resthub.booking.service.UserService;
import org.resthub.booking.webapp.t5.data.BedType;
import org.resthub.booking.webapp.t5.data.Months;
import org.resthub.booking.webapp.t5.data.Years;
import org.resthub.booking.webapp.t5.pages.Search;

/**
 * Layout component for pages of resthub tapestry5-booking sample application.
 * Inspirated by Tapestry5 booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author ccordenier
 */
public class HotelBook {

	@Inject
	private Messages messages;

	@Inject
	@Service("userService")
	private UserService userService;

	@Inject
	@Service("bookingService")
	private BookingService bookingService;

	@Property
	@PageActivationContext
	private Hotel hotel;

	@Inject
	private Block bookBlock;

	@Inject
	private Block confirmBlock;

	@Property
	@Persist
	private Booking booking;

	@InjectComponent
	private Form bookForm;

	@Persist
	private boolean confirm;

	@SuppressWarnings("unused")
	@Property
	private SelectModel bedType = new BedType(this.messages);

	@SuppressWarnings("unused")
	@Property
	private SelectModel years = new Years();

	@SuppressWarnings("unused")
	@Property
	private SelectModel months = new Months(messages);

	/**
	 * Manage confirmation
	 * 
	 * @return confirmation block is we are in a confirmation phase, current
	 *         block otherwise
	 */
	public Block getStep() {
		return this.confirm ? this.confirmBlock : this.bookBlock;
	}

	public String getSecuredCardNumber() {
		return this.booking.getCreditCardNumber().substring(12);
	}

	public void onActivate() {
		if (this.booking == null) {
			List<User> users = this.userService.findAll();
			this.booking = new Booking(hotel, users.get(0), 1, 1);
		}
	}

	public Object onSuccessFromConfirmForm() {
		// Create
		this.bookingService.create(this.booking);

		// Return to search
		return Search.class;
	}

	@OnEvent(value = "cancelConfirm")
	public void cancelConfirm() {
		this.confirm = false;
	}

	public void onValidateFromBookForm() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		if (this.booking.getCheckinDate().before(calendar.getTime())) {
			this.bookForm
					.recordError(this.messages.get("checkInNotFutureDate"));
			return;
		} else if (!this.booking.getCheckinDate().before(
				this.booking.getCheckoutDate())) {
			this.bookForm.recordError(messages.get("checkOutBeforeCheckIn"));
			return;
		}

		this.confirm = true;
	}

}
