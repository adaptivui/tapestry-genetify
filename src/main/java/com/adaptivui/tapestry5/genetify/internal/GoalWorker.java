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
import org.apache.tapestry5.json.JSONObject;
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
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.adaptivui.tapestry5.genetify.annotations.Goal;
import com.adaptivui.tapestry5.genetify.config.GenetifyConstants;

/**
 * This worker adds the logic to add the genetify client stack for the page if not already done.
 * It also calls the genetify's record goal function on page's onload event
 * 
 */
public class GoalWorker implements ComponentClassTransformWorker2
{
	private final JavaScriptSupport javascriptSupport;

	private final Worker<com.adaptivui.tapestry5.genetify.data.Goal> addGoalScript = new Worker<com.adaptivui.tapestry5.genetify.data.Goal>()
	{
		public void work(com.adaptivui.tapestry5.genetify.data.Goal goal)
		{
			/**
			 * Call the Genetify's "record.goal()" function
			 **/
			if(goal.getScore() != 0){
				JSONObject spec = new JSONObject();
				spec.put("label", goal.getLabel());
				spec.put("score", goal.getScore());
				javascriptSupport.require("genetify/tapestry-genetify").invoke("recordGoal").with(spec);
			}
		}
	};

	public GoalWorker(JavaScriptSupport javascriptSupport)
	{
		this.javascriptSupport = javascriptSupport;
	}

	public void transform(PlasticClass componentClass, TransformationSupport support, MutableComponentModel model)
	{
		processClassAnnotationAtCleanupRenderPhase(componentClass, model);
	}

	private void processClassAnnotationAtCleanupRenderPhase(PlasticClass componentClass, MutableComponentModel model)
	{
		Goal annotation = componentClass.getAnnotation(Goal.class);

		if (annotation != null)
		{
			/**
			 * It seems that Tapestry ignores the annotation method when it is overridden
			 * So we choose the cleanupRender phase as it is the method that has less chance to be annotated with @Goal  
			 * */
			PlasticMethod cleanupRender = componentClass.introduceMethod(TransformConstants.CLEANUP_RENDER_DESCRIPTION);

			decorateMethodForAddingScript(componentClass, model, cleanupRender, annotation);

			model.addRenderPhase(CleanupRender.class);
		}
	}

	private void decorateMethodForAddingScript(PlasticClass componentClass, MutableComponentModel model, PlasticMethod method,
			Goal annotation)
	{
		addGoalScript(componentClass, 
						model, 
						method,
						new com.adaptivui.tapestry5.genetify.data.Goal(annotation.label(), annotation.score()));
	}

	private void addGoalScript(PlasticClass plasticClass, MutableComponentModel model, PlasticMethod method,
			com.adaptivui.tapestry5.genetify.data.Goal goal)
	{
		decorateMethodWithOperation(plasticClass, model, method, goal, addGoalScript);
	}

	private void decorateMethodWithOperation(PlasticClass componentClass, MutableComponentModel model,
			PlasticMethod method, com.adaptivui.tapestry5.genetify.data.Goal goal, Worker<com.adaptivui.tapestry5.genetify.data.Goal> operation)
	{
		if (goal == null)
			return;

		PlasticField goalField = componentClass.introduceField(com.adaptivui.tapestry5.genetify.data.Goal.class,
				"goal_" + method.getDescription().methodName);

		initializeGoal(method, goal, goalField);

		addMethodGoalOperationAdvice(method, goalField.getHandle(), operation);
	}

	private void initializeGoal(final PlasticMethod method,
			final com.adaptivui.tapestry5.genetify.data.Goal goal, final PlasticField goalField)
	{
		goalField.injectComputed(new ComputedValue<com.adaptivui.tapestry5.genetify.data.Goal>()
		{
			public com.adaptivui.tapestry5.genetify.data.Goal get(InstanceContext context)
			{
				if (goal.getLabel() == null || goal.getLabel().length() == 0){
					goal.setLabel(method.getDescription().methodName);
				}
				return goal;
			}
		});
	}

	private void addMethodGoalOperationAdvice(PlasticMethod method, final FieldHandle access,
			final Worker<com.adaptivui.tapestry5.genetify.data.Goal> operation)
	{
		method.addAdvice(new MethodAdvice()
		{
			public void advise(MethodInvocation invocation)
			{
				javascriptSupport.importStack(GenetifyConstants.GENETIFY_STACK);
				
				com.adaptivui.tapestry5.genetify.data.Goal goal = (com.adaptivui.tapestry5.genetify.data.Goal) access.get(invocation.getInstance());

				F.flow(goal).each(operation);

				invocation.proceed();
			}
		});
	}
}

