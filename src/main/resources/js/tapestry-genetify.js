(function($){
	
	$.extend(Tapestry.Initializer, {
		recordGoal: function(spec) {
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
        },
        
        showStats: function (spec){
			$("#"+spec.elementId).bind('click', function(){
				genetify.controls.showResults();
			});
		},
		
		showControl: function (spec){
			$("#"+spec.elementId).bind('click', function(){
				genetify.controls._insertHTML('genetify_controls', genetify_controls_HTML);
				genetify.controls.showResults();
			});
		}
	});
})(jQuery);



