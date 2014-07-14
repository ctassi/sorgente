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
 * Name : ApplicationConfiguration.js
 * Module : SeqApp
 * Location : /applicationContent/com/sequenziatore/client
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ------------------------------------------------------
 * 0.0.2           2014-04-08     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornamento Filtri e aggiunte growl
 * ======================================================
 */

var application = angular.module('SeqApp',
								['LoginRouting','RegistrationRouting','LogoutRouting',
								 'ProfileRouting','WelcomeRouting','CreateProcessRouting',
								 'RegistrationNewAdminRouting','AdminProcessRouting','StepValidatorRouting',
                                 'ActiveProcessRouting','AvailableProcessRouting',
								 'ListUserProcessRouting','ModifyProcessRouting','ModifyAccountRouting',
								 'CurrentStepRouting','AdminReportRouting','RecoveryPasswordRouting',
								 'AdminProcessNotActiveRouting','ClosedProcessRouting',
								 'StandardUserRequestManager','LocalStorageModule',
								 'ngAnimate','angular-growl','ui.bootstrap','ezfb','chieffancypants.loadingBar']);


application.config(['$routeProvider', function($routeProvider){
	$routeProvider.otherwise({
		redirectTo: '/login'
		});
}]);

application.config(['growlProvider', function(growlProvider){
    growlProvider.onlyUniqueMessages(false);
    growlProvider.globalTimeToLive(3000);
}]);

application.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeBar = false;
});

application.controller('CtrlGlobal',['$scope','LocationManager','growl','StandardUserRequestManager','localStorageService','ezfb',
                                     function($scope,LocationManager,growl,StandardUserRequestManager,localStorageService,ezfb){
	
	$scope.socket = undefined;
	$scope.stompClient = undefined;
	$scope.isConnected = undefined;

	//Metodo che gestisce la visibilità dei menu della applicazione
	//a seconda che l'utente sia loggato o meno e a seconda se
	//l'utente loggato è un admin o un utente normale
	$scope.handlerMenu = function(){
		if(LocationManager.isLogged() === 'true'){
			$scope.logged = 'true';
			if(LocationManager.isAdmin() === 'true'){
				$scope.admin = 'true';
			}
			else{
				$scope.admin = undefined;
			}
		}
		else{
			$scope.logged = undefined;
			$scope.admin = undefined;
			
		}
	};
	
	$scope.openWebSocket = function(address){
		if($scope.socket == undefined){
			$scope.socket = new SockJS(address);
			$scope.stompClient = Stomp.over($scope.socket);
			
			// callback function to be called when stomp client is connected to server
			var connectCallback = function(frame) {
			    $scope.stompClient.subscribe('/notificationlistener', function(msg){
				    if($scope.$eval(msg['body'])['actionType'] == 'stepValidation' && $scope.$eval(msg['body'])['username'] == LocationManager.getUserCookie()){
                        $scope.$apply(function(){
                            if($scope.$eval(msg['body'])['success'] == 'true'){
                                $scope.addSuccessMessage($scope.$eval(msg['body'])['message']);
                                StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                            }
                            else{
                                $scope.addErrorMessage($scope.$eval(msg['body'])['message']);
                                StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                            }
                        });
                    }
				});
            };
            // callback function to be called when stomp client could not connect to server
            var errorCallback = function(error) {
                // display an error message
                $scope.closeWebSocket();
            };
			$scope.stompClient.connect('guest','guest', connectCallback, errorCallback);
		}
	};
	
	$scope.closeWebSocket = function(){
		if(!($scope.socket === undefined) && !($scope.stompClient === undefined)){
			$scope.stompClient.disconnect(function() {
				$scope.socket = undefined;
				$scope.stompClient = undefined;
			});
		}
	};
	
	$scope.sendNotification = function(address,dataToSend){
        $scope.stompClient.send(address, {}, dataToSend);
	};

	$scope.getTimes=function(n){
	     return new Array(n);
	};

    $scope.addInfoMessage = function(msg) {
        growl.addInfoMessage(msg);
    };
    
    $scope.addSuccessMessage = function(msg){
    	growl.addSuccessMessage(msg);
    };
	
    $scope.addErrorMessage = function(msg){
    	growl.addErrorMessage(msg);
    };

    // Facebook Login
     $scope.updateLoginStatus = function(more) {
         ezfb.getLoginStatus(function (res) {
             $scope.loginStatus = res;
             (more || angular.noop)();
         });
     };

     $scope.updateApiMe = function() {
         ezfb.api('/me', function (res) {
             $scope.apiMe = res;
             $scope.$broadcast('REQUEST_FB_API_IS_END');
         });
     };

     $scope.scrollOnTop = function(){
         $('body,html').animate({
             scrollTop: 0
         }, 800);
     };

     $scope.handlerMenu();
}]);

