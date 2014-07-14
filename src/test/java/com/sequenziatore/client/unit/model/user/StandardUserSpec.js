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
 * Name : StandardUserSpec.js
 * Target : StandardUser
 * Location : /test/unit/model/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('MODEL - StandardUser', function (){
    var standardUser;

    beforeEach(function (){

        module('StandardUser');

        inject(function($injector) {
            standardUser = $injector.get('StandardUser');
            $rootScope = $injector.get('$rootScope');

            spyOn($rootScope, '$broadcast').andCallThrough();
;
        });
    });

    it('should be able to set an empty step list', function (){
        var obj = {};
        standardUser.setStepList(obj);
        expect(standardUser.getStepList()).toEqual(obj);
    });

    it('should be able to set a mono-step list', function (){
        var obj = [{
            IdStep: '1',
            Level: '1',
            Description: 'Some description',
            IsPhoto: 'true',
            IsText: 'true',
            IsGeolocation: 'false',
            AdminVerify: 'false',
            Parallelism: 'AND',
            State: 'active'
        }];
        standardUser.setStepList(obj);
        expect(standardUser.getStepList()[0].getIdStep()).toEqual(obj[0]['IdStep']);
        expect(standardUser.getStepList()[0].getLevel()).toEqual(obj[0]['Level']);
        expect(standardUser.getStepList()[0].getDescription()).toEqual(obj[0]['Description']);
    });

    it('should be able to handle the geolocation successfully', function(){
        inject(function(){
            spyOn(navigator.geolocation,"getCurrentPosition").andCallFake(function() {
                var position = { coords: { latitude: 45, longitude: 45 } };
                arguments[0](position);
            });
        });
        standardUser.getGeolocation($rootScope);
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GEOLOCATION_IS_END', { Latitude : 45, Longitude : 45 });
    });
});