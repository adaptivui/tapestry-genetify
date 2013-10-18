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

package com.adaptivui.tapestry5.genetify.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import com.adaptivui.tapestry5.genetify.config.GenetifyConstants;

/**
 * Add JS and CSS Genetify resources to Tapestry5 defaults.
 * 
 */
public class GenetifyStack implements JavaScriptStack {

	private final SymbolSource symbolSource;

	private final AssetSource assetSource;
	
	private final List<Asset> javascriptStack;
    
	private List<String> CORE_JAVASCRIPT = null;

	private final List<StylesheetLink> cssStack;

	private static final String CORE_CSS = "classpath:/META-INF/assets/genetify/css/genetify.css";


	private final boolean productionMode;
    
	public GenetifyStack(SymbolSource symbolSource, 
			AssetSource assetSource,
			final @Symbol(SymbolConstants.PRODUCTION_MODE)
			boolean productionMode,
			final @Symbol(GenetifyConstants.GENETIFY_TEST_MODE) 
			Boolean genetifyTestMode) {
		
		this.symbolSource = symbolSource;
		this.assetSource = assetSource;
		this.productionMode = productionMode;
		
		CORE_JAVASCRIPT = new ArrayList<String>(){
			private static final long serialVersionUID = 8264935457409477130L;
			{
				 if(genetifyTestMode) add("classpath:META-INF/assets/genetify/js/genetify-options.js");
                 add("classpath:META-INF/assets/genetify/js/genetify.js");
			}
		};
		this.javascriptStack = convertToAssets(CORE_JAVASCRIPT.toArray(new String[CORE_JAVASCRIPT.size()]));
        this.cssStack = new ArrayList<StylesheetLink>();
		this.cssStack.add(new StylesheetLink(expand(CORE_CSS, null)));
	}
	
	public List<Asset> getJavaScriptLibraries() {
		return this.javascriptStack;
	}

	public List<StylesheetLink> getStylesheets() {
		return this.cssStack;
	}

	public String getInitialization() {
		return productionMode ? null : "Tapestry.DEBUG_ENABLED = true;";
	}
	
	public List<String> getStacks() {
		return Collections.emptyList();
	}
	
	
	public List<String> getModules() {
		return Arrays.asList( new String[]{"genetify/tapestry-genetify"});
	}
	
	private Asset expand(String path, Locale locale) {
		String expanded = symbolSource.expandSymbols(path);
		if (expanded != null && !"".equals(expanded)) {
			return assetSource.getAsset(null, expanded, locale);
		} else {
			return null;
		}
	}

	private List<Asset> convertToAssets(String[] paths) {
        List<Asset> assets = CollectionFactory.newList();
        for (String path : paths) {
                assets.add(expand(path, null));
        }
        return Collections.unmodifiableList(assets);
	}
}
