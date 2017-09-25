package com.ghapi.praeses.model;

public class EventRequest {
	
	private long eventId;
	private String username;
	
	public EventRequest() {
		
	}
	
	public EventRequest(long eventId, String username) {
		this.eventId = eventId;
		this.username = username;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
