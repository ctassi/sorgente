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
 * Name : AdminUserStep.js
 * Module : PreAuthenticationRequestManager
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

var AdminUserStep = function(){
	
	var adminUserStep = {};
	
	adminUserStep.__proto__ = GenericStep();
	
	var indexStep = undefined;
	var checkLongitude = undefined;
	var checkLatitude = undefined;
	var checkText = undefined;

	adminUserStep.getIndexStep = function(){
		return indexStep;
	};
	
	adminUserStep.setIndexStep = function(newIndexStep){
		indexStep = newIndexStep;
	};

	adminUserStep.getCheckLongitude = function(){
		return checkLongitude;
	};
	
	adminUserStep.setCheckLongitude = function(newCheckLongitude){
		checkLongitude = newCheckLongitude;
	};

	adminUserStep.getCheckLatitude = function(){
		return checkLatitude;
	};
	
	adminUserStep.setCheckLatitude = function(newCheckLatitude){
		checkLatitude = newCheckLatitude;
	};

	adminUserStep.getCheckText = function(){
		return checkText;
	};
	
	adminUserStep.setCheckText = function(newCheckText){
		checkText = newCheckText;
	};
	
	return adminUserStep;
};

