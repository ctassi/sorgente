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
 * Name : CtrlStepValidator.js
 * Module : CtrlStepValidator
 * Location : /applicationContent/com/sequenziatore/client/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-28     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-03     Pavanello Mirko
 * ------------------------------------------------------
 * Aggiornata visualizzazione mappa
 * ======================================================
 */

var application = angular.module('CtrlStepValidator',['LocationManager','AdminUserRequestManager','AdminUser','ngMap']);

application.controller('CtrlStepValidator',['$scope','LocationManager','AdminUserRequestManager','AdminUser',
                                          function($scope,LocationManager,AdminUserRequestManager,AdminUser){
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleStepValidatorLocation();
	
	if(LocationManager.isLogged() === 'true'){

		if(LocationManager.isAdmin() === 'true'){

			AdminUserRequestManager.getAdminInfo($scope);

			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
            	if(result == 'connectionError'){
                    $scope.messageVisible = true;
                    $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
            	else{
					$scope.openWebSocket("/seq/Sequenziatore/pushservice");
					AdminUserRequestManager.getAllStepsToBeValidatedList($scope);
            	}
			});
			
			$scope.$on('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END',function(event,result){
				if(AdminUser.getStepList().length <= 0){
					$scope.stepListToValidate = [];
					$scope.messageResult = 'Non ci sono processi con passi da validare';
				}
				else{
					$scope.stepListToValidate = AdminUser.getStepList();
					
					// Per la paginazione del contenuto
				    $scope.currentPage = 0;
				    $scope.pageSize = 5;
				    $scope.numberOfPages= Math.ceil($scope.stepListToValidate.length/$scope.pageSize);
				    
					$scope.changePage=function(page){
						$scope.currentPage = page;
					};
				}
			});
			
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
	}
	
	$scope.mapList = new Array();
	$scope.createMap = function(){
		if($scope.mapList.length == 0)
			$scope.mapList.push(new Object());
	};
	
	$scope.doStepValidation = function(indexProcess,indexLevel,indexStep,indexDataCollection){
		try{
			var dataCollection =  
				$scope.stepListToValidate[indexProcess]['List'][indexLevel]['List'][indexStep]['List'][indexDataCollection];
			
			
			if(!(dataCollection.CheckLatitude === 'null') && dataCollection.WrongGeolocation === false
					&& dataCollection.geolocationConfirmation === undefined){
				throw 'Non è stato impostato il superamento del task geolocalizzazione';
		
			}

			if(!(dataCollection.CheckText === 'null') && dataCollection.WrongText === false
					&& dataCollection.textConfirmation === undefined){
				throw 'Non è stato impostato il superamento del task testo';
			}
			
			if(!(dataCollection.WrongPhoto === true) && !(dataCollection.Photo === undefined)
					&& dataCollection.photoConfirmation === undefined){
					throw 'Non è stato impostato il superamento del task foto';
			}

			// Preparo il JSONObject con i dati da inviare
			var dataToSend = {
					'Username': dataCollection.Username,
					'WrongText': true,
					'WrongGeolocation': true,
					'WrongPhoto': true,
					'IdCollection': dataCollection.IdCollection,
                    'processName': $scope.stepListToValidate[indexProcess].Name
			};
			
			if(dataCollection.textConfirmation === 'false'){
				dataToSend['WrongText'] = false;
			}
			
			if(dataCollection.geolocationConfirmation === 'false'){
				dataToSend['WrongGeolocation'] = false;
			}
			
			if(dataCollection.photoConfirmation === 'false'){
				dataToSend['WrongPhoto'] = false;
			}
			
			AdminUserRequestManager.doStepValidation($scope,dataToSend);

        }
		catch(error){
			dataCollection.messageValidation = error;
		}
	};
	
	$scope.$on('VALIDATION_STEP_IS_END',function(event,result){
		if(result == 'AlreadyVerified'){
			alert('Il passo è già stato validato in precedenza e non può essere validato nuovamente. Premendo OK la lista verrà riaggiornata.');
			AdminUserRequestManager.getAllStepsToBeValidatedList($scope);
		}
		else{
			alert(result);
			AdminUserRequestManager.getAllStepsToBeValidatedList($scope);
		}
		
	});
	
	$scope.showProcessContent = function(indexProcess){
		var processList = $scope.stepListToValidate;
		if(processList[indexProcess].visible === undefined){
			processList[indexProcess].visible = true;
		}
		else{
			processList[indexProcess].visible = undefined;
		}
	};

	$scope.showLevelContent = function(indexProcess,indexLevel){
		var levelList = $scope.stepListToValidate[indexProcess]['List'];
		if(levelList[indexLevel].visible === undefined){
			levelList[indexLevel].visible = true;
		}
		else{
			levelList[indexLevel].visible = undefined;
		}
	};

	$scope.showStepContent = function(indexProcess,indexLevel,indexStep){
		var stepList = $scope.stepListToValidate[indexProcess]['List'][indexLevel]['List'];
		if(stepList[indexStep].visible === undefined){
			stepList[indexStep].visible = true;
		}
		else{
			stepList[indexStep].visible = undefined;
		}
	};

	$scope.showMap = function(indexProcess,indexLevel,indexStep,indexDataCollection){
		var dataCollection = 
			$scope.stepListToValidate[indexProcess]['List'][indexLevel]['List'][indexStep]['List'][indexDataCollection];
		$scope.createMap();
		dataCollection.mapVisible = true;
	};
	
	$scope.calculateTotalDataCollection = function(indexProcess){
		
		var stepList = $scope.stepListToValidate;
		var process = stepList[indexProcess]['List'];
		
		for(var j=0; j < process.length; j++){
			var level = process[j]['List'];
			for(var k=0; k < level.length; k++){
				return stepList[indexProcess]['List'][j]['List'][k]['List'].length;
			}
		}

	};
	
}]);