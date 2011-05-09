package org.resthub.booking.webapp.t5.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.resthub.booking.model.Hotel;
import org.resthub.booking.service.HotelService;

/**
 * Start page of application. Inspirated from Tapestry5 booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author ccordenier
 */
public class Search {

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@Inject
	private Request request;

	@Inject
	@Property
	private Block hotelsGridBlock;

	@InjectComponent
	private Zone result;

	@Inject
	@Service("hotelService")
	private HotelService hotelService;

	@SuppressWarnings("unused")
	@Property
	private Hotel hotel;

	@SuppressWarnings("unused")
	@Property
	@Persist
	private List<Hotel> hotels;

	@Property
	private Integer searchSize;

	@Property
	private String searchValue;

	public void onActivate() {
		this.searchSize = 5;
	}

	public Boolean onActivate(Integer searchSize) {
		this.searchSize = searchSize;
		return true;
	}

	public Integer onPassivate() {
		return this.searchSize;
	}

	/**
	 * @return customized model for the datagrid
	 */
	public BeanModel<Hotel> getGridModel() {
		BeanModel<Hotel> model = this.beanModelSource.createDisplayModel(
				Hotel.class, this.messages);
		model.add("cityState", null);
		model.add("actions", null);
		model.include("name", "address", "cityState", "zip", "actions");
		return model;
	}

	public Object onSuccess() {
		this.hotelService.rebuildIndex();
		this.prepareSearchValueForQuery();
		this.hotels = this.hotelService.find(this.searchValue);
		return this.hotelsGridBlock;
	}

	/**
	 * @return re-formated query for searching
	 */
	private String prepareSearchValueForQuery() {
		if (this.searchValue == null) {
			this.searchValue = formatQueryForEmptySearch();
		} else {
			this.searchValue = addQueryWildcardsForPartialSearch();
		}
		return this.searchValue;
	}

	/**
	 * @return re-formated empty query
	 */
	private String formatQueryForEmptySearch() {
		if (this.searchValue == null) {
			return "";
		}
		return this.searchValue;
	}

	/**
	 * @return re-formated query by adding wildcards
	 */
	private String addQueryWildcardsForPartialSearch() {
		return this.searchValue + "*";
	}

	/**
	 * Manage event on text serach in order to use with ZoneUpdater mixin
	 * 
	 * @return block to update
	 */
	public Object onSearchValueChanged() {
		this.hotelService.rebuildIndex();
		this.searchValue = request.getParameter("param");

		if (this.searchValue != null) {
			this.prepareSearchValueForQuery();
			this.hotels = new ArrayList<Hotel>();
			this.hotels = this.hotelService.find(this.searchValue);
			return this.result.getBody();
		}
		return null;
	}

}
