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
 * Name : AdminProcessNotActiveRouting.js
 * Module : AdminProcessNotActiveRouting
 * Location : /applicationContent/com/sequenziatore/client/controller/admin
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


var application = angular.module('AdminProcessNotActiveRouting',['ngRoute','CtrlAdminProcessNotActive']);

application.config(['$routeProvider', function($routeProvider){
    $routeProvider.when('/adminprocessnotactive',
        {
            templateUrl: 'applicationContent/com/sequenziatore/client/view/admin/adminprocessnotactive.html'
        });
}]);
