package org.resthub.booking.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.Booking;
import org.resthub.booking.repository.BookingRepository;
import org.resthub.booking.repository.UserRepository;
import org.resthub.core.service.GenericServiceImpl;
import org.springframework.util.Assert;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
@Named("bookingService")
public class BookingServiceImpl extends GenericServiceImpl<Booking, Long, BookingRepository> implements BookingService {

    /**
     * {@inheritDoc}
     */
    @Inject
    @Named("bookingRepository")
    @Override
    public void setRepository(BookingRepository bookingRepository) {
        this.repository = bookingRepository;
    }

    @Inject
    @Named("userRepository")
    private UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    public List<Booking> findByUserId(Long userId) {
        Assert.notNull(userId, "User ID can't be null");

        return repository.findByUser(this.userRepository.findOne(userId));
    }

	@Override
	public Long getIdFromEntity(Booking booking) {
		return booking.getId();
	}
}
