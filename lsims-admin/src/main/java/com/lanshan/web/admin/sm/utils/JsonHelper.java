package com.lanshan.web.admin.sm.utils;

import com.google.gson.Gson;

public class JsonHelper {
	static Gson gson = new Gson();

	public static String parser(Object obj) {
		return gson.toJson(obj);
	}
}
