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
 * Name : AdminUserSpec.js
 * Target : AdminUser
 * Location : /test/unit/model/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-25     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('MODEL - AdminUser', function (){
    var adminUser;

    beforeEach(function (){
        module('AdminUser');

        inject(function($injector) {
            adminUser = $injector.get('AdminUser');
        });
    });

    it('should set the user list', function () {
        var obj = [{name: 'Phil', value: 'user'},{name: 'Luke', value: 'admin' }];
        adminUser.setUserList(obj);
        expect(adminUser.getUserList()[1]['name']).toEqual('Luke');
    });

    it('should set the step list', function (){
        var obj = [{description: 'a', isPhoto: 'true'},{description: 'b', isPhoto: 'false' }];
        adminUser.setStepList(obj);
        expect(adminUser.getStepList()[0]['description']).toEqual('a');
    });

    it('should be able to get all steps from all process', function(){
        var pList = [{
         stepsList:[{
           IdStep: '1'
         },{
           IdStep: '2'
         }]
        }];
        adminUser.setProcessList(pList);
        expect(adminUser.getAllStepsFromAllProcess()).toEqual([{IdStep: '1'},{IdStep: '2'}]);
    });
});