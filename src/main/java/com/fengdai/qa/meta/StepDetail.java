package com.fengdai.qa.meta;

import java.util.ArrayList;
import java.util.HashMap;

public class StepDetail{
		String name;
		RequestMeta request;
		HashMap<String, String> extractors;
		ArrayList<ValidateMeta> validate;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public RequestMeta getRequest() {
			return request;
		}
		public void setRequest(RequestMeta request) {
			this.request = request;
		}
		public HashMap<String, String> getExtractors() {
			return extractors;
		}
		public void setExtractors(HashMap<String, String> extractors) {
			this.extractors = extractors;
		}
		public ArrayList<ValidateMeta> getValidate() {
			return validate;
		}
		public void setValidate(ArrayList<ValidateMeta> validate) {
			this.validate = validate;
		}
	}