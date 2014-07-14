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
 * Name : AdminUserStepSpec.js
 * Target : AdminUserStep
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

describe("MODEL - AdminUserStep", function(){
	var adminUserStep;
	
	beforeEach(function(){
		adminUserStep = new AdminUserStep();
	});
	
	it("should set the step index", function(){
		adminUserStep.setIndexStep(42);
		expect(adminUserStep.getIndexStep()).toEqual(42);
	});
	
	it("should check the step's longitude", function(){
		adminUserStep.setCheckLongitude(42);
		expect(adminUserStep.getCheckLongitude()).toEqual(42);
	});
	
	it("should chek the step's latitude ", function(){
		adminUserStep.setCheckLatitude(42);
		expect(adminUserStep.getCheckLatitude()).toEqual(42);
	});
	
	it("should check step's text", function(){
		adminUserStep.setCheckText(42);
		expect(adminUserStep.getCheckText()).toEqual(42);
	});
});