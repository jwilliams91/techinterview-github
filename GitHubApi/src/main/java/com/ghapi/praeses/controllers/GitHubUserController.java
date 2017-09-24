package com.ghapi.praeses.controllers;

import static com.ghapi.praeses.configuration.Constants.CLIENT_ID;
import static com.ghapi.praeses.configuration.Constants.CLIENT_SECRET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.kohsuke.github.GHPerson;
import org.kohsuke.github.GitHub;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ghapi.praeses.configuration.GitHubApiConfig;

@RestController
@RequestMapping("api/users")
public class GitHubUserController {

	private static GitHub ghClient;
	
	
	@RequestMapping("/get")
	public GHPerson getUser(@RequestParam(value="username") String username) throws IOException {
			return ghClient.getUser(username);
       
	}
	
	@RequestMapping("/getUserAvatar")
	public String getAvatar(@RequestParam(value="username") String username) throws IOException {
		return ghClient.getUser(username).getAvatarUrl();
	}
	
	@RequestMapping("/getUserAvatarBulk")
	public List<String> getAvatars(@RequestBody List<String> users) throws IOException {
		List<String> avatarUrls = new ArrayList<String>();
		for(String user: users) {
			avatarUrls.add(ghClient.getUser(user).getAvatarUrl());
		}
		return avatarUrls;
	}
	
	@RequestMapping("/current")
	public String getCurrentUser() throws IOException {
		return ghClient.getMyself().getLogin();
	}

	@RequestMapping(value="/authenticate", method=POST)
	public String authenticate(@RequestBody String code) {
		try {
			String url = "https://github.com/login/oauth/access_token";
			HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
			HttpPost postRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			params.add(new BasicNameValuePair("client_id", CLIENT_ID));
			params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
			params.add(new BasicNameValuePair("code", code));
			postRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			HttpResponse response = httpClient.execute(postRequest);
			HttpEntity entity = response.getEntity();
			
			if(entity != null) {
				InputStream inputStream = entity.getContent();
				StringBuilder textBuilder = new StringBuilder();
			    try (Reader reader = new BufferedReader(new InputStreamReader
			      (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
			        int c = 0;
			        while ((c = reader.read()) != -1) {
			            textBuilder.append((char) c);
			        }
			    }
				return setTokenForClient(textBuilder.toString());	
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String setTokenForClient(String rawToken) throws IOException {
		String[] splitTokenString = rawToken.split("&");
		String parsedToken = splitTokenString[0].substring(13);
		GitHubApiConfig.updateClientForControllers(GitHub.connectUsingOAuth(parsedToken));
		return ghClient.getMyself().getLogin();
	}

	public static void setGhClient(GitHub ghClient) {
		GitHubUserController.ghClient = ghClient;
	}
}
