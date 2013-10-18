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

package com.adaptivui.tapestry5.genetify.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.adaptivui.tapestry5.genetify.annotations.Genetify;
import com.adaptivui.tapestry5.genetify.data.GeneType;

/**
 * This mixin adds the logic to record a genetify's goal on a event
 * 
 */
public class RecordGoal {
	
	/**
	 * The goal's id
	 * */
	@Parameter(required=true, defaultPrefix = BindingConstants.LITERAL)
	private String label;

	/**
	 * The goal's score
	 * */
	@Parameter(value="1", defaultPrefix = BindingConstants.LITERAL)
	private int score;
	
	@Inject
	private JavaScriptSupport javascriptSupport;
	
	@InjectContainer
	private ClientElement element;
	
	/**
	 * Add the Genetify client stack
	 * */
	@Genetify(vary=GeneType.NONE)
	@SetupRender
	void setupGenetify(){}
	
	@AfterRender
	void addScriptInitialization() {
		JSONObject spec = new JSONObject();
		spec.put("elementId", element.getClientId());
		spec.put("label", label);
		spec.put("score", score);
		javascriptSupport.require("tapestry-genetify").invoke("recordGoal").with(spec);
	}
}
