package org.resthub.booking.repository;

import org.resthub.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface HotelRepository extends JpaRepository<Hotel, Long>, HotelRepositoryCustom {

}
