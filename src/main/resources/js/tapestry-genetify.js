/**
 * call the genetify's record function on a click event
 **/
RecordGoal = Class.create({
		
      initialize: function(spec) {
			/*
			 * The clicked object id  
			 */
			this.elementId = spec.elementId;
			/*
			 * The goal's id
			 * */
	   		this.label = spec.label;
	   		/*
	   		 * The goal's score
	   		 */
	   		this.score = spec.score;
	   		
	   		/*
	   		 * Observe click event to store the goal
	   		 */
	   		this.boundRecordGoal = this.doRecordGoal.bindAsEventListener(this);
	   		/**
	   		 * Check if it is a pagewiew goal or an link goal
	   		 * */
	   		if(spec.elementId != null && spec.elementId != undefined){
	   			Event.observe($(this.elementId), 'mousedown', this.boundRecordGoal);
	   		}else{
	   			Event.observe(window, 'load', this.boundRecordGoal);
	   		}
       },
       
        /*
        * Record the goal by calling a genetify.js function
        */ 
       doRecordGoal: function(event) {
    	   
    	   genetify.record.goal(this.label, this.score);
    	   
       }
});

Tapestry.Initializer.recordGoal = function(spec){
	new RecordGoal(spec);
};
