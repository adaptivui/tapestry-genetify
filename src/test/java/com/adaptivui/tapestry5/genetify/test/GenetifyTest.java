//
// Copyright 2012 Nourredine Khadri
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.adaptivui.tapestry5.genetify.test;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.test.TestableResponse;
import org.apache.tapestry5.test.PageTester;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.adaptivui.tapestry5.genetify.data.GeneType;
import com.adaptivui.tapestry5.genetify.services.AppModule;

/**
 * Test the @Genetify and @Goal annotations with their attributes
 * Test the "RecordGoal" mixins
 * Check that the appropriate stack is imported. 
 * Check also that the application js scripts do not suffer from the Genetify resources import
 * */
public class GenetifyTest {
	
	private PageTester tester;

	public GenetifyTest() {
		tester = new PageTester("com.adaptivui.tapestry5.genetify", "app",
				"src/test/webapp", AppModule.class);
	}

	@AfterClass
	public void shutdown() {
		tester.shutdown();
	}
	
	/**
	 * Check that the "Genetify" annotation add effectively the appropriate stack (js and css)
	 * Check that the function "generify.vary()" is called without parameter
	 * */
	@Test
	public void checkGenetifyAnnotation() {
		Document result = checkJsAndCssStack("Index");
		String markup  = result.getRootElement().getChildMarkup();
		System.out.println(markup);
		checkVaryFunctionCall(markup, GeneType.ALL.getLabel());
		
	}
	
	/**
	 * Check that the function "generify.vary()" is called with the appropriate parameters
	 * */
	@Test
	public void checkVaryFunctionCall() {
		Document result = tester.renderPage("GenetifyPage");
		String markup  = result.getRootElement().getChildMarkup();
		checkVaryFunctionCall(markup, GeneType.CSSRULES.getLabel());
		checkVaryFunctionCall(markup, GeneType.JAVASCRIPT.getLabel());
		checkVaryFunctionCall(markup, GeneType.ELEMENTS.getLabel());
	}
	
	/**
	 * Check that the "Goal" annotation add effectively the appropriate stack
	 * Check that the function "genetify.record.goal()" is called with the appropriate parameters
	 * */
	@Test
	public void checkGoalAnnotation(){
		Document result = checkJsAndCssStack("GoalPage");
		String markup  = result.getRootElement().getChildMarkup();
		checkGoalFunctionCall(markup, null, "goalpageview", "2");
		
	}
	
	/**
	 * Check that the function "genetify.record.goal()" is called with the appropriate parameters
	 * Check that the actionlink works correctly
	 * */
	@Test
	public void checkRecordGoalMixins(){
		Document result = tester.renderPage("RecordGoal");
		String markup  = result.getRootElement().getChildMarkup();
		checkGoalFunctionCall(markup, "incrementAjax", "incrementAjax", "3");
		// Test the ajax actionlink
		TestableResponse response = tester.clickLinkAndReturnResponse(result.getElementById("incrementAjax"));
		assert response.getRenderedDocument().getRootElement().getChildMarkup().contains(
				"Increment (via Ajax) clicked") : "Zone has not been refreshed as expected";
	}
	
	// Check that js and css stack is correctly imported
	private Document checkJsAndCssStack(String pageName) {
		Document result = tester.renderPage(pageName);
		assert result.getRootElement() != null : "The document is empty";
		assert result.getRootElement().getChildMarkup().contains("/js/genetify.js") : "'genetify.js' missing";
		assert result.getRootElement().getChildMarkup().contains("/css/genetify.css") : "'genetify.css' missing";
		return result;
	}
	
	// Check that the genetify.vary() function is called with an expected parameter
	private void checkVaryFunctionCall(String markup, String expectedType){
		String token =	"    \"genetify/tapestry-genetify:vary\",\n"+
						"    {\n"+
						"      \"type\" : \""+expectedType+"\"\n"+
						"    }\n";
		assert markup.contains(token) : "The following script for vary function is missing : "+token;
	}
	
	// Check that the recordGoal initialization function is called with some expected parameters
	private void checkGoalFunctionCall(String markup, String elementId, String goal, String score){
		String token =	"    \"genetify/tapestry-genetify:recordGoal\",\n"+
						"    {\n"+
						(elementId!=null ? "      \"elementId\" : \""+elementId+"\",\n":"")+
						"      \"score\" : "+score+",\n"+
						"      \"label\" : \""+goal+"\"\n"+
						"    }\n"; 
		assert markup.contains(token) : "The following script for recordGoal function is missing : "+token;
	}
}
