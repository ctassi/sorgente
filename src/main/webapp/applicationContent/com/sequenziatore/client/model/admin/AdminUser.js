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
 * Name : AdminUser.js
 * Module : AdminUser
 * Location : /applicationContent/com/sequenziatore/client/model/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 */

var application = angular.module('AdminUser',[]);

application.factory('AdminUser',[function(){

	var AdminUser = function(){

		var adminUser = {};
		
		//Memorizzo il sotto oggetto della superclasse
		adminUser.__proto__ = GenericUser();
		
		var userList = undefined;
		var stepList = undefined;
		
		//Costruttore
		adminUser.constructor = function(){
			adminUser.setId(undefined);
			adminUser.setUsername(undefined);
			adminUser.setIsAdmin(undefined);
			adminUser.setEmail(undefined);
			adminUser.setCity(undefined);
			adminUser.setDistrict(undefined);
			adminUser.setName(undefined);
			adminUser.setSurname(undefined);

		};
		
		adminUser.build = function(data){
			adminUser.setId(data["Id"]);
			adminUser.setUsername(data["Username"]);
			adminUser.setIsAdmin(data["IsAdmin"]);
			adminUser.setEmail(data["Email"]);
			adminUser.setCity(data["City"]);
			adminUser.setDistrict(data["District"]);
			adminUser.setName(data["Name"]);
			adminUser.setSurname(data["Surname"]);
		};

		//Metodo ritorna un array contenente tutti gli step
		//di ogni processo, localmente, senza eseguire query
		adminUser.getAllStepsFromAllProcess = function(){

			//Array dove verranno salvati tutti gli steps
			var allSteps = [];
				
			var processList = adminUser.getProcessList();
			
			//Per ogni processo
			for(var i=0; i < processList.length; i++){
				
				//Ricavo la lista passi
				var stepsList = processList[i]['stepsList'];
				for(var j=0; j < stepsList.length; j++){
					//Inserisco in allSteps il singlo passo ricavato
					allSteps.push(stepsList[j]);
				}
			}
			return allSteps;
		};
		
		adminUser.getUserList = function(){
			return userList;
		};
		
		adminUser.setUserList = function(newUserList){
			if(!(newUserList["null"] === 'null')){
				userList = newUserList;
			}
		};
		
		adminUser.getStepList = function(){
			return stepList;
		};
		
		adminUser.setStepList = function(newStepList){
			if(newStepList === undefined){
				stepList = [];
			}
			else if(!(newStepList["null"] === 'null')){
				stepList = newStepList;
			}
		};

		return adminUser;
	};
	
	return AdminUser();
}]);
