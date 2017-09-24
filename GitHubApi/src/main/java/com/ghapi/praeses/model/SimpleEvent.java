package com.ghapi.praeses.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kohsuke.github.GHEventInfo;

public class SimpleEvent {

	private long eventId;
	private String username;
	private String eventType;
	private String repo;
	private String avatarUrl;
	private Date createdAt;

	public SimpleEvent(GHEventInfo event) {
		try {
			this.eventId = event.getId();
			this.username = event.getActorLogin();
			this.eventType = event.getType().toString();
			this.repo = event.getRepository().getFullName();
			this.avatarUrl = event.getActor().getAvatarUrl();
			this.createdAt = event.getCreatedAt();

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public static List<SimpleEvent> convertToSimpleEvents(List<GHEventInfo> events) {
		List<SimpleEvent> simpleEvents = new ArrayList<SimpleEvent>();
		for (GHEventInfo e : events) {
			simpleEvents.add(new SimpleEvent(e));
		}
		return simpleEvents;
	}
}