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
 * Name : CtrlModifyProcess.js
 * Module : CtrlModifyProcess
 * Location : /applicationContent/com/sequenziatore/client/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-26     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-03-26     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunta paginazione contenuto
 * ======================================================
 * 0.0.3           2014-04-10     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornati controlli di modifica del processo
 * ======================================================
 * 0.0.4           2014-04-11     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornate dipendenze del modulo
 * ======================================================
 */

var application = angular.module('CtrlModifyProcess',['LocationManager','AdminUserRequestManager','AdminUser','LocalStorageModule','pickadate']);

application.controller('CtrlModifyProcess',['$scope','LocationManager','AdminUserRequestManager','AdminUser','localStorageService',
                                          function($scope,LocationManager,AdminUserRequestManager,AdminUser,localStorageService){
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleModifyProcessLocation();

      $scope.closeAlert = function(){
          $scope.showAlertOK = 'false';
          $scope.showAlertError = 'false';
      };
      $scope.closeAlert();

	if(LocationManager.isLogged() === 'true'){
		
		if(LocationManager.isAdmin() === 'true'){

			AdminUserRequestManager.getAdminInfo($scope);

			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
            	if(result == 'connectionError'){
            		//StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                    $scope.showAlertOK = true;
                    $scope.messageOk = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
            	else{
    				AdminUserRequestManager.getProcess($scope,Number(localStorageService.get('IdProcess')));
    				$scope.user = AdminUser.getName();
    				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
            	}

			});

			$scope.$on('GET_PROCESS_IS_END',function(event,result){
				if(result == 'connectionError'){
            		//StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                    $scope.showAlertOK = true;
                    $scope.messageOk = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
				else{
					$scope.result = result;
					$scope.available = result['Available'];
					if($scope.available === false){
						$scope.isActive = 'Non attivo';
						$scope.buttonIsActiveStatus = 'Attiva processo';
					}
					else{
						$scope.isActive = 'Attivo';
						$scope.buttonIsActiveStatus = 'Disattiva processo';
					}
					$scope.processName = result['Name'];
					$scope.processDescription = result['Description'];
					$scope.dateStartView = result['PublicationDate'];
					$scope.dateCloseView = result['ClosingDate'];
					$scope.dateEndSubscriptionView = result['EndSubscriptionDate'];
					$scope.stepList = result['StepList'];
				}
			});
			
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
	}
	
	$scope.isActiveClicked = function(){
		if($scope.available === false){
			var confirmModify = confirm('Sicuro di voler ATTIVARE il processo?');
			if(confirmModify === true){
				$scope.available = true;
				$scope.isActive = 'Attivo';
				$scope.buttonIsActiveStatus = 'Disattiva processo';
			}
		}
		else{
			var confirmModify = confirm("Sicuro di voler DISATTIVARE il processo?");
			if(confirmModify === true){
				$scope.available = false;
				$scope.isActive = 'Non Attivo';
				$scope.buttonIsActiveStatus = 'Attiva processo';
			}
		}
	};
	
	$scope.showCalendarStart = function(){
		if($scope.dateStartVisible === undefined){
			$scope.dateStartVisible = 'true';
		}
		else{
			$scope.dateStartVisible = undefined;
		}
	};

	
	$scope.showCalendarClose = function(){
		if($scope.dateCloseVisible === undefined){
			$scope.dateCloseVisible = 'true';
		}
		else{
			$scope.dateCloseVisible = undefined;
		}
		
	};
	
	$scope.showCalendarEndSubscription = function(){
		
		if($scope.dateEndSubscriptionVisible === undefined){
			$scope.dateEndSubscriptionVisible = 'true';
		}
		else{
			$scope.dateEndSubscriptionVisible = undefined;
		}
	};
	
	// Metodo che dati anno mese e giorno nel formato yyy-mm-dd
	// restituisce un oggetto di tipo Data con ora 00.00.00
	$scope.yearMonthDayToJSDate = function(year,month,day){
		var date = new Date(new Date().setHours(0, 0, 0, 0));
		return date.setFullYear(Number(year), Number(month), Number(day));
	};
	
	$scope.dateStartSelected = function(date){
		if(!(date === undefined)){
			$scope.changedPublicationDate = true;
			var year = date.substring(0,4); 
			var month = date.substring(5,7); 
			var day = date.substring(8,10);
			$scope.dateStartJSFormat = $scope.yearMonthDayToJSDate(year,month,day);
			$scope.dateStartView = day + '-' + month + '-' + year;
		    $scope.dateStartToSend = {'Day': Number(day), 'Month': Number(month), 'Year': Number(year)};
		    $scope.dateStart = undefined;
		    // Per nascondere il calendario al click sulla data decommentare la riga sotto
			$scope.dateStartVisible = undefined;
		}
	};
	
	$scope.dateCloseSelected = function(date){
		if(!(date === undefined)){
			$scope.changedCloseDate = true;
			$scope.dateEndSubscriptionSelected(date,true);
			var year = date.substring(0,4); 
		    var month = date.substring(5,7); 
		    var day = date.substring(8,10);
		    $scope.dateCloseJSFormat = $scope.yearMonthDayToJSDate(year,month,day);
		    $scope.dateCloseView = day + '-' + month + '-' + year;
		    $scope.dateCloseToSend = {'Day': Number(day), 'Month': Number(month), 'Year': Number(year)};
		    $scope.dateClose = undefined;
		    // Per nascondere il calendario al click sulla data decommentare la riga sotto
		    $scope.dateCloseVisible = undefined;
		}
	};
	
	$scope.dateEndSubscriptionSelected = function(date,clickedClose){
		if(!(date === undefined)){
			$scope.changedEndSubDate = true;
			var year = date.substring(0,4); 
		    var month = date.substring(5,7); 
		    var day = date.substring(8,10);
		    $scope.dateEndSubscriptionJSFormat = $scope.yearMonthDayToJSDate(year,month,day);
		    $scope.dateEndSubscriptionView = day + '-' + month + '-' + year;
		    $scope.dateEndSubscriptionToSend = {'Day': Number(day), 'Month': Number(month), 'Year': Number(year)};
		    $scope.dateEndSubscription = undefined;
		    // Per nascondere il calendario al click sulla data decommentare la riga sotto
		    $scope.dateEndSubscriptionVisible = undefined;
		}
	};

	// Formato data input 'dd-mm-yyyy'
	$scope.stringDateToJSONdate = function(data){

		var date = data;
		var month = undefined;
		var year = undefined;
		var day = undefined;
		
		day = date.substring(0,2);

		// Se il giorno ha 2 cifre tipo 25
		if(day.indexOf('-') == -1){
			month = date.substring(3,5);
			
			// Se il mese ha 2 cifre
			if(month.indexOf('-') == -1){
				year = date.substring(6,10);
			}
			else{
				month = date.substring(3,4);
				year = date.substring(5,9);
			}
		}
		// Se il giorno ha 1 cifra
		else{
			day = date.substring(0,1);
			month = date.substring(2,4);
			// Se il mese ha 2 cifre
			if(month.indexOf('-') == -1){
				year = date.substring(5,9);
			}
			else{
				month = date.substring(2,3);
				year = date.substring(4,8);
			}
		}

		return{
			'Day': Number(day), 
			'Month': Number(month), 
			'Year': Number(year)
		};
	};
	
	$scope.doModifyProcess = function(){
		try{
			if($scope.processName === undefined ||
				$scope.processName === ''){
				throw 'Nome processo non inserito';
			}
			if($scope.processDescription === undefined ||
					$scope.processDescription === ''){
					throw 'Descrizione processo non inserita';
			}

			$scope.result['PublicationDate'] = $scope.stringDateToJSONdate($scope.dateStartView);
			$scope.result['ClosingDate'] = $scope.stringDateToJSONdate($scope.dateCloseView);
			$scope.result['EndSubscriptionDate'] = $scope.stringDateToJSONdate($scope.dateEndSubscriptionView);

			var startDateToCast = new Date();
			var closeDateToCast = new Date();
			var endSubDateToCast = new Date();
		
			if($scope.changedPublicationDate === undefined){
				startDateToCast.setFullYear($scope.result['PublicationDate'].Year, $scope.result['PublicationDate'].Month -1, $scope.result['PublicationDate'].Day);
			}
			if($scope.changedCloseDate === undefined){
				closeDateToCast.setFullYear($scope.result['ClosingDate'].Year, $scope.result['ClosingDate'].Month -1, $scope.result['ClosingDate'].Day);
			}
			if($scope.changedEndSubDate === undefined){
				endSubDateToCast.setFullYear($scope.result['EndSubscriptionDate'].Year, $scope.result['EndSubscriptionDate'].Month -1, $scope.result['EndSubscriptionDate'].Day);
			}
			if($scope.changedPublicationDate === true){
				startDateToCast.setFullYear($scope.dateStartToSend.Year, $scope.dateStartToSend.Month -1, $scope.dateStartToSend.Day);
			}
			if($scope.changedCloseDate === true){
				closeDateToCast.setFullYear($scope.dateCloseToSend.Year, $scope.dateCloseToSend.Month -1, $scope.dateCloseToSend.Day);
			}
			if($scope.changedEndSubDate === true){
				endSubDateToCast.setFullYear($scope.dateEndSubscriptionToSend.Year, $scope.dateEndSubscriptionToSend.Month -1, $scope.dateEndSubscriptionToSend.Day);
			}

			// Creo le date in formato JavaScript e setto le ore a zero in quanto
			// il confronto va fatto solo rispetto alla data
			var actualDate = new Date(new Date().setHours(0, 0, 0, 0));
			var startDate = new Date(new Date(startDateToCast).setHours(0, 0, 0, 0));
			var closeDate = new Date(new Date(closeDateToCast).setHours(0, 0, 0, 0));
			var endSubscriptionDate = new Date(new Date(endSubDateToCast).setHours(0, 0, 0, 0));
			
			if(startDate < actualDate && $scope.changedPublicationDate === true){
				throw 'La data di inizio deve essere la stessa di oggi o una maggiore';
			}
			
			if(closeDate < startDate && $scope.changedCloseDate === true){
				throw 'La data di chiusura deve essere successiva a quella di inizio';
			}
			
			if((!((endSubscriptionDate >= startDate)&&(endSubscriptionDate <= closeDate))) && $scope.changedEndSubDate === true){
				throw 'La data di chiusura iscrizione deve essere compresa fra quella di inizio e quella fine processo';
			}
			
			$scope.result['Name'] = $scope.processName;
			$scope.result['Description'] = $scope.processDescription;
			$scope.result['IdStep'] = Number($scope.result['IdStep']);
			$scope.result['Level'] = Number($scope.result['Level']);
			$scope.result['IdProcess'] = Number($scope.result['IdProcess']);
			$scope.result['Available'] = new Boolean($scope.available === true);
			
			for(var i = 0; i < $scope.result.StepList.length; i++){
				$scope.stepList[i]['IsPhoto'] = new Boolean($scope.stepList[i]['IsPhoto'] === 'true');
				$scope.stepList[i]['IsText'] = new Boolean($scope.stepList[i]['IsText'] === 'true');
				$scope.stepList[i]['IsGeolocation'] = new Boolean($scope.stepList[i]['IsGeolocation'] === 'true');
				
				$scope.result.StepList[i] = $scope.stepList[i];
			}
	
			AdminUserRequestManager.doModifyProcess($scope,$scope.result);
			
			$scope.$on('MODIFY_PROCESS_IS_END',function(event,result){
				$scope.messageOk = result;
                $scope.showAlertOK = 'true';
                $scope.showAlertError = 'false';
                $scope.scrollOnTop();
			});
		}
		catch(error){
			$scope.showAlertOK = 'false';
			$scope.messageError = error;
            $scope.showAlertError = 'true';
            $scope.scrollOnTop();
		}
	};
}]);