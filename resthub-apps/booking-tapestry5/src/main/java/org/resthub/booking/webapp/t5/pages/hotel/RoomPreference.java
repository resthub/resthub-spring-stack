package org.resthub.booking.webapp.t5.pages.hotel;

public enum RoomPreference {
	KING_SIZE(1), TWO_BEDS(2), THREE_BEDS(3);

	private int value;

	RoomPreference(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
