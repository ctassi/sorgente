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
 * Name : CtrlCurrentStep.js
 * Module : CtrlCurrentStep
 * Location : /applicationContent/com/sequenziatore/client/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-10     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-17     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunta gestione local storage
 * ======================================================
 * 0.0.3           2014-04-18     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornato invio del passo
 * ======================================================
 */

var application = angular.module('CtrlCurrentStep',['LocationManager','StandardUserRequestManager','StandardUser','LocalStorageModule','angularFileUpload','ngMap']);

application.controller('CtrlCurrentStep',['$scope','LocationManager','StandardUserRequestManager','StandardUser','localStorageService','$upload',
                                          function($scope,LocationManager,StandardUserRequestManager,StandardUser,localStorageService,$upload){
	
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleCurrentStepLocation();

    $scope.closeAlert = function(){
        $scope.messageSuccess = 'false';
        $scope.messageVisible = 'false';
    };
    
    $scope.closeAlert();

	if(LocationManager.isLogged() === 'true'){
		
		if(LocationManager.isAdmin() === 'false'){
			
			StandardUserRequestManager.getUserInfo($scope);
			
			$scope.mapList = new Array();
			
			$scope.createMap = function(){
                $scope.mapList.push(new Object());
			};
			
			$scope.$on('REQUEST_INFO_IS_END',function(event){
				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
				StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
			});
			

			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				if(window.localStorage !== undefined){
					alert("Ci sono problemi di connessione. I tuoi dati saranno salvati in locale e resi disponibili non appena sarai connesso");

					// Aggiungo la chiave per l'id del passo per l'utente loggato
					var idLocalData = 'ID:'+String(LocationManager.getIdUserCookie()) + '_STEP:'+String(result.IdStep);
					if(!($scope.base64Photo == undefined)){
						var base64String = $scope.base64Photo;
						var substitute = "";
						var base64StringToSend = "";
						var limit = base64String.indexOf(',', 0)+1;
						
						for(var i = 0; i < limit; i++ ){
							substitute = substitute + base64String[i];
						}

						base64StringToSend = base64String.replace(substitute,"");
						result['PhotoStored'] = $scope.base64Photo;
						result['Photo'] = base64StringToSend;
					}
					localStorageService.set(idLocalData,result);
				}
				else{
				    alert('Ci sono problemi di connessione e sembra che il tuo browser non supporti il salvataggio dei dati offline.'+
				    		' Riprova quando sarà disponibile una connessione.');
					for(var i=0; i < $scope.stepList.length; i++){
						if($scope.stepList[i].getIdStep() == result.IdStep){
							$scope.stepList[i].messageStepIsSaved = 'I dati sono salvati in locale';
						}
				    }
				}
			});
			
			$scope.$on('REQUEST_GET_NEXT_STEP_IS_END',function(event, result){

				if(!(result['ProcessName'] == undefined)){
					$scope.processName = result['ProcessName'];
				}
				
				if(StandardUser.getStepList().length <= 0){
					$scope.messageData = 'Processo completato con SUCCESSO';
                    $scope.messageVisible = true;
                    $scope.scrollOnTop();
				}
				else{
					$scope.stepList = StandardUser.getStepList();
					$scope.buttonMyPosition = "La mia posizione";

					// Se il parallelismo dei passi del processo è AND allora notifico all'utente
					// che deve eseguire tutti i passi
					if($scope.stepList[0].getParallelism() === 'AND'){
						$scope.parallelism = "Per procedere al livello successivo dovrai completare" +
												" TUTTI i passi presenti in un ordine a tua scelta";
					}
					// Se il parallelismo dei passi del processo è OR allora notifico all'utente
					// che deve eseguire uno solo fra i passi presente
					else if($scope.stepList[0].getParallelism() === 'OR'){
						$scope.parallelism = "Per procedere al livello successivo dovrai completare" +
												" UNO dei passi a tua scelta";
					}
					// Se il parallelismo dei passi del processo è NOT allora notifico all'utente
					// che deve eseguire l'unico passo presente
					else if($scope.stepList[0].getParallelism() === 'NOT'){
						$scope.parallelism = "Per procedere al livello successivo dovrai completare" +
												" il passo richiesto";
					}

					for(i in localStorageService.keys()){
						var idLocalData = localStorageService.keys()[i];
						var idStepKey = idLocalData.replace('ID:'+String(LocationManager.getIdUserCookie()) + '_STEP:','');
						for(var i=0; i < $scope.stepList.length; i++){
							if($scope.stepList[i].getIdStep() == idStepKey){
								$scope.stepList[i].contentLocalStorageData = localStorageService.get(idLocalData);
								$scope.stepList[i].localIsDefined = true;
								$scope.stepList[i].messageNotification = '';
								$scope.stepList[i].insertedText = $scope.stepList[i].contentLocalStorageData.Text;
								$scope.stepList[i].image = $scope.stepList[i].contentLocalStorageData.PhotoStored;

								if(($scope.stepList[i].getState() === 'TOVERIFY' &&
										$scope.stepList[i].getWrongGeolocation() === false) ||
										!($scope.stepList[i].contentLocalStorageData.Latitude === undefined)
										){
									$scope.stepList[i].mapList = new Array();
									$scope.stepList[i].mapVisible = true;
									$scope.stepList[i].mapList.push(new Object());
									$scope.stepList[i].messageInfoLatitude = 'La tua latitudine rilevata: ' + $scope.stepList[i].contentLocalStorageData.Latitude;
									$scope.stepList[i].messageInfoLongitude = 'La tua longitudine rilevata: ' + $scope.stepList[i].contentLocalStorageData.Longitude;
								}
							}	
						}
					}
				}
			});
			
			// Metodo che attiva la procedura per recuperare la geolocazione
			// e poi avviare l'invio dei dati al backend
			$scope.getGeolocationData = function(idStep){

				try{
					StandardUser.getGeolocation($scope);
					
					for(var i=0; i < $scope.stepList.length; i++){
						if($scope.stepList[i].getIdStep() === idStep){
							$scope.stepList[i].mapVisible = true;
							$scope.stepList[i].messageInfoResultGeo = 'Geolocalizzazione in corso...';
							$scope.stepList[i].mapList = new Array();
							$scope.stepList[i].mapList.push(new Object());
						}
					}
					$scope.$on('GEOLOCATION_NOT_AVAILABLE',function(event){
						for(var i=0; i < $scope.stepList.length; i++){
							if($scope.stepList[i].getIdStep() === idStep){
								$scope.stepList[i].mapVisible = false;
								$scope.stepList[i].messageInfoResultGeo = '';
								$scope.stepList[i].mapList = undefined;
							}
						}
					});


					// Quando la rilevazione della geolocalizzazione è stata eseguita
					// viene eseguito questo meotodo che raccoglie i dati della
					// geolocazione
					$scope.$on('GEOLOCATION_IS_END',function(event,geoLocation){
						
						$scope.buttonMyPosition = "Rileva nuovamente la mia posizione";

						var dataCollection = StandardUserStep();
						dataCollection.setInsertedLatitude(geoLocation['Latitude']);
						dataCollection.setInsertedLongitude(geoLocation['Longitude']);
						dataCollection.setInsertedText('null');
						dataCollection.setInsertedPhoto('null');
						dataCollection.setIdStep(idStep);

						var messageToDisplay = 'La tua posizione attuale è stata rilevata';
					    var messageLatitude =  geoLocation['Latitude'];
						var messageLongitude = geoLocation['Longitude'];

						// Preparo un JSONObject per i dati che saranno poi inviati al back-end
						// e poi invoco il metodo per l'invio dei dati
						var dataToSend = dataCollection.makeJSONObject();

						// StandardUserRequestManager.doSendUserStep($scope,dataToSend);
						// return dataToSend;
						for(var i=0; i < $scope.stepList.length; i++){
							if($scope.stepList[i].getIdStep() === idStep){
								$scope.$apply(function(){
								$scope.stepList[i].messageInfoResultGeo =  messageToDisplay;
								$scope.stepList[i].messageInfoLatitude = messageLatitude;
								$scope.stepList[i].messageInfoLongitude = messageLongitude;
								$scope.stepList[i].elementGeoVisible = false;
								$scope.stepList[i].dataGeolocation = dataToSend;
								
								});
							}
						}
					});
				}
				catch(error){
					for(var i=0; i < $scope.stepList.length; i++){
						alert(error);
						if($scope.stepList[i].getIdStep() === idStep){
							$scope.stepList[i].messageInfoResultGeo = error;
						}
					}
				}
			};

            $scope.doStep = function(idStep,insertedText){
                try{
                	
                    // Resetto il campo di testo di notifica
                    for(var i=0; i < $scope.stepList.length; i++){
                        if($scope.stepList[i].getIdStep() === idStep){
                            $scope.stepList[i].messageNotification = '';
                        }
                    }

                    var dataToSend = {
                        'Photo': 'null',
                        'Text': 'null',
                        'Latitude': 'null',
                        'Longitude': 'null',
                        'WrongGeolocation': false,
                        'WrongText': false,
                        'WrongPhoto': false,
                        'IdProcess': Number(localStorageService.get('IdProcess')),
                        'IdStep': idStep
                    };

                    // Memorizzo i dati del passo o sellevo una eccezione se
                    // i campi sono vuoti
                    for(var i=0; i < $scope.stepList.length; i++){
                        if($scope.stepList[i].getIdStep() === idStep){

                            dataToSend.Photo = StandardUser.getStepList()[i].getInsertedPhoto();
                            dataToSend.Text = StandardUser.getStepList()[i].getInsertedText();
                            dataToSend.Longitude = StandardUser.getStepList()[i].getInsertedLongitude();
                            dataToSend.Latitude = StandardUser.getStepList()[i].getInsertedLatitude();
                            dataToSend.WrongGeolocation = new Boolean(StandardUser.getStepList()[i].getWrongGeolocation() === true);
                            dataToSend.WrongText = new Boolean(StandardUser.getStepList()[i].getWrongText() === true);
                            dataToSend.WrongPhoto = new Boolean(StandardUser.getStepList()[i].getWrongPhoto() === true);

                            if((StandardUser.getStepList()[i].getWrongGeolocation() === false ||
                                    StandardUser.getStepList()[i].getWrongGeolocation() === undefined) &&
                                    StandardUser.getStepList()[i].getIsGeolocation() === 'true'){
                                if(!($scope.stepList[i].dataGeolocation === undefined)){
                                    dataToSend.Latitude = $scope.stepList[i].dataGeolocation.Latitude;
                                    dataToSend.Longitude = $scope.stepList[i].dataGeolocation.Longitude;
                                }
                                else{
                                    throw 'Geolocalizzazione obbligatoria';
                                }
                            }

                            if((StandardUser.getStepList()[i].getWrongText() === false ||
                                    StandardUser.getStepList()[i].getWrongText() === undefined) &&
                                    StandardUser.getStepList()[i].getIsText() === 'true'){

                                if(!((insertedText === undefined ) || (insertedText === ''))){
                                    dataToSend.Text = insertedText;
                                }
                                else{
                                    throw 'Testo obbligatorio';
                                }
                            }

                            if((StandardUser.getStepList()[i].getWrongPhoto() === false ||
                                    StandardUser.getStepList()[i].getWrongPhoto() === undefined) &&
                                    StandardUser.getStepList()[i].getIsPhoto() === 'true'){
                                if(!($scope.file === undefined )){
                                    dataToSend.Photo = $scope.file;
                                }
                                else{
                                    throw 'Foto obbligatoria';
                                }
                            }
                        }
                    }
                    StandardUserRequestManager.doSendUserStep($scope,dataToSend);
                }
                catch(error){
                    for(var i=0; i < $scope.stepList.length; i++){
                        if($scope.stepList[i].getIdStep() === idStep){
                            $scope.stepList[i].messageNotification = error;
                        }
                    }
                }
            };
	
           
            $scope.$on('SEND_USER_STEP_IS_END',function(event,result){
            	if(result == 'processNotActive'){
            		alert('Il processo in cui stai tentando di avanzare è stato disattivato da un amministratore.' +
            				' Verrai rimandato alla pagina dei tuoi processi');
            		LocationManager.navigateTo('/activeprocess');
            	}
            	else if(result == 'successSendData'){
            		
            		StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                    $scope.messageVisible = true;
                    $scope.messageData = 'Dati inviati con successo';
                    $scope.scrollOnTop();
            	}
            	else if(result == 'connectionError'){
                    $scope.messageVisible = true;
                    $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
            	else if(result == 'notSuccessAutoVerify'){
            		StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                    $scope.messageVisible = true;
                    $scope.messageData = 'Dati inviati con successo';
                    $scope.scrollOnTop();
            	}
            	else if(result == 'successAutoVerify'){
            		StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
            		$scope.messageVisible = true;
                    $scope.messageData = 'Dati inviati con successo';
                    $scope.scrollOnTop();
            	}
            });

            $scope.onFileSelect = function($files,idStep) {
                $scope.file = $files[0];
                var fileExtension = $scope.file.type;
                if(fileExtension == 'image/jpeg' || fileExtension == 'image/png'){
                	 // Limite dimensione file in Byte
                    var fileSizeLimit = 3*1024*1024;
                    if($scope.file.size <= fileSizeLimit){
                        var reader = new window.FileReader();
                        reader.readAsDataURL($scope.file);
                        reader.onloadend = function() {
                            $scope.base64Photo = reader.result;
                            for(var i = 0; i < $scope.stepList.length; i++){
                                if($scope.stepList[i].getIdStep() === idStep){
                                	$scope.stepList[i].imageList = new Array();
                                    $scope.$apply(function(){
                                    	$scope.stepList[i].imageList.push(new Object());
                                        $scope.stepList[i].image = $scope.base64Photo;
                                    });
                                }
                            }
                        };
                        reader.onloadend();
                    }
                    else{
                        alert("L'immagine che stai tentando di caricare supera la dimensione (in MB) massima permessa");
                    }
                }
                else{
                	alert("Formato immagine non supportato. Sono supportati solamente *.jpg, *.jpeg, *.png");
                }
            };

            $scope.sendContentLocalStorageData = function(idStep){
                var idLocalData = 'ID:'+String(LocationManager.getIdUserCookie()) + '_STEP:'+String(idStep);
                var localDataToSend = localStorageService.get(idLocalData);
                StandardUserRequestManager.doSendUserStep($scope,localDataToSend);
                $scope.deleteContentLocalStorageData(idStep,false);
            	$scope.messageVisible = true;
                $scope.messageData = 'Dati locali del passo inviati con successo';
                $scope.scrollOnTop();
            };
		
            $scope.deleteContentLocalStorageData = function(idStep,showDeleteMessage){
               var idLocalData = 'ID:'+String(LocationManager.getIdUserCookie()) + '_STEP:'+String(idStep);

                var idStepKey = idLocalData.replace('ID:'+String(LocationManager.getIdUserCookie()) + '_STEP:','');

                for(var i=0; i < $scope.stepList.length; i++){
                    if($scope.stepList[i].getIdStep() == idStepKey){

                        $scope.stepList[i].localIsDefined = undefined;
                        $scope.stepList[i].messageNotification = undefined;
                        $scope.stepList[i].insertedText = undefined;
                        $scope.stepList[i].image = undefined;

                        if(($scope.stepList[i].getState() === 'TOVERIFY' &&
                                $scope.stepList[i].getWrongGeolocation() === false) ||
                                !($scope.stepList[i].contentLocalStorageData.Latitude === undefined)
                                ){
                            $scope.stepList[i].mapList = undefined;
                            $scope.stepList[i].mapVisible = undefined;
                        }
                        $scope.stepList[i].contentLocalStorageData = undefined;
                    }
                }
                localStorageService.remove(idLocalData);
                if(showDeleteMessage != false){
    				$scope.messageVisible = true;
                    $scope.messageData = 'Dati locali del passo eliminati con successo';
                    $scope.scrollOnTop();
                }
            };
	    }
	}
}]);
