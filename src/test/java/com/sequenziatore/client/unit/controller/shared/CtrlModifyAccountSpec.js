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
 * Name : CtrlModifyAccountSpec.js
 * Target : CtrlModifyAccount
 * Location : /test/unit/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-26     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlModifyAccount', function (){
    var StandardUserMock;
    var LocationManagerMock;
    var StandardUserRequestManagerMock;
    var AdminUserMock;
    var AdminUserRequestManagerMock;
    var RegularExpressionsMock;
    var DistrictListMock;

    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['getFacebookCookie','handleModifyAccountLocation','isLogged','isAdmin']);
        RegularExpressionsMock = jasmine.createSpyObj('RegularExpressions',['test']);
        DistrictListMock = jasmine.createSpyObj('DistrictList',['']);
        DistrictListMock.DistrictList = [] ;

        RegularExpressionsMock.regularExpressionUsername= new RegExp('^[a-zA-Z][a-zA-Z0-9]+([.][a-zA-Z0-9]+|[a-zA-Z0-9]*)[a-zA-Z0-9]$');
        RegularExpressionsMock.regularExpressionEmail = new RegExp('^[a-zA-Z0-9][a-zA-Z0-9]+([.][a-zA-Z0-9]+|[a-zA-Z0-9]*)[@][a-zA-Z0-9]+[.][a-z]{2,3}');
        RegularExpressionsMock.regularExpressionPassword = new RegExp('');
        RegularExpressionsMock.regularExpressionName = new RegExp('');
        RegularExpressionsMock.regularExpressionSurname = new RegExp('');


        module('CtrlModifyAccount');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.scrollOnTop = function () {};

            // Creo un controller fittizio
            ctrl = $controller('CtrlModifyAccount', {
                $scope: $scope,
                LocationManager : LocationManagerMock,
                RegularExpressions : RegularExpressionsMock,
                DistrictList : DistrictListMock
            });
        });
    });

    it ("should handleProfileLocation have been called",function(){
        expect(LocationManagerMock.handleModifyAccountLocation).toHaveBeenCalled();
    });

    describe("CONTROLLER - CtrlModifyAccount an admin",function(){

        beforeEach(function(){
            AdminUserMock = jasmine.createSpyObj('AdminUser',['getUsername','getName','getSurname','getEmail','getIsAdmin','getDistrict','getCity']);
            AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getAdminInfo']);
            AdminUserMock.getUsername.andReturn("testAdminUsername");
            AdminUserMock.getName.andReturn("testAdminName");
            AdminUserMock.getEmail.andReturn("testAdmin@email.com");
            AdminUserMock.getIsAdmin.andReturn("testAdminIsAdmin");
            AdminUserMock.getDistrict.andReturn("testAdminDistrict");
            AdminUserMock.getCity.andReturn("testAdminCity");

            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('true');

            inject (function($rootScope, $controller){
                // crea uno scope fittizio
                $scope = $rootScope.$new();

                // crea un controller fittizio
                ctrl = $controller('CtrlModifyAccount', {
                    $scope: $scope,
                    AdminUser : AdminUserMock,
                    LocationManager : LocationManagerMock,
                    AdminUserRequestManager : AdminUserRequestManagerMock,
                    RegularExpressions : RegularExpressionsMock,
                    DistrictList : DistrictListMock
                });
            });
        });

        it ("should has logged as Admin",function(){
            expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
            $scope.openWebSocket = function(variable){};
            $scope.$broadcast('REQUEST_INFO_IS_END','');
            expect($scope.username).toEqual("testAdminUsername");
            expect($scope.name).toEqual("testAdminName");
            expect($scope.email).toEqual("testAdmin@email.com");
            expect($scope.isAdmin).toEqual("testAdminIsAdmin");
            expect($scope.district).toEqual("testAdminDistrict");
            expect($scope.city).toEqual("testAdminCity");
            expect($scope.user).toEqual("testAdminName");
        });
    });

    describe("CONTROLLER - CtrlModifyAccount an user",function(){
        beforeEach(function(){
            StandardUserMock = jasmine.createSpyObj('StandardUser',['getUsername','getName','getSurname','getEmail','getIsAdmin','getDistrict','getCity']);
            StandardUserRequestManagerMock =  jasmine.createSpyObj('StandardUserRequestManager',['getUserInfo','doModifyAccount']);
            StandardUserMock.getUsername.andReturn("testStandardUserUsername");
            StandardUserMock.getName.andReturn("testStandardUserName");
            StandardUserMock.getEmail.andReturn("testStandardUser@email.com");
            StandardUserMock.getIsAdmin.andReturn("testStandardUserIsAdmin");
            StandardUserMock.getDistrict.andReturn("testStandardUserDistrict");
            StandardUserMock.getCity.andReturn("testStandardUserCity");

            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('false');

            inject (function($rootScope, $controller){
                // crea uno scope fittizio
                $scope = $rootScope.$new();

                // crea un controller fittizio
                ctrl = $controller('CtrlModifyAccount', {
                    $scope: $scope,
                    StandardUser : StandardUserMock,
                    LocationManager : LocationManagerMock,
                    StandardUserRequestManager : StandardUserRequestManagerMock,
                    RegularExpressions : RegularExpressionsMock,
                    DistrictList : DistrictListMock

                });
            });
        });

        it ("should has logged as User",function(){
            expect(StandardUserRequestManagerMock.getUserInfo).toHaveBeenCalled();
            $scope.openWebSocket = function(variable){};
            $scope.$broadcast('REQUEST_INFO_IS_END','');
            expect($scope.username).toEqual("testStandardUserUsername");
            expect($scope.name).toEqual("testStandardUserName");
            expect($scope.email).toEqual("testStandardUser@email.com");
            expect($scope.isAdmin).toEqual("testStandardUserIsAdmin");
            expect($scope.district).toEqual("testStandardUserDistrict");
            expect($scope.city).toEqual("testStandardUserCity");
            expect($scope.user).toEqual("testStandardUserName");
        });

        it ("should do modifyAccount action",function(){
            var dataToSend = {
                'Username': "testUsername",
                'Password': "testPassword",
                'Email': "email@test.com",
                'Name': "testName",
                'Surname': "testSurame",
                'City': "testCity",
                'District': "testDistrict",
                'IsAdmin': true
            };
            $scope.username = "testUsrname";
            $scope.password = "testPassword";
            $scope.reinsertedPassword= "testPassword";
            $scope.oldPassword= "testPassword";
            $scope.email = "email@test.com";
            $scope.name = "testName";
            $scope.surname = "testSurame";
            $scope.city= "testCity";
            $scope.district= "testDistrict";
            $scope.messageError="";
            $scope.showAlertS="";
            $scope.doModifyAccount();
            expect(StandardUserRequestManagerMock.doModifyAccount).toHaveBeenCalled();
            $scope.scrollOnTop = function(){};
            $scope.$broadcast("MODIFY_ACCOUNT_IS_END",dataToSend);
            expect($scope.messageInfoResult).toEqual(dataToSend);
            expect($scope.messageError).toEqual("");
            expect($scope.showAlertS).toEqual("true");

            $scope.email = "";
            $scope.messageError="";
            $scope.showAlertD="";
            $scope.doModifyAccount();
            expect($scope.messageInfoResult).toEqual("");
            expect($scope.messageError).toEqual("Email obbligatoria");
            expect($scope.showAlertD).toEqual("true");
        });

        it ("should check if the Username is write in a correct form",function(){
            var usr="";
            $scope.username=usr;
            $scope.errorUsername="";
            $scope.okUsername="";
            try{$scope.checkUsername();}
            catch(test){
                expect(test).toEqual("Username obbligatorio");
            }

            var usr="Usr";
            $scope.username=usr;
            try{$scope.checkUsername();}
            catch(test){
                expect($scope.errorUsername).toEqual('Lo username deve essere compreso tra 4 e 10 caratteri');
                expect($scope.okUsername).toEqual('');
                expect(test).toEqual('Errore inserimento username');
            }

            var usr="testUsrname.";
            $scope.username=usr;
            try{
                $scope.checkUsername();
                expect(RegularExpressionsMock.regularExpressionUsername.test).toHaveBeenCalled();
            }
            catch(test){
                expect($scope.errorUsername).toEqual('L\'username non può contenere caratteri speciali');
                expect($scope.okUsername).toEqual('');
                expect(test).toEqual('L\'username non può contenere caratteri speciali');
            }

            var usr="testUsername";
            $scope.username=usr;
            $scope.checkUsername();
            expect($scope.errorUsername).toEqual('');
        });

        it ("should check if the password is write in a correct form",function(){
            var psw="";
            $scope.password=psw;
            $scope.errorPassword="";
            $scope.okPassword="";
            try{
                $scope.checkPassword();
            }
            catch(test){
                expect(test).toEqual('Per sicurezza reinserisci la password');
            }

            psw="test";
            $scope.password=psw;
            $scope.errorPassword="";
            $scope.okPassword="";
            try{
                $scope.checkPassword();
            }
            catch(test){
                expect($scope.errorPassword).toEqual('La password deve essere compresa tra 6 e 12 caratteri');
                expect($scope.okPassword).toEqual('');
                expect(test).toEqual('La password deve essere compresa tra 6 e 12 caratteri');
            }

            psw="testPassword";
            pswRe=undefined;
            $scope.password=psw;
            $scope.reinsertedPassword=pswRe;
            try{
                $scope.checkPassword();
            }
            catch(test){
                expect($scope.errorPassword).toEqual('Per sicurezza reinserisci la password');
                expect($scope.okPassword).toEqual('');
                expect(test).toEqual('Per sicurezza reinserisci la password');
            }

            psw="testPassword";
            pswRe="testpassword";
            $scope.password=psw;
            $scope.reinsertedPassword=pswRe;
            try{
                $scope.checkPassword();
            }
            catch(test){
                expect($scope.errorPassword).toEqual('Le password non coincidono');
                expect($scope.okPassword).toEqual('');
                expect(test).toEqual('Le password non coincidono');
            }

            psw="testPassword";
            pswRe="testPassword";
            $scope.password=psw;
            $scope.reinsertedPassword=pswRe;
            try{
                $scope.checkPassword();
            }
            catch(test){
                expect($scope.errorPassword).toEqual('');
            }

            psw="PSW";
            $scope.oldPassword=psw
            $scope.passowrd="password";
            $scope.reinsertedPassowrd="password";
            try{
                $scope.checkPassword();
            }
            catch(test){
                expect($scope.okPassword).toEqual('');
                expect($scope.errorPassword).toEqual('La password deve avere almeno 6 caratteri');
                expect(test).toEqual('Errore inserimento password');
            }

            psw="";
            $scope.oldPassword=psw;
            $scope.passowrd="password";
            try{
                $scope.checkPassword();
            }
            catch(test){
                expect(test).toEqual('Vecchia password non inserita');
            }
        });

        it ("should check if the email is write in a correct form",function(){

            var email="";
            $scope.email=email;
            $scope.errorEmail="";
            $scope.okEmail="";
            try{
                $scope.checkEmail();
            }
            catch(test){
                expect(test).toEqual("Email obbligatoria");
            }

            email="email.it";
            $scope.email=email;
            $scope.errorEmail="";
            $scope.okEmail="";
            try{
                $scope.checkEmail();
            }
            catch(test){
                expect($scope.errorEmail).toEqual("L'email non rispetta il pattern");
                expect($scope.okEmail).toEqual("");
                expect(test).toEqual("Errore inserimento email");
            }

            email="email@test.it";
            $scope.email=email;
            $scope.errorEmail="";
            $scope.okEmail="";
            try{
                $scope.checkEmail();
            }
            catch(test){
                expect($scope.errorEmail).toEqual("");
            }

        });

        it ("should check if the name is write in a correct form",function(){

            var name=new Object();
            $scope.name=name;
            $scope.errorName="";
            $scope.okName="";
            try{
                $scope.checkName();
            }
            catch(test){
                expect($scope.errorName).toEqual("Nome non valido");
                expect($scope.okName).toEqual("");
                expect(test).toEqual("Errore inserimento nome");
            }

            name="nameTest";
            $scope.name=name;
            $scope.errorName="";
            $scope.okName="";
            $scope.checkName();
            expect($scope.errorName).toEqual("");
        });

        it ("should check if the surname is write in a correct form",function(){
            var surname="";
            $scope.surname=surname;
            $scope.errorSurname="";
            $scope.okSurname="";
            try{
                $scope.checkSurname();
            }
            catch(test){
                expect($scope.errorName).toEqual("Cognome non valido");
                expect($scope.okName).toEqual("");
                expect(test).toEqual("Errore inserimento cognome");
            }
            surnamename="surnameTest";
            $scope.name=surnamename;
            $scope.errorName="";
            $scope.okName="";
            $scope.checkSurname();
            expect($scope.errorSurname).toEqual("");
        });
    });
});