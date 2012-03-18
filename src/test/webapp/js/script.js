Highlighter = Class.create({
	  initialize: function(element) {
			this.elementId = element;
	   		Event.observe($(this.elementId), 'click', this.doHighlighter.bindAsEventListener(this));
       },
       doHighlighter: function(event) {$(this.elementId).style.color="green";}
});

Highlighter_vRed = Class.create({
		initialize: function(element) {
			this.elementId = element;
	   		Event.observe($(this.elementId), 'click', this.doHighlighter.bindAsEventListener(this));
		},
	    doHighlighter: function(event) {$(this.elementId).style.color="pink";}
});

Tapestry.Initializer.highlighter = function(element){
	new Highlighter(element);
};
