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
 * Name : RequestManagerSpec.js
 * Target : RequestManager
 * Location : /test/unit/model/shared/superclasses
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-26     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe("MODEL - RequestManager", function(){
	 var requestManager;
	 
	 beforeEach(function(){
		requestManager = new RequestManager(); 
	 });
	 
	 it("should be able to set his request base URL", function(){
		requestManager.setRequestBaseURL("example.com");
		expect(requestManager.getRequestBaseURL()).toEqual("example.com");
	 });
	 
	 it("should be able to set his request URL", function(){
		requestManager.setRequestURL("example.com");
		expect(requestManager.getRequestURL()).toEqual("example.com");
	 });
	 
	 it("should be able to set his request base method", function(){
		requestManager.setRequestMethod("TestMethod");
		expect(requestManager.getRequestMethod()).toEqual("TestMethod");
	 });
	 
	 it("should be able to set his request header", function(){
		requestManager.setRequestHeader("TestHeader");
		expect(requestManager.getRequestHeader()).toEqual("TestHeader");
	 });
	 
	 it("should be able to set his request data", function(){
		requestManager.setRequestData("TestData");
		expect(requestManager.getRequestData()).toEqual("TestData");
	 });
	 
	 it("should be able to make his JSONObject", function(){
        requestManager.setRequestURL("Test URL");
        requestManager.setRequestMethod("POST");
        requestManager.setRequestHeader("Test header");
        requestManager.setRequestData("Test data");
        var obj = requestManager.makeJSONObject();
        expect(obj["url"]).toEqual("Test URL");
        expect(requestManager.getRequestMethod(obj["method"])).toEqual("POST");
        expect(requestManager.getRequestHeader(obj["headers"])).toEqual("Test header");
        expect(requestManager.getRequestData(obj["data"])).toEqual("Test data");
	 });
});