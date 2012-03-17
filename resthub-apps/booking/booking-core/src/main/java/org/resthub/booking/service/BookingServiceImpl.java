package org.resthub.booking.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.dao.BookingDao;
import org.resthub.booking.dao.UserDao;
import org.resthub.booking.model.Booking;
import org.resthub.core.monitoring.Monitored;
import org.resthub.core.service.GenericServiceImpl;
import org.springframework.util.Assert;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
@Monitored
@Named("bookingService")
public class BookingServiceImpl extends GenericServiceImpl<Booking, Long, BookingDao> implements BookingService {

    /**
     * {@inheritDoc}
     */
    @Inject
    @Named("bookingDao")
    @Override
    public void setDao(BookingDao bookingDao) {
        this.dao = bookingDao;
    }

    @Inject
    @Named("userDao")
    private UserDao userDao;

    /**
     * {@inheritDoc}
     */
    public List<Booking> findByUserId(Long userId) {
        Assert.notNull(userId, "User ID can't be null");

        return dao.findByUser(this.userDao.readByPrimaryKey(userId));
    }

}
