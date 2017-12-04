package com.fengdai.qa.meta;

import java.util.ArrayList;
import java.util.HashMap;

public class StepDetail{
		String name;
		RequestMeta request;
		HashMap<String, String> responsebind;
		HashMap<String, String> requestbind;
		HashMap<String, String> requesthandler;
		HashMap<String, String> responsehandler;
		ArrayList<ValidateMeta> validate;


		public HashMap<String, String> getResponsebind() {
			return responsebind;
		}
		public void setResponsebind(HashMap<String, String> responsebind) {
			this.responsebind = responsebind;
		}
		public HashMap<String, String> getRequestbind() {
			return requestbind;
		}
		public void setRequestbind(HashMap<String, String> requestbind) {
			this.requestbind = requestbind;
		}
		public HashMap<String, String> getRequesthandler() {
			return requesthandler;
		}
		public void setRequesthandler(HashMap<String, String> requesthandler) {
			this.requesthandler = requesthandler;
		}
		public HashMap<String, String> getResponsehandler() {
			return responsehandler;
		}
		public void setResponsehandler(HashMap<String, String> responsehandler) {
			this.responsehandler = responsehandler;
		}
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
		public ArrayList<ValidateMeta> getValidate() {
			return validate;
		}
		public void setValidate(ArrayList<ValidateMeta> validate) {
			this.validate = validate;
		}
		
		public String toString(){
			return "url:"+request.getUrl()+"---"+"method:"+request.getMethod()+"---Parameters:"+request.getParams()+"----Jsondata:"+request.getJsondata();
		}
	}