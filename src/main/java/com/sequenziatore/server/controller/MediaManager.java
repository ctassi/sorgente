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
 * 	File contentente la classe MediaManager
 * 
 *	@file		MediaManager.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

/**
 *	La classe permette di salvare i file inviati dal front-end.
 *
 *	@author 	DeSQ
 */
public class MediaManager {
	
	/**
	 * Salva il file inviato dal front-end.
	 * 
	 * @param fileItems file da salvare
	 * @param uploadPath percorso dove viene salvato il file 
	 * @return il percorso e il nome del file salvato
	 */
	public String uploadMedia(List<FileItem> fileItems , String uploadPath){
		String filePath = null;
		File fileToBeSaved = null;
		//creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		for(int i=0;i<fileItems.size();i++){
			FileItem item = (FileItem) fileItems.get(i);
			if (!item.isFormField()) {
				String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				filePath = uploadPath + File.separator + fileName;
				fileToBeSaved = new File(filePath);
				try{
					item.write(fileToBeSaved);
				    }catch(Exception e){}
				}
			}

		String imagePathForBrowser = "";
		for(int i =filePath.length(); i>0; i--){
			if(filePath.charAt(i-1) == 'q' && filePath.charAt(i-2) == 'e' && filePath.charAt(i-3) == 's'){
				for(int j = i+1; j < filePath.length(); j++){
					imagePathForBrowser = imagePathForBrowser + filePath.charAt(j);
				}

				break;
				
			}
		}
		return imagePathForBrowser;
	}
	
	/**
	 * Salva il file inviato dal front-end.
	 * 
	 * @param photo foto da salvare
	 * @param uploadPath percorso dove viene salvato il file 
	 * @return il percorso e il nome del file salvato
	 */
	public String uploadMediaBase64(String photo , String uploadPath){
        byte[] imgBytes;
        //creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String filePath = uploadPath + File.separator + fileName;
		File fileToBeSaved = new File(filePath);
		try {
			imgBytes = Base64.decodeBase64(photo.getBytes());
			FileOutputStream osf = new FileOutputStream(fileToBeSaved);
			IOUtils.write(imgBytes, osf);
			osf.flush();
			osf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		String imagePathForBrowser = "";
		for(int i =filePath.length(); i>0; i--){
			if(filePath.charAt(i-1) == 'q' && filePath.charAt(i-2) == 'e' && filePath.charAt(i-3) == 's'){
				for(int j = i+1; j < filePath.length(); j++){
					imagePathForBrowser = imagePathForBrowser + filePath.charAt(j);
				}
				break;
				
			}
		}
		return imagePathForBrowser;
	}
}
