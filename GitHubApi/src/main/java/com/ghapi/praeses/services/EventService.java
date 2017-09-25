package com.ghapi.praeses.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.kohsuke.github.GHEventInfo;
import org.kohsuke.github.GitHub;

import com.ghapi.praeses.model.EventRequest;
import com.ghapi.praeses.model.SimpleEvent;

public class EventService {

	public static List<SimpleEvent> getPublicEvents(Object nullInput, GitHub ghClient) {
		List<GHEventInfo> events = null;
		try {
			events = ghClient.getEvents().subList(0, 15);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SimpleEvent.convertToSimpleEvents(events);
	}
	
	public static List<SimpleEvent> getEventsByUser(Object nullInput, GitHub ghClient) {
		List<GHEventInfo> events = null;
		try {
			events = ghClient.getMyself().listEvents().asList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SimpleEvent.convertToSimpleEvents(events);
	}
	
	public static SimpleEvent getEventDetails(Object request, GitHub ghClient) {
		EventRequest eventRequest = (EventRequest) request;
		List<GHEventInfo> events = null;
		try {
			events = ghClient.getUser(eventRequest.getUsername()).listEvents().asList().stream().filter(event -> event.getId() == eventRequest.getEventId())
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new SimpleEvent(events.get(0));
	}
}
