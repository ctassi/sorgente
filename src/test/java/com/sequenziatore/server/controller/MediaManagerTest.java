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
 * 	File contentente la classe MediaManagerTest
 * 
 *	@file		MediaManagerTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.Test;


/**
 *	Classe contenente tutti i test di unità dei metodi della classe MediaManager.
 *
 *	@author 	DeSQ
 */
public class MediaManagerTest {

	/**
	 *  Test del metodo uploadMedia() che si occupa dell'upload dei file multimediali.
	 */
	@Test
	public void testUploadMedia() {
		MediaManager mediaManagerObject = new MediaManager();
		
		FileItemFactory factory = new DiskFileItemFactory(100000, null);
        String textFieldName = "testField";
        String fileName = "testFileName";
        String textContentType = "text/plain";
        String uploadPath = File.separator;

        FileItem item = factory.createItem(
                textFieldName,
                textContentType,
                true,
                fileName
        );
        item.setFormField(false);
        List<FileItem> fileItemList = new ArrayList<FileItem>();
        fileItemList.add(item);
        
        String returnedString = mediaManagerObject.uploadMedia(fileItemList, uploadPath);
        
        assertNotNull(returnedString);
	}

	/**
	 *  Test del metodo uploadMediaBase64() che si occupa dell'upload dei file multimediali.
	 */
	@Test
	public void testUploadMediaBase64() {
		MediaManager mediaManagerObject = new MediaManager();
		String path = new String("testPath");
		String uploadPath = File.separator;
		
        String returnedString = mediaManagerObject.uploadMediaBase64(path, uploadPath);
        
        assertNotNull(returnedString);
	}
}
