package com.hitmenwfm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FHIRHelper {

	public List<String> getPatientNames(String name) throws IOException, JSONException {
		List<String> toReturn = new ArrayList<String>();
		JSONObject j = JsonReader.readJsonFromUrl("http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp-ro/base/Patient?name=" + name);
		JSONArray entryArray = j.getJSONArray("entry");
		for(int i = 0;i<entryArray.length();i++) {
			JSONObject obj = entryArray.getJSONObject(i);
			toReturn.add(getNameForEntry(obj));
		}
		return toReturn;
	}
	
	public String getNameForEntry(JSONObject obj) throws JSONException {
		String toReturn = "";
		JSONObject resourceObj = obj.getJSONObject("resource");
		JSONArray nameArray = resourceObj.getJSONArray("name");
		JSONObject nameObject = nameArray.getJSONObject(0);
		
		JSONArray givenArray = nameObject.getJSONArray("given");
		JSONArray familyArray = nameObject.getJSONArray("family");
		for(int i = 0;i<givenArray.length();i++) {
			toReturn += givenArray.getString(i) + " ";
		}
		for(int i = 0;i<familyArray.length();i++) {
			toReturn += familyArray.getString(i) + " ";
		}
		return toReturn.substring(0,toReturn.length()-1);
	}

	public String getPatientById(int id) throws IOException, JSONException {
		JSONObject j = JsonReader.readJsonFromUrl("http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp-ro/base/Patient?_id=" + id);
		JSONArray entryArray = j.getJSONArray("entry");
		JSONObject obj = entryArray.getJSONObject(0);
		return getNameForEntry(obj);
	}

}
