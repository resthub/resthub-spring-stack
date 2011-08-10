package org.resthub.booking.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.Hotel;
import org.resthub.booking.repository.HotelRepository;
import org.resthub.core.service.GenericServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
@Named("hotelService")
public class HotelServiceImpl extends GenericServiceImpl<Hotel, Long, HotelRepository> implements HotelService {

    /**
     * {@InheritDoc}
     */
    @Inject
    @Named("hotelRepository")
    @Override
    public void setRepository(HotelRepository hotelRepository) {
        this.repository = hotelRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Hotel> find(final String query, final Pageable pageable) {
        if (query == null || query.isEmpty()) {
        	return this.findAll(pageable);
        } else {
            return this.repository.find(query, pageable);
        }
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public Page<Hotel> findAll(Pageable pageable) {
		return this.repository.findAll(pageable);
	}
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<Hotel> find(final String query) {
        if (query == null || query.isEmpty()) {
            return this.findAll();
        } else {
            Page<Hotel> resultList = this.repository.find(query, null);
            if (resultList == null) {
                return new ArrayList<Hotel>();
            } 
            return (List<Hotel>) resultList;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildIndex() {
        this.repository.rebuildIndex();
    }
}
