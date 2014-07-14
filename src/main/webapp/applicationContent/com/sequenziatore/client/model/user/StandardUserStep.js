/*
 * Copyright [2014] [DeSQ]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Name : StandardUserStep.js
 * Module : PreAuthenticationRequestManager
 * Location : /applicationContent/com/sequenziatore/client/model/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-19     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 */

var StandardUserStep = function(){
	
	var standardUserStep = {};
	
	standardUserStep.__proto__ = GenericStep();
	
	var insertedText = undefined;
	var insertedLatitude = undefined;
	var insertedLongitude = undefined;
	var insertedPhoto = undefined;

	standardUserStep.getInsertedText = function() {
		return insertedText;
	};
	
	standardUserStep.setInsertedText = function(newInsertedText){
		insertedText = newInsertedText;
	};
	
	standardUserStep.getInsertedLatitude = function(){
		return insertedLatitude;
	};
	
	standardUserStep.setInsertedLatitude = function(newInsertedLatitude){
		insertedLatitude = newInsertedLatitude;
	};
	
	standardUserStep.getInsertedLongitude = function(){
		return insertedLongitude;
	};
	
	standardUserStep.setInsertedLongitude = function(newInsertedLongitude){
		insertedLongitude = newInsertedLongitude;
	};
	
	standardUserStep.getInsertedPhoto = function(){
		return insertedPhoto;
	};
	
	standardUserStep.setInsertedPhoto = function(newInsertedPhoto){
		insertedPhoto = newInsertedPhoto;
	};
	
	standardUserStep.makeJSONObject = function(){
		return{
			'IdStep': standardUserStep.getIdStep(),
			'Text': insertedText,
			'Latitude': String(insertedLatitude),
			'Longitude': String(insertedLongitude),
			'Photo': insertedPhoto
		};
	};
	
	return standardUserStep;
};