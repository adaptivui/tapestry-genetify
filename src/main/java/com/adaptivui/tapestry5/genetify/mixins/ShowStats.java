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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.adaptivui.tapestry5.genetify.annotations.Genetify;
import com.adaptivui.tapestry5.genetify.data.GeneType;

/**
 * This mixin displays Genetify's stats on a click
 * 
 */
public class ShowStats {
	/**
	 * The tapestry-genetify js script to be loaded after the genetify.js
	 * */
	@Inject
	@Path(value="classpath:js/tapestry-genetify.js")
	private Asset script;
	
	@Inject
	private JavaScriptSupport javascriptSupport;
	
	@InjectContainer
	private ClientElement element;
	
	/**
	 * Add the Genetify client stack
	 * */
	@Genetify(vary=GeneType.NONE)
	@SetupRender
	void addLibraries(){
		javascriptSupport.importJavaScriptLibrary(script);
	}
	
	@AfterRender
	void addScriptInitialization() {
		JSONObject spec = new JSONObject();
		spec.put("elementId", element.getClientId());
		javascriptSupport.addInitializerCall("showStats", spec);
	}

}
