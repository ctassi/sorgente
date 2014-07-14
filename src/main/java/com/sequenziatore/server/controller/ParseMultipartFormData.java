/*
 * Copyright 2014 Dainese Matteo, De Nadai Andrea, Girotto Giacomo, Pavanello Mirko, Romagnosi Nicolò, Sartoretto Massimiliano, Visentin Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 	File contentente la classe ParseMultipartFormData
 * 
 *	@file		ParseMultipartFormData.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

/**
 *	La classe ParseMultipartFormData viene utilizzata dai controller per ricavare un JSONObject da una richiesta http contenente sia valori alfanumerici che binari.
 *
 *	@author 	DeSQ
 */
public class ParseMultipartFormData implements IParser {
 
	/**
	 * Estrae le informazioni da una richiesta http contenente sia valori alfanumerici che binari e restituisce un JSONObject che le contiene.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @return il JSONObject con le informazioni della richiesta http
	 */
	public JSONObject parse(HttpServletRequest request) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		JSONObject json = new JSONObject();
		MediaManager save= new MediaManager();
		List<FileItem> fileItems = null;
		String path=null;
		try {
			fileItems = upload.parseRequest(request);
		}catch (FileUploadException e) {
		}
		
		if(fileItems == null)
			return json;
		for (int i=0;i<fileItems.size();i++) {
		    FileItem item = (FileItem) fileItems.get(i);
		    if (item.isFormField()) {
		    	json.put(item.getFieldName(),item.getString());
		    }
		}
		String uploadPath = request.getServletContext().getRealPath("")+ File.separator + json.get("IdUser") + File.separator +json.get("IdProcess");
		path=save.uploadMedia(fileItems,uploadPath);
		json.put("Photo", path);
		return json;
	}
}
 
