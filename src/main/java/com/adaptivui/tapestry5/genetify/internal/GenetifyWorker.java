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

package com.adaptivui.tapestry5.genetify.internal;

import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Worker;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.ComputedValue;
import org.apache.tapestry5.plastic.FieldHandle;
import org.apache.tapestry5.plastic.InstanceContext;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.adaptivui.tapestry5.genetify.annotations.Genetify;
import com.adaptivui.tapestry5.genetify.config.GenetifyConstants;
import com.adaptivui.tapestry5.genetify.data.GeneType;

/**
 * This worker adds the logic to add the genetify client stack for a page or during a render phase.
 * 
 */
public class GenetifyWorker implements ComponentClassTransformWorker2
{
	private final JavaScriptSupport javascriptSupport;

	private final Worker<GeneType> addVaryScript = new Worker<GeneType>()
	{
		public void work(GeneType geneType)
		{
			if(geneType != GeneType.NONE){
				/**
				 * Call the Genetify's "vary()" function very early
				 * Not very elegant but fulfills Genetify's requirements and does the job
				 **/
				javascriptSupport.addScript(
						InitializationPriority.IMMEDIATE, 
						String.format("genetify.vary('%s');", 
						geneType.getLabel()));
			}
		}
	};

	public GenetifyWorker(JavaScriptSupport javascriptSupport)
	{
		this.javascriptSupport = javascriptSupport;
	}

	public void transform(PlasticClass componentClass, TransformationSupport support, MutableComponentModel model)
	{
		processClassAnnotationAtCleanupRenderPhase(componentClass, model);
		
		for (PlasticMethod m : componentClass.getMethodsWithAnnotation(Genetify.class))
        {
			decorateMethod(componentClass, model, m);
        }
	}

	private void decorateMethod(PlasticClass componentClass, MutableComponentModel model, PlasticMethod method)
    {
        Genetify annotation = method.getAnnotation(Genetify.class);
        
        decorateMethodForAddingScript(componentClass, model, method, annotation);
    }

	private void processClassAnnotationAtCleanupRenderPhase(PlasticClass componentClass, MutableComponentModel model)
	{
		Genetify annotation = componentClass.getAnnotation(Genetify.class);

		if (annotation != null)
		{
			/**
			 * It seems that Tapestry ignores the method annotation when you override the method
			 * So we choose to override the cleanupRender phase as it has less chance to be annotated with @Genetify  
			 * */
			PlasticMethod cleanupRender = componentClass.introduceMethod(TransformConstants.CLEANUP_RENDER_DESCRIPTION);

			decorateMethodForAddingScript(componentClass, model, cleanupRender, annotation);

			model.addRenderPhase(CleanupRender.class);
		}
	}

	private void decorateMethodForAddingScript(PlasticClass componentClass, MutableComponentModel model, PlasticMethod method,
			Genetify annotation)
	{
		addVaryScript(componentClass, model, method, annotation.vary());

	}

	private void addVaryScript(PlasticClass plasticClass, MutableComponentModel model, PlasticMethod method,
			GeneType[] geneTypes)
	{
		decorateMethodWithOperation(plasticClass, model, method, geneTypes, addVaryScript);
	}

	private void decorateMethodWithOperation(PlasticClass componentClass, MutableComponentModel model,
			PlasticMethod method, GeneType[] geneTypes, Worker<GeneType> operation)
	{
		if (geneTypes == null || geneTypes.length == 0)
			return;

		PlasticField geneTypesField = componentClass.introduceField(GeneType[].class,
				"varyTypes_" + method.getDescription().methodName);

		initializeGeneTypes(model.getBaseResource(), geneTypes, geneTypesField);

		addMethodGeneTypesOperationAdvice(method, geneTypesField.getHandle(), operation);
	}

	private void initializeGeneTypes(final Resource baseResource,
			final GeneType[] geneTypes, final PlasticField geneTypesField)
	{
		geneTypesField.injectComputed(new ComputedValue<GeneType[]>()
			{
				public GeneType[] get(InstanceContext context)
				{
					return geneTypes;
				}
			});
	}

	private void addMethodGeneTypesOperationAdvice(PlasticMethod method, final FieldHandle access,
			final Worker<GeneType> operation)
	{
		method.addAdvice(new MethodAdvice()
		{
			public void advise(MethodInvocation invocation)
			{
				javascriptSupport.importStack(GenetifyConstants.GENETIFY_STACK);
				
				GeneType[] geneTypes = (GeneType[]) access.get(invocation.getInstance());

				F.flow(geneTypes).each(operation);

				invocation.proceed();
			}
		});
	}
}

