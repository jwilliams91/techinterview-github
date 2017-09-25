package com.ghapi.praeses.controllers;

import static com.ghapi.praeses.configuration.GitHubApiConfig.gitHubRequest;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ghapi.praeses.model.EventRequest;
import com.ghapi.praeses.model.SimpleEvent;
import com.ghapi.praeses.services.EventService;

@RestController
@RequestMapping("api/events")
@JsonInclude(Include.NON_NULL)
public class GitHubEventController {

	//Returns 15 public events for the feed
	@RequestMapping(value = "/list")
	public List<SimpleEvent> getPublicEvents(@RequestParam(value="token") String token) {
		return (List<SimpleEvent>) gitHubRequest(null, token, EventService::getPublicEvents);
	}

	//Returns events performed by the currently authenticated user
	@RequestMapping(value = "/listByCurrentUser")
	public List<SimpleEvent> getEventsByUser(@RequestParam(value="token") String token) {
		return (List<SimpleEvent>) gitHubRequest(null, token, EventService::getEventsByUser);
	}

	//Returns Event based on username and eventId
	@RequestMapping(value = "/getEventDetails", method=POST)
	public SimpleEvent getEventDetails(@RequestBody EventRequest eventRequest,
									   @RequestParam(value="token") String token) {
		return (SimpleEvent) gitHubRequest(eventRequest, token, EventService::getEventDetails);
	}

}
