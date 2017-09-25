package com.ghapi.praeses.controllers;

import static com.ghapi.praeses.configuration.Constants.CLIENT_ID;
import static com.ghapi.praeses.configuration.Constants.CLIENT_SECRET;
import static com.ghapi.praeses.configuration.GitHubApiConfig.gitHubRequest;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.kohsuke.github.GitHub;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ghapi.praeses.model.AuthenticatedUser;
import com.ghapi.praeses.services.UserService;

@RestController
@RequestMapping("api/users")
public class GitHubUserController {

	//Returns URL to Avatar for a given user
	@RequestMapping("/getUserAvatar")
	public String getAvatar(@RequestParam(value="username") String username,
							@RequestParam(value="token") String token) {
		return (String) gitHubRequest(username, token, UserService::getAvatar);
	}
	
	//Return List of Avatars based on a list of supplied users
	@RequestMapping("/getUserAvatarBulk")
	public List<String> getAvatars(@RequestBody List<String> users,
								   @RequestParam(value="token") String token) {
		return (List<String>) gitHubRequest(users, token, UserService::getAvatars);
	}
	
	//Return the currently authenticated username
	@RequestMapping("/current")
	public String getCurrentUser(@RequestParam(value="token") String token) {
		return (String) gitHubRequest(null, token, UserService::getCurrentUser);
	}

	@RequestMapping(value="/authenticate", method=POST)
	public AuthenticatedUser authenticate(@RequestBody String code) {
		try {
			//Configure post request to Github to exchange code for user access token
			String url = "https://github.com/login/oauth/access_token";
			HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
			HttpPost postRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			params.add(new BasicNameValuePair("client_id", CLIENT_ID));
			params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
			params.add(new BasicNameValuePair("code", code));
			postRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			//Execute post
			HttpResponse response = httpClient.execute(postRequest);
			HttpEntity entity = response.getEntity();
			
			//Convert the response to a string
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
				return getAuthenticatedUser(textBuilder.toString());	
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Sets the accessToken for future API requests and returns the username
	private AuthenticatedUser getAuthenticatedUser(String rawToken) throws IOException {
		String[] splitTokenString = rawToken.split("&");
		String parsedToken = splitTokenString[0].substring(13);
		GitHub ghClient = GitHub.connectUsingOAuth(parsedToken);
		return new AuthenticatedUser(ghClient.getMyself().getLogin(), parsedToken);
	}

}
