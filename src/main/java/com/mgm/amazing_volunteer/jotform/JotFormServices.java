package com.mgm.amazing_volunteer.jotform;



import com.mgm.amazing_volunteer.dto.submission.SubmittedUser;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public final class JotFormServices {

	public static String getFormStatus(final String apiKey, final Long id) throws JSONException{
		JotForm client = new JotForm(apiKey);
		JSONObject form = client.getForm(id);
		JSONObject formContent = form.getJSONObject("content");
		return formContent.getString("status");
	}

	public static int getLitmitSubmission(final String apiKey, final Long id) throws JSONException {
		JotForm client = new JotForm(apiKey);
		JSONObject content = client.getFormProperty(id, "limitSubmission").getJSONObject("content");
		try {
			return Integer.parseInt(content.getString("limitSubmission"));
		} catch (Exception e) {
			return -1;
		}

	}

	public static Map<String, String> getFormStatus(final String apiKey) {
		try {
			Map<String, String> map = new HashMap<>();
			JotForm client = new JotForm(apiKey);
			JSONObject response = client.getForms();
			JSONArray listFormObject = response.getJSONArray("content");
			for (int i = 0; i < listFormObject.length(); i++){

					JSONObject item = listFormObject.getJSONObject(i);
					String url = item.getString("url");
					String status = item.getString("status");
					map.put(url, status);
			}
			return map;
		} catch (JSONException e) {
			log.error(e.toString());
			return new HashMap<>();
		}
	}
	
	public static Map<String, Integer> countEventSubmission(final String apiKey) {
		try {
			Map<String, Integer> map = new HashMap<>();
			JotForm client = new JotForm(apiKey);
			JSONObject response = client.getForms();
			JSONArray listFormObject = response.getJSONArray("content");
			for (int i = 0; i < listFormObject.length(); i++){

				JSONObject item = listFormObject.getJSONObject(i);
				String url = item.getString("url");
				int count = item.getInt("count");
				map.put(url, count);
			}
			return map;
		} catch (JSONException e) {
			log.error(e.toString());
			return new HashMap<>();
		}
	}

	public static String getFormTitle(final String apiKey, final Long formId) throws JSONException {
		JotForm client = new JotForm(apiKey);
		JSONObject content = client.getForm(formId).getJSONObject("content");
		return content.getString("title");
	}

	public static List<SubmittedUser> getFormSubmissionUsers(final String apiKey, final Long id) throws JSONException{
		final JotForm client = new JotForm(apiKey);
		final JSONObject formSubmissionsObject = client.getFormSubmissions(id);
		final JSONArray formSubmissions = formSubmissionsObject.getJSONArray("content");
		final List<JSONObject> submissionAnswers = new ArrayList<>();
		final List<SubmittedUser> submissionEmails = new ArrayList<>();
		for(int i = 0; i < formSubmissions.length(); i++){
			submissionAnswers.add(formSubmissions.getJSONObject(i).getJSONObject("answers"));
		}
		final String emailKey;
		if(submissionAnswers.size()>0){
			emailKey = findKeyForEmailField(submissionAnswers.get(0));
		}else{
			return new ArrayList<>();
		}

		for (final JSONObject answer : submissionAnswers) {
			try {

				final String emailAnswer = answer.getJSONObject(emailKey).getString("answer");
				submissionEmails.add(new SubmittedUser(emailAnswer,false));
			} catch (Exception e){
				log.error(e.getMessage());
			}

		}
		return submissionEmails;
	}

	public static String findKeyForEmailField(final JSONObject answer) throws JSONException {
		Iterator itr = answer.keys();
		while(itr.hasNext()){
			Object element = itr.next();
			final JSONObject object = answer.getJSONObject(element.toString());
			if(object.getString("type").equals("control_email")){
				return element.toString();
			}
		}
		return null;
	}
}
