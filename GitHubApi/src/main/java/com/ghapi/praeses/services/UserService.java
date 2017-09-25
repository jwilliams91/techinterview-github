package com.ghapi.praeses.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.github.GitHub;

public class UserService {
	
	public static String getAvatar(Object username, GitHub ghClient) {
		try {
			return ghClient.getUser((String)username).getAvatarUrl();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> getAvatars(Object users, GitHub ghClient) {
		List<String> avatarUrls = new ArrayList<String>();		
		for(String user: (List<String>) users) {
			try {
				avatarUrls.add(ghClient.getUser(user).getAvatarUrl());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return avatarUrls;
	}
	
	public static String getCurrentUser(Object nullInput, GitHub ghClient) {
		try {
			return ghClient.getMyself().getLogin();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
