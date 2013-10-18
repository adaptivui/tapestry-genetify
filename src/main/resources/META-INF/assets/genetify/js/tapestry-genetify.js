// Copyright 2013 Nourredine Khadri
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

define(["jquery", "t5/core/console"], function($, console) {

		vary = function (spec){
			if (console.debugEnabled) {
				console.debug('tapestry-genetify.js gene type : ' + JSON.stringify(spec.type));
			}
			genetify.vary(spec.type);
		};
		
		recordGoal = function(spec) {
			if (console.debugEnabled) {
				console.debug('tapestry-genetify.js record goal (label : ' + JSON.stringify(spec.label)+ ', score : '+JSON.stringify(spec.score)+')');
			}
			var elementId = spec.elementId;
			if(elementId != null && elementId != undefined){
				$("#"+elementId).bind('click', function(){
					genetify.record.goal(spec.label, spec.score);
				});
			}else{
	   			$(window).bind('load', function(){
					genetify.record.goal(spec.label, spec.score);
				});
	   		}
        };
        
        showStats = function (spec){
        	if (console.debugEnabled) {
				console.debug('tapestry-genetify.js showStats');
			}
			
			$("#"+spec.elementId).bind('click', function(){
				genetify.controls.showResults();
			});
		};
		
		showControl = function (spec){
			if (console.debugEnabled) {
				console.debug('tapestry-genetify.js showControl');
			}
			$("#"+spec.elementId).bind('click', function(){
				genetify.controls._insertHTML('genetify_controls', genetify_controls_HTML);
				genetify.controls.showResults();
			});
		};
		
		return {
			vary : vary,
			recordGoal : recordGoal,
			showStats : showStats,
			showControl : showControl
		};
});


