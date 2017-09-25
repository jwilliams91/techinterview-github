package com.ghapi.praeses.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.kohsuke.github.GHEventInfo;
import org.kohsuke.github.GitHub;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ghapi.praeses.model.SimpleEvent;

@RestController
@RequestMapping("api/events")
@JsonInclude(Include.NON_NULL)
public class GitHubEventController {

	private static GitHub ghClient;

	//Returns 15 public events for the feed
	@RequestMapping(value = "/list")
	public List<SimpleEvent> getPublicEvents() throws IOException {
		List<GHEventInfo> events = ghClient.getEvents().subList(0, 15);
		return SimpleEvent.convertToSimpleEvents(events);
	}

	//Returns events performed by the currently authenticated user
	@RequestMapping(value = "/listByCurrentUser")
	public List<SimpleEvent> getEventsByUser() throws IOException {
		List<GHEventInfo> events = ghClient.getMyself().listEvents().asList();
		return SimpleEvent.convertToSimpleEvents(events);
	}

	//Returns Event based on username and eventId
	@RequestMapping(value = "/getEventDetails")
	public SimpleEvent getEventDetails(@RequestParam(value = "username") String username,
			@RequestParam(value = "eventId") long eventId) throws IOException {
		List<GHEventInfo> events = ghClient.getUser(username).listEvents().asList().stream().filter(event -> event.getId() == eventId)
				.collect(Collectors.toList());
		return new SimpleEvent(events.get(0));
	}

	public static void setGhClient(GitHub ghClient) {
		GitHubEventController.ghClient = ghClient;
	}

}
