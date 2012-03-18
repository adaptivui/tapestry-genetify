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

/**
 * Add JS and CSS Genetify resources to Tapestry5 defaults.
 * 
 */
public class GenetifyStack implements JavaScriptStack {

	private final SymbolSource symbolSource;

	private final AssetSource assetSource;
	
	private final List<Asset> javascriptStack;
	
	private static final String[] CORE_JAVASCRIPT = new String[] {
				"classpath:js/genetify.js",
				"classpath:js/tapestry-genetify.js" 
			};

	private final List<StylesheetLink> cssStack;

	private static final String CORE_CSS = "classpath:css/genetify.css";


	private final boolean productionMode;
    
	public GenetifyStack(SymbolSource symbolSource, 
			AssetSource assetSource,
			@Symbol(SymbolConstants.PRODUCTION_MODE)
			boolean productionMode) {
		
		this.symbolSource = symbolSource;
		this.assetSource = assetSource;
		this.productionMode = productionMode;
		
		this.javascriptStack = convertToAssets(CORE_JAVASCRIPT);
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
