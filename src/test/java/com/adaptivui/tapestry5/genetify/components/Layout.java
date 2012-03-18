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

package com.adaptivui.tapestry5.genetify.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Layout component for test application pages.
 */
@Import(
			library = {"context:js/script.js"},
			stylesheet = {"context:css/layout.css"}
		)
public class Layout
{
    @Inject
    private JavaScriptSupport javascriptSupport; 
    
    @SuppressWarnings("unused")
	@Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;
    
    @AfterRender
    void addHighlighterFunction(){
    	javascriptSupport.addScript(
    			InitializationPriority.NORMAL, 
    			String.format("new Highlighter('%s')", "jsvariant"));
    	
    }

}
