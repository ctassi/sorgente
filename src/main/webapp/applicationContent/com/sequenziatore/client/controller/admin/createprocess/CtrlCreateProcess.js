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
 * Name : CtrlCreateProcess.js
 * Module : CtrlCreateProcess
 * Location : /applicationContent/com/sequenziatore/client/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-27     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-04     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunta paginazione contenuto
 * ======================================================
 * 0.0.3           2014-04-06     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiornamento cancellazione passo
 * ======================================================
 * 0.0.4           2014-04-11     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornata gestione geolocalizzazione
 * ======================================================
 */

var application = angular.module('CtrlCreateProcess',['LocationManager','AdminUserRequestManager','pickadate','ngMap']);

application.controller('CtrlCreateProcess',['$scope','LocationManager','AdminUserRequestManager',
                                          function($scope,LocationManager,AdminUserRequestManager){
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleCreateProcessLocation();
	
	$scope.processDivVisible = 'true';
	$scope.levelDivVisible = 'false';	
	$scope.stepDivVisible = 'false';
	$scope.summaryProcessDivVisible = 'false';

    $scope.closeAlert = function(){
        $scope.showAlert = 'false';
        $scope.showAlertDetailsError = 'false';
        $scope.showAlertSummarySuccess = 'false';
        $scope.showAlertSummaryError = 'false';

        angular.forEach($scope.levelList, function(entry) {
            entry.showStepAlert = 'false';
            entry.showStepAlertDanger = 'false';
        });
    };

    $scope.closeAlert();

	if(LocationManager.isLogged() === 'true'){
		
		if(LocationManager.isAdmin() === 'true'){
			
			AdminUserRequestManager.getAdminInfo($scope);

			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
			});
		}
	}
	
	$scope.$on('CONNECTION_PROBLEM',function(event,result){
		$scope.closeWebSocket();
		alert(result);
	});
	
	// Array che rappresenta la lista dei nuovi passi che
	// l'admin sta creando, di un livello, di un nuovo processo
	$scope.newStepList = [];
	
	// Array che rappresenta la lista dei nuovi livelli che
	// l'admin sta creando di un processo
	$scope.levelList = [];
	
	// Metodo che aggiunge un nuovo step all'array newStepList
	$scope.addStep = function(indexLevel){
		try{
			if($scope.levelList[indexLevel].stepDescription === undefined || 
				$scope.levelList[indexLevel].stepDescription === ''){
				throw 'Descrizione del passo non valida';
			}
			if($scope.levelList[indexLevel].isAdmin === undefined){
				throw 'Verifica amministratore non impostata';
			}
			if($scope.levelList[indexLevel].isPhoto === undefined){
				throw 'Verifica foto non impostata';
			
			}
			if($scope.levelList[indexLevel].isText === undefined){
				throw 'Verifica testo non impostata';
			
			}
			if($scope.levelList[indexLevel].isText === 'true'){
				if($scope.levelList[indexLevel].textCheck === undefined ||
						$scope.levelList[indexLevel].textCheck === ''	){
						throw 'Testo di verifica non impostato';
				}
			}
			if($scope.levelList[indexLevel].isGeolocation === undefined){
				throw 'Verifica geolocalizzazione non impostata';
			
			}
			if($scope.levelList[indexLevel].isGeolocation === 'true'){
				if($scope.levelList[indexLevel].latitudeCheck === undefined ||
						$scope.levelList[indexLevel].latitudeCheck === ''	){
						throw 'Latitudine di verifica non impostata';
				}
				
				if($scope.levelList[indexLevel].longitudeCheck === undefined ||
						$scope.levelList[indexLevel].longitudeCheck === ''	){
						throw 'Longitudine di verifica non impostata';
				}
			}
			// Controllo se il passo che sto creando richieda almeno una
			// task tra geolocalizzazione, testo e foto
			if($scope.levelList[indexLevel].isText === 'false' && $scope.levelList[indexLevel].isGeolocation == 'false' &&
					$scope.levelList[indexLevel].isPhoto === 'false'){
					throw 'Non puoi inserire un passo senza alcun task richiesto';
				
				
			}

			// Creo un AdminUserStep
			var newAdminUserStep = AdminUserStep();
			
			// Popolo l'oggetto con i valori letti dalla view
			newAdminUserStep.setIndexStep($scope.levelList[indexLevel].length);	// Set Index
			newAdminUserStep.setLevel(indexLevel);			// Set Level
			newAdminUserStep.setDescription($scope.levelList[indexLevel].stepDescription);	// Set Description
			newAdminUserStep.setAdminVerify($scope.levelList[indexLevel].isAdmin);			// Set IsAdmin
			newAdminUserStep.setIsPhoto($scope.levelList[indexLevel].isPhoto);				// Set IsPhoto
			newAdminUserStep.setIsText($scope.levelList[indexLevel].isText);					// Set IsText
			newAdminUserStep.setIsGeolocation($scope.levelList[indexLevel].isGeolocation);	// Set Geo

			// Se è richiesto inserimento del testo allora salvo il testo campione di confronto
			// Altrimenti null
			if(newAdminUserStep.getIsText() === "true"){
				newAdminUserStep.setCheckText($scope.levelList[indexLevel].textCheck);
			}
			else{
				newAdminUserStep.setCheckText('null');
			}

			// Se è richiesta la geolocalizzazione salvo latitudine e longitudine campione
			// di confronto. Altrimenti entrambi i campi a null
			if(newAdminUserStep.getIsGeolocation() === "true"){
				newAdminUserStep.setCheckLatitude($scope.levelList[indexLevel].latitudeCheck);
				newAdminUserStep.setCheckLongitude($scope.levelList[indexLevel].longitudeCheck);
			}
			else{
				newAdminUserStep.setCheckLatitude('null');
				newAdminUserStep.setCheckLongitude('null');
			}

			// Aggiungo il passo alla lista passi
			$scope.levelList[indexLevel].push(newAdminUserStep);
			$scope.levelList[indexLevel].messageStepOk = 'Passo: "' + newAdminUserStep.getDescription() + '" inserito correttamente';
			$scope.levelList[indexLevel].parallelism = 'NOT';
			$scope.levelList[indexLevel].messageStepError = '';
			$scope.levelList[indexLevel].symbol = '+';
			$scope.levelList[indexLevel].stepDivVisible = 'false';
			$scope.messageSummaryProcessError = '';
			$scope.resetFields(indexLevel);

            $scope.levelList[indexLevel].showStepAlert = 'true';

		}// Fine try
		catch(error){
			$scope.levelList[indexLevel].messageStepOk = '';
			$scope.levelList[indexLevel].messageStepError = error;
            $scope.levelList[indexLevel].showStepAlertDanger = 'true';
		}
	};
	
	// Metodo che cancella un passo dall'array newStepList e aggiorna
	// gli indici degli step
	$scope.deleteStep = function(indexStep, indexLevel){
        //$scope.levelList[indexLevel].showStepAlertDanger = 'false';

		var stepToDelete = $scope.levelList[indexLevel][indexStep];
		
		var confirmDelete = confirm("Sei sicuro di voler eliminare il passo " + stepToDelete.getDescription() + " ?");
		// Cambio indexStep a tutti i passi successivi a quello
		// da eliminare ed elimino il passo
		if(confirmDelete === true){
			for(var i=indexStep+1; i < $scope.levelList[indexLevel].length; i++){
				var newIndex = $scope.levelList[indexLevel][i].getIndexStep() - 1;
				$scope.levelList[indexLevel][i].setIndexStep(newIndex);
			}
			$scope.levelList[indexLevel].splice(indexStep, 1);
		}
		$scope.resetFields(indexLevel);
		$scope.levelList[indexLevel].buttonAddStepVisible = 'true';
		$scope.levelList[indexLevel].buttonApplyModifyStepVisible = 'false';
		$scope.levelList[indexLevel].buttonIgnoreModifyStepVisible = 'false';
		$scope.levelList[indexLevel].creatingOrModifingStep = 'Creazione passo:';
		$scope.levelList[indexLevel].messageStepOk = '';
		$scope.levelList[indexLevel].messageStepError = '';
        $scope.closeAlert();
	};
	
	// Metodo che raccoglie e avvia la richiesta della creazione di un nuovo
	// processo
	$scope.doCreateNewProcess = function(){
        $scope.closeAlert();
		$scope.messageSummaryProcessError = '';
		$scope.messageSummaryProcessOk = '';
		try{
			if($scope.levelList.length === 0){
				throw 'Un processo deve avere almeno un livello';
			}

			var totalStepsList = [];
			
			for(var i=0; i < $scope.levelList.length; i++){
				
				if($scope.levelList[i].length === 0){
					throw 'Il livello ' + Number(i+1) + ' non ha alcun passo. Ogni livello deve avere almeno un passo';
				}
				
				if($scope.levelList[i].length > 1 && $scope.levelList[i].parallelism === 'NOT'){
					throw 'Il livello ' + Number(i+1) + ' non ha il tipo di parallelismo impostato';
				}
				
				for(var j=0; j < $scope.levelList[i].length; j++){
					var step = {
							'Level':$scope.levelList[i][j].getLevel(),
							'Description': $scope.levelList[i][j].getDescription(),
							'IsPhoto': new Boolean($scope.levelList[i][j].getIsPhoto() === 'true'),
							'IsText': new Boolean($scope.levelList[i][j].getIsText() === 'true'),
							'IsGeolocation':new Boolean($scope.levelList[i][j].getIsGeolocation() === 'true'),
							'CheckText': $scope.levelList[i][j].getCheckText(),
							'CheckLatitude': $scope.levelList[i][j].getCheckLatitude(),
							'CheckLongitude': $scope.levelList[i][j].getCheckLongitude(),
							'AdminVerify': new Boolean($scope.levelList[i][j].getAdminVerify() === 'true'),
							'Parallelism': $scope.levelList[i].parallelism
					};

					totalStepsList.push(step);
				}
			}

			var processData = {
					'Name': $scope.processName,
					'Description': $scope.processDescription,
					'TotalLevel': $scope.levelList.length,
					'PublicationDate': $scope.dateStartToSend,
					'ClosingDate': $scope.dateCloseToSend,
					'Available': true,
					'EndSubscriptionDate': $scope.dateEndSubscriptionToSend,
					'Steps': totalStepsList
			};

			AdminUserRequestManager.doCreateNewProcess($scope,processData);
			
		}
		catch(error){
			$scope.messageSummaryProcessError = error;
            $scope.showAlertSummaryError = 'true';
            $scope.scrollOnTop();
		}
	};
	
	$scope.$on('CREATE_PROCESS_IS_END',function(event,result){
		$scope.messageDetailsProcessOk = result;
		$scope.levelList = [];
		$scope.processDivVisible = 'true';
		$scope.summaryProcessDivVisible = undefined;
		$scope.processName = undefined;
		$scope.processDescription = undefined;
		$scope.dateStart = undefined;
		$scope.dateClose = undefined;
		$scope.dateEndSubscription = undefined;
		$scope.dateStartView = undefined;
		$scope.dateCloseView = undefined;
		$scope.dateEndSubscriptionView = undefined;
        $scope.showAlert = 'true';
        $scope.scrollOnTop();
		
	});
	
	// Metodo che istanzia un oggetto di tipo GenericProcess che sarà poi popolato
	// con i valori letti nella view
	$scope.createNewProcess = function(){
		try{
			$scope.messageDetailsProcessOk = '';
            $scope.closeAlert();

			if($scope.processName === undefined ||
				$scope.processName === ''){
				throw 'Nome del processo non inserito';
			}
			if($scope.processDescription === undefined ||
					$scope.processDescription === ''){
					throw 'Descrizione del processo non inserita';
			}

			if($scope.dateStartToSend === undefined ||
					$scope.dateStartToSend === ''){
					throw 'Data di inizio non selezionata';
			}
			
			if($scope.dateCloseToSend === undefined ||
					$scope.dateCloseToSend === ''){
					throw 'Data di chiusura non selezionata';
			}
			
			if($scope.dateEndSubscriptionToSend === undefined ||
					$scope.dateEndSubscriToSend === ''){
					throw 'Data di fine iscrizione non selezionata';
			}
			
			var actualDate = new Date(new Date().setHours(0, 0, 0, 0));
			var startDate = new Date();
			var startDateToCast = new Date(new Date(startDate).setHours(0, 0, 0, 0));
			startDateToCast.setFullYear($scope.dateStartToSend.Year,$scope.dateStartToSend.Month-1,$scope.dateStartToSend.Day);
			
			if(startDateToCast < actualDate){
				throw 'La data di inizio deve essere la stessa di oggi o una maggiore';
			}
			
			if($scope.dateCloseJSFormat < $scope.dateStartJSFormat){
				throw 'La data di chiusura deve essere una data successiva a quella di inizio';
			}
			
			if(!(($scope.dateEndSubscriptionJSFormat >= $scope.dateStartJSFormat)&&($scope.dateEndSubscriptionJSFormat <= $scope.dateCloseJSFormat))){
				throw 'La data di chiusura iscrizione deve essere compresa fra quella di inizio e quella fine processo';
			}
			$scope.processDivVisible = 'false';
			$scope.summaryProcessDivVisible = 'true';
		}
		catch(error){
			$scope.messageDetailsProcessError = error;
            $scope.showAlertDetailsError = 'true';
            $scope.scrollOnTop();
		}
	};

	
	$scope.insertNewLevel = function(){
		$scope.levelDivVisible = 'true';

		var emptyLevel = new Array();
		emptyLevel.stepDivVisible = 'false';
		emptyLevel.symbol = '+';

		$scope.levelList.push(emptyLevel);
		$scope.messageSummaryProcessError = '';
        $scope.closeAlert();
	};

	$scope.insertNewStep = function(indexLevel){
        //$scope.levelList[indexLevel].showStepAlert = 'false';
		if($scope.levelList[indexLevel].stepDivVisible === 'false'){
			$scope.levelList[indexLevel].stepDivVisible = 'true';
			if($scope.levelList[indexLevel].buttonAddStepVisible === 'false'){
				$scope.levelList[indexLevel].buttonApplyModifyStepVisible = 'true';
				$scope.levelList[indexLevel].buttonIgnoreModifyStepVisible = 'true';
				$scope.levelList[indexLevel].creatingOrModifingStep = 'Modifica passo:';
			}
			else{
				$scope.levelList[indexLevel].buttonAddStepVisible = 'true';
				$scope.levelList[indexLevel].buttonApplyModifyStepVisible = 'false';
				$scope.levelList[indexLevel].buttonIgnoreModifyStepVisible = 'false';
				$scope.levelList[indexLevel].creatingOrModifingStep = 'Creazione passo:';
			}
			$scope.levelList[indexLevel].symbol = '-';
		}
		else{
			$scope.levelList[indexLevel].stepDivVisible = 'false';
			$scope.levelList[indexLevel].symbol = '+';

		}
		$scope.levelList[indexLevel].messageStepOk = '';
        $scope.closeAlert();

	};
	
	$scope.deleteLevel = function(indexLevel){
		var confirmDelete = confirm("Sei sicuro di voler eliminare il livello? Verranno eliminati anche tutti i suoi passi");

		if(confirmDelete === true){
			$scope.levelList.splice(indexLevel, 1);
		}
		$scope.messageSummaryProcessError = '';
        $scope.closeAlert();
	};
	
	$scope.modifyStep = function(indexStep,indexLevel){
        $scope.closeAlert();
        //$scope.levelList[indexLevel].showStepAlert = 'false';

		$scope.levelList[indexLevel].stepDivVisible = 'true';
		$scope.levelList[indexLevel].buttonAddStepVisible = 'false';
		$scope.levelList[indexLevel].buttonApplyModifyStepVisible = 'true';
		$scope.levelList[indexLevel].buttonIgnoreModifyStepVisible = 'true';
		$scope.levelList[indexLevel].creatingOrModifingStep = 'Modifica passo:';
		$scope.levelList[indexLevel].symbol = '-';

		// Ripopolo la console di inserimento passo con i dati del passo da modificare
		$scope.levelList[indexLevel].stepDescription = $scope.levelList[indexLevel][indexStep].getDescription();
		$scope.levelList[indexLevel].isAdmin = $scope.levelList[indexLevel][indexStep].getAdminVerify();
		$scope.levelList[indexLevel].isPhoto = $scope.levelList[indexLevel][indexStep].getIsPhoto();
		$scope.levelList[indexLevel].isText = $scope.levelList[indexLevel][indexStep].getIsText();
		$scope.levelList[indexLevel].isGeolocation = $scope.levelList[indexLevel][indexStep].getIsGeolocation();
		
		// Se il passo richiede inserimento di testo popolo anche la casella di testo per il checkText
		if($scope.levelList[indexLevel].isText === 'true'){
			$scope.levelList[indexLevel].textCheck = $scope.levelList[indexLevel][indexStep].getCheckText();
		}
		
		// Se il passo richiede geolocalizzazione popolo le caselle di testo della latitudine e longitudine
		if($scope.levelList[indexLevel].isGeolocation === 'true'){
			$scope.levelList[indexLevel].longitudeCheck = $scope.levelList[indexLevel][indexStep].getCheckLongitude();
			$scope.levelList[indexLevel].latitudeCheck = $scope.levelList[indexLevel][indexStep].getCheckLatitude();
			$scope.latMarker = $scope.levelList[indexLevel][indexStep].getCheckLongitude();
			$scope.longMarker = $scope.levelList[indexLevel][indexStep].getCheckLatitude();
		}

		// Metodo interno per applicare le modifiche ad un passo
		$scope.applyModifyStep = function(indexLevel){
			try{
                $scope.closeAlert();
				if($scope.levelList[indexLevel].isText === 'false' && $scope.levelList[indexLevel].isGeolocation == 'false' &&
						$scope.levelList[indexLevel].isPhoto === 'false'){
						throw 'Un passo deve richiedere almeno una task.';
				}
				
				var confirmModify = confirm('Sei sicuro di voler applicare le modifiche?');
				if(confirmModify === true){

					// Ripopolo la console di inserimento passo con i dati del passo da modificare
					$scope.levelList[indexLevel][indexStep].setDescription($scope.levelList[indexLevel].stepDescription); 
					$scope.levelList[indexLevel][indexStep].setAdminVerify($scope.levelList[indexLevel].isAdmin);
					$scope.levelList[indexLevel][indexStep].setIsPhoto($scope.levelList[indexLevel].isPhoto);
					$scope.levelList[indexLevel][indexStep].setIsText($scope.levelList[indexLevel].isText);
					$scope.levelList[indexLevel][indexStep].setIsGeolocation($scope.levelList[indexLevel].isGeolocation);
					
					// Se il passo richiede inserimento di testo popolo anche la casella di testo per il checkText
					if($scope.levelList[indexLevel].isText === 'true'){
						$scope.levelList[indexLevel][indexStep].setCheckText($scope.levelList[indexLevel].textCheck);
					}
					
					// Se il passo richiede geolocalizzazione popolo le caselle di testo della latitudine e longitudine
					if($scope.levelList[indexLevel].isGeolocation === 'true'){
						$scope.levelList[indexLevel][indexStep].setCheckLongitude($scope.levelList[indexLevel].longitudeCheck);
						$scope.levelList[indexLevel][indexStep].setCheckLatitude($scope.levelList[indexLevel].latitudeCheck);
					}
					$scope.levelList[indexLevel].creatingOrModifingStep = 'Aggiungi passo:';
					$scope.levelList[indexLevel].buttonAddStepVisible = 'true';
					$scope.levelList[indexLevel].buttonApplyModifyStepVisible = 'false';
					$scope.levelList[indexLevel].buttonIgnoreModifyStepVisible = 'false';
					
					$scope.levelList[indexLevel].messageStepOk = 'Passo modificato correttamente';
					$scope.levelList[indexLevel].messageStepError = '';
					// Per nascondere alla pressione del tasto
					$scope.levelList[indexLevel].stepDivVisible = 'false';
					$scope.levelList[indexLevel].symbol = '+';

                    $scope.levelList[indexLevel].showStepAlert = 'true';
					$scope.resetFields(indexLevel);
				}
			}
			catch(error){
				$scope.levelList[indexLevel].messageStepOk = '';
				$scope.levelList[indexLevel].messageStepError = error;
                $scope.levelList[indexLevel].showStepAlertDanger = 'false';
			}
		};
		
		$scope.ignoreModifyStep = function(indexLevel){
			var ignoreModify = confirm('Le modifiche applicate andranno perse. Vuoi continuare?');
			if(ignoreModify === true){
				$scope.levelList[indexLevel].buttonAddStepVisible = 'true';
				$scope.levelList[indexLevel].buttonApplyModifyStepVisible = 'false';
				$scope.levelList[indexLevel].buttonIgnoreModifyStepVisible = 'false';
				$scope.levelList[indexLevel].creatingOrModifingStep = 'Creazione passo:';

				// Per nascondere alla pressione del tasto
				$scope.levelList[indexLevel].stepDivVisible = 'false';
				$scope.levelList[indexLevel].symbol = '+';
				$scope.resetFields(indexLevel);
				$scope.messageStepOk = '';
				$scope.messageStepError = '';
				
				$scope.levelList[indexLevel].latMarker = $scope.levelList[indexLevel].latitudeCheck;
				$scope.levelList[indexLevel].longMarker = $scope.levelList[indexLevel].longitudeCheck;
			}

		};
		$scope.levelList[indexLevel].messageStepOk = '';
		$scope.levelList[indexLevel].messageStepError = '';
	};
	

	$scope.resetFields = function(indexLevel){
		// Se il passo richiede inserimento di testo popolo anche la casella di testo per il checkText
		if($scope.levelList[indexLevel].isText === 'true'){
			$scope.levelList[indexLevel].textCheck = undefined;
		}
		
		// Se il passo richiede geolocalizzazione popolo le caselle di testo della latitudine e longitudine
		if($scope.levelList[indexLevel].isGeolocation === 'true'){
			$scope.levelList[indexLevel].latitudeCheck = undefined;
			$scope.levelList[indexLevel].longitudeCheck = undefined;
		}
		
		$scope.levelList[indexLevel].stepDescription = undefined; 
		$scope.levelList[indexLevel].isAdmin = undefined;
		$scope.levelList[indexLevel].isPhoto = undefined;
		$scope.levelList[indexLevel].isText = undefined;
		$scope.levelList[indexLevel].isGeolocation = undefined;
	};

	$scope.showCalendarStart = function(){
		if($scope.dateStartVisible === undefined){
			$scope.dateStartVisible = 'true';
		}
		else{
			$scope.dateStartVisible = undefined;
		}
	};

	
	$scope.showCalendarClose = function(date){
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
			var year = date.substring(0,4); 
			var month = date.substring(5,7); 
			var day = date.substring(8,10);
		    $scope.dateStartJSFormat = $scope.yearMonthDayToJSDate(year,month,day);
			$scope.dateStartView = day + '/' + month + '/' + year;
		    $scope.dateStartToSend = {'Day': Number(day), 'Month': Number(month), 'Year': Number(year)};
		    $scope.dateStart = undefined;
		    $scope.dateStartVisible = undefined;
		}
	};

	$scope.dateCloseSelected = function(date){
		if(!(date === undefined)){
			var year = date.substring(0,4); 
		    var month = date.substring(5,7); 
		    var day = date.substring(8,10);
		    $scope.dateCloseJSFormat = $scope.yearMonthDayToJSDate(year,month,day);
			$scope.dateEndSubscriptionSelected(date,true);
		    $scope.dateCloseView = day + '/' + month + '/' + year;
		    $scope.dateCloseToSend = {'Day': Number(day), 'Month': Number(month), 'Year': Number(year)};
		    $scope.dateClose = undefined;
		    $scope.dateCloseVisible = undefined;
		}
	};
	
	$scope.dateEndSubscriptionSelected = function(date,clickedClose){
		if(!(date === undefined)){
			var year = date.substring(0,4); 
		    var month = date.substring(5,7);
		    var day = date.substring(8,10);
		    $scope.dateEndSubscriptionJSFormat = $scope.yearMonthDayToJSDate(year,month,day);
		    $scope.dateEndSubscriptionView = day + '/' + month + '/' + year;
		    $scope.dateEndSubscriptionToSend = {'Day': Number(day), 'Month': Number(month), 'Year': Number(year)};
		    $scope.dateEndSubscription = undefined;
		    $scope.dateEndSubscriptionVisible = undefined;
		}
	
	};
	
	$scope.myPosition = function(event,indexLevel){

		var position = String(this.getPosition());
		var latitude = '';
		var longitude = '';
		var latitudeEnd = position.indexOf(',', 0);
		for(var i = 1; i < latitudeEnd; i++){
			latitude = latitude + position[i];
		}
		for(var i = latitudeEnd+2; i < position.length-1; i++){
			longitude = longitude + position[i];
		}
		
		$scope.$apply(function(){
			$scope.levelList[indexLevel].latitudeCheck = latitude;
			$scope.levelList[indexLevel].longitudeCheck = longitude;
			});
		$scope.latMarker = latitude;
		$scope.longMarker = longitude;
	};

	$scope.createMap = function(indexLevel){
		$scope.levelList[indexLevel].mapList = new Array();
		$scope.levelList[indexLevel].latMarker = 'Rome';
		$scope.levelList[indexLevel].longMarker = 'Italy';
		if($scope.levelList[indexLevel].mapList.length == 0)
			$scope.levelList[indexLevel].mapList.push(new Object());
	};
	
}]);