// Dichiarazione delle costante da iniettare nei controller
// ^[a-zA-Z] primo carattere deve essere una lettera maiuscola o minuscola
// [a-zA-Z0-9]+ una serie di 1 o più dei caratteri presenti nelle parentesi
// [a-zA-Z0-9]$ l'ultimo carattere deve essere uno dei caratteri delle parentesi

application.constant('RegularExpressions',{
	regularExpressionUsername: new RegExp('^[a-zA-Z][a-zA-Z0-9]+([a-zA-Z0-9]+|[a-zA-Z0-9]*)[a-zA-Z0-9]$'),
	regularExpressionEmail: new RegExp('^[a-zA-Z0-9]([.]?[_]?[-]?[.]?)+[a-zA-Z0-9]+(([.]?[_]?[-]?[.]?)+[a-zA-Z0-9]+|[a-zA-Z0-9]*)[@][a-zA-Z0-9]+[.]([a-z]{2,3})$'),
	regularExpressionPassword: new RegExp(''),
	regularExpressionName: new RegExp(''),
	regularExpressionSurname: new RegExp(''),
	regularExpressionWhiteSpaces: new RegExp('\s')

});

application.constant('DistrictList',{
	DistrictList: districtList = [
	                  			{district:'Agrigento', abbrev:'AG'},
	                			{district:'Alessandria', abbrev:'AL'},
	                			{district:'Ancona', abbrev: 'AN'},
	                			{district:'Aosta', abbrev: 'AO'},
	                			{district:'Arezzo', abbrev:'AR'},
	                			{district:'Ascoli Piceno', abbrev: 'AP'},
	                			{district:'Asti', abbrev: 'AT'},
	                			{district:'Avellino', abbrev: 'AV'},
	                			{district:'Bari', abbrev: 'BA'},
	                			{district:'Barletta-Andria-Trani', abbrev: 'BT'},
	                			{district:'Belluno', abbrev: 'BL'},
	                			{district:'Benevento', abbrev: 'BN'},
	                			{district:'Bergamo', abbrev: 'BG'},
	                			{district:'Biella', abbrev: 'BI'},
	                			{district:'Bologna', abbrev: 'BO'},
	                			{district:'Bolzano', abbrev: 'BZ'},
	                			{district:'Brescia', abbrev: 'BS'},
	                			{district:'Brindisi', abbrev: 'BR'},
	                			{district:'Cagliari', abbrev: 'CA'},
	                			{district:'Caltanissetta', abbrev: 'CL'},
	                			{district:'Campobasso', abbrev: 'CB'},
	                			{district:'Carbonia-Iglesias', abbrev: 'CI'},
	                			{district:'Caserta', abbrev: 'CE'},
	                			{district:'Catania', abbrev: 'CT'},
	                			{district:'Catanzaro', abbrev: 'CZ'},
	                			{district:'Chieti', abbrev: 'CH'},
	                			{district:'Como', abbrev: 'CO'},
	                			{district:'Cosenza', abbrev: 'CS'},
	                			{district:'Cremona', abbrev: 'CR'},
	                			{district:'Crotone', abbrev: 'KR'},
	                			{district:'Cuneo', abbrev: 'CN'},
	                			{district:'Enna', abbrev: 'EN'},
	                			{district:'Fermo', abbrev: 'FM'},
	                			{district:'Ferrara', abbrev: 'FE'},
	                			{district:'Firenze', abbrev: 'FI'},
	                			{district:'Foggia', abbrev: 'FG'},
	                			{district:'Forlì-Cesena', abbrev: 'FC'},
	                			{district:'Frosinone', abbrev: 'FR'},
	                			{district:'Genova', abbrev: 'GE'},
	                			{district:'Gorizia', abbrev: 'GO'},
	                			{district:'Grosseto', abbrev: 'GR'},
	                			{district:'Imperia', abbrev: 'IM'},
	                			{district:'Isernia', abbrev: 'IS'},
	                			{district:'La Spezia', abbrev: 'SP'},
	                			{district:"L'Aquila", abbrev: 'AQ'},
	                			{district:'Latina', abbrev: 'LT'},
	                			{district:'Lecce', abbrev: 'LE'},
	                			{district:'Lecco', abbrev: 'LC'},
	                			{district:'Livorno', abbrev: 'LI'},
	                			{district:'Lodi', abbrev: 'LO'},
	                			{district:'Lucca', abbrev: 'LU'},
	                			{district:'Macerata', abbrev: 'MC'},
	                			{district:'Mantova', abbrev: 'MN'},
	                			{district:'Massa-Carrara', abbrev: 'MS'},
	                			{district:'Matera', abbrev: 'MT'},
	                			{district:'Messina', abbrev: 'ME'},
	                			{district:'Milano', abbrev: 'MI'},
	                			{district:'Modena', abbrev: 'MO'},
	                			{district:'Monza e della Brianza', abbrev: 'MB'},
	                			{district:'Napoli', abbrev: 'NA'},
	                			{district:'Novara', abbrev: 'NO'},
	                			{district:'Nuoro', abbrev: 'NU'},
	                			{district:'Olbia-Tempio', abbrev: 'OT'},
	                			{district:'Oristano', abbrev: 'OR'},
	                			{district:'Padova', abbrev: 'PD'},
	                			{district:'Palermo', abbrev: 'PA'},
	                			{district:'Parma', abbrev: 'PR'},
	                			{district:'Pavia', abbrev: 'PV'},
	                			{district:'Perugia', abbrev: 'PG'},
	                			{district:'Pesaro e Urbino', abbrev: 'PU'},
	                			{district:'Pescara', abbrev: 'PE'},
	                			{district:'Piacenza', abbrev: 'PC'},
	                			{district:'Pisa', abbrev: 'PI'},
	                			{district:'Pistoia', abbrev: 'PT'},
	                			{district:'Pordenone', abbrev: 'PN'},
	                			{district:'Potenza', abbrev: 'PZ'},
	                			{district:'Prato', abbrev: 'PO'},
	                			{district:'Ragusa', abbrev: 'RG'},
	                			{district:'Ravenna', abbrev: 'RA'},
	                			{district:'Reggio Calabria', abbrev: 'RC'},
	                			{district:'Reggio Emilia', abbrev: 'RE'},
	                			{district:'Rieti', abbrev: 'RI'},
	                			{district:'Rimini', abbrev: 'RN'},
	                			{district:'Roma', abbrev: 'RM'},
	                			{district:'Rovigo', abbrev: 'RO'},
	                			{district:'Salerno', abbrev: 'SA'},
	                			{district:'Medio Campidano', abbrev: 'VS'},
	                			{district:'Sassari', abbrev: 'SS'},
	                			{district:'Savona', abbrev: 'SV'},
	                			{district:'Siena', abbrev: 'SI'},
	                			{district:'Siracusa', abbrev: 'SR'},
	                			{district:'Sondrio', abbrev: 'SO'},
	                			{district:'Taranto', abbrev: 'TA'},
	                			{district:'Teramo', abbrev: 'TE'},
	                			{district:'Terni', abbrev: 'TR'},
	                			{district:'Torino', abbrev: 'TO'},
	                			{district:'Ogliastra', abbrev: 'OG'},
	                			{district:'Trapani', abbrev: 'TP'},
	                			{district:'Trento', abbrev: 'TN'},
	                			{district:'Treviso', abbrev: 'TV'},
	                			{district:'Trieste', abbrev:'TS'},
	                			{district:'Udine', abbrev: 'UD'},
	                			{district:'Varese', abbrev: 'VA'},
	                			{district:'Venezia', abbrev:'VE'},
	                			{district:'Verbano-Cusio-Ossola', abbrev:'VB'},
	                			{district:'Vercelli', abbrev: 'VC'},
	                			{district:'Verona' , abbrev:'VR'},
	                			{district:'Vibo Valentia', abbrev: 'VV'},
	                			{district:'Vicenza', abbrev: 'VI'},
	                			{district:'Viterbo', abbrev:'VT'}
	                			]
});

//Filtro per la paginazione dei contenuti
application.filter('startFrom', function() {
    return function(input, start) {
    	input = input || '';
        start = +start;
        return input.slice(start);
    };
});
