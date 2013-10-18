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

import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptModuleConfiguration;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.javascript.ModuleManager;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

import com.adaptivui.tapestry5.genetify.config.GenetifyConstants;
import com.adaptivui.tapestry5.genetify.internal.GenetifyWorker;
import com.adaptivui.tapestry5.genetify.internal.GoalWorker;

/**
 * Module for tapestry-genetify
 */
public class GenetifyModule {

	@Contribute(SymbolProvider.class)
	@FactoryDefaults
	public static void contributeSymboleProvider(MappedConfiguration<String, String> configuration)
	{
		configuration.add(GenetifyConstants.GENETIFY_TEST_MODE,"false");
	}

	@Contribute(ComponentClassTransformWorker2.class)
	@Primary
	public static void provideTransformWorkers(
			OrderedConfiguration<ComponentClassTransformWorker2> configuration)
	{
		configuration.addInstance("Genetify", GenetifyWorker.class);
		configuration.addInstance("Goal", GoalWorker.class);
	}

	/**
	 * Contribute to the JavaScriptStackSource to provide Genetify resources
	 * 
	 */
	@Contribute(JavaScriptStackSource.class)
	public static void	addGenetifyStack(
			MappedConfiguration<String, JavaScriptStack> configuration){
		configuration.addInstance(GenetifyConstants.GENETIFY_STACK, GenetifyStack.class);
	} 

	@Contribute(ComponentClassResolver.class)
	public static void addVirtualFolder(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("genetify", "com.adaptivui.tapestry5.genetify"));
	}
	
	@Contribute(ModuleManager.class)
	public static void addJSModules(
			MappedConfiguration<String, JavaScriptModuleConfiguration> configuration,
			@Symbol(GenetifyConstants.GENETIFY_TEST_MODE)
			boolean testMode,
			@Path("classpath:/META-INF/assets/genetify/js/tapestry-genetify.js")
            Resource script,
            JavaScriptSupport jsSupport
)
	{
		JavaScriptModuleConfiguration conf = new JavaScriptModuleConfiguration(script);
		conf.dependsOn("jquery", "genetify");
		if(testMode){
			conf.dependsOn("genetify-options");
		}
		configuration.add("tapestry-genetify", conf);
	}

	
}
