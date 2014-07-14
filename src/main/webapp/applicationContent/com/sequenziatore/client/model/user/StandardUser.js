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
 * Name : StandardUser.js
 * Module : StandardUser
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

var application = angular.module('StandardUser',[]);

application.factory('StandardUser',[function(){
		
var StandardUser = function(){

		var standardUser = {};
		
		// Memorizzo il sotto oggetto della superclasse
		standardUser.__proto__ = GenericUser();
		
		var stepList = [];
		
		// Costruttore
		standardUser.constructor = function(){
			standardUser.setId(undefined);
			standardUser.setUsername(undefined);
			standardUser.setIsAdmin(undefined);
			standardUser.setEmail(undefined);
			standardUser.setCity(undefined);
			standardUser.setDistrict(undefined);
			standardUser.setName(undefined);
			standardUser.setSurname(undefined);
		};
		
		standardUser.build = function(data){
			standardUser.setId(data["IdUser"]);
			standardUser.setUsername(data["Username"]);
			standardUser.setIsAdmin(data["IsAdmin"]);
			standardUser.setEmail(data["Email"]);
			standardUser.setCity(data["City"]);
			standardUser.setDistrict(data["District"]);
			standardUser.setName(data["Name"]);
			standardUser.setSurname(data["Surname"]);
		};
		
		// Metodo che usa la gelocalizzazione di HTML5 per ottenere
		// le coordinate e ritorna un JSONObject con i dati rilevati
		standardUser.getGeolocation = function(scope){
			try{
				// Creo la funzione da eseguire in caso corretto ottenimento
				// della geolocazione
				var geoLocationSuccess = function(position){
					//scope.$broadcast('GEOLOCATION_IS_AVAILABLE');
					var geoLocation = {
							'Latitude':position.coords.latitude,
							'Longitude': position.coords.longitude
						};
					scope.$broadcast('GEOLOCATION_IS_END', geoLocation);
				};
				
				// Creo la funzione da eseguire in caso non sia disponibile
				// nel browser la geolocalizzazione
				var geoLocationFail = function(position){
					scope.$broadcast('GEOLOCATION_NOT_AVAILABLE');
					alert("Geolocalizzazione non disponibile. Assicurati di avere una connessione dati attiva e prova ad eseguire un refresh della pagina.");
				};
				
				// Se disponibile la gelocalizzazione provo ad ottenere la geolocazione
				// utilizzando i metodi dell'oggetto navigator reso disponibile dal browser
				if(navigator.geolocation){
					navigator.geolocation.getCurrentPosition(geoLocationSuccess,geoLocationFail);
				}
				else{
					throw 'Geolocalizzazione non disponibile';
				}
			}
			catch(error){
				throw error;
			}
		};

		standardUser.getStepList = function(){
			return stepList;
		};
		
		standardUser.setStepList = function(newStepList){
			var genericStepList = [];
			
			for(var i=0; i<newStepList.length; i++){
				var genericStep = StandardUserStep();
				genericStep.setIdStep(newStepList[i]['IdStep']);
				genericStep.setLevel(newStepList[i]['Level']);
				genericStep.setDescription(newStepList[i]['Description']);
				genericStep.setIsPhoto(newStepList[i]['IsPhoto']);
				genericStep.setIsText(newStepList[i]['IsText']);
				genericStep.setIsGeolocation(newStepList[i]['IsGeolocation']);
				genericStep.setAdminVerify(newStepList[i]['AdminVerify']);
				genericStep.setParallelism(newStepList[i]['Parallelism']);
				genericStep.setState(newStepList[i]['State']);
				genericStep.setWrongText(newStepList[i]['WrongText']);
				genericStep.setWrongGeolocation(newStepList[i]['WrongGeolocation']);
				genericStep.setWrongPhoto(newStepList[i]['WrongPhoto']);
				genericStep.setInsertedText(newStepList[i]['Text']);
				genericStep.setInsertedPhoto(newStepList[i]['Photo']);
				genericStep.setInsertedLatitude(newStepList[i]['Longitude']);
				genericStep.setInsertedLongitude(newStepList[i]['Latitude']);
				genericStepList.push(genericStep);
			}
			stepList = genericStepList;
		};

		return standardUser;
	};
	
	return StandardUser();
}]);