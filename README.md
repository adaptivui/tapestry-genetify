Tapestry Genetify
=================

Tapestry-genetify is intented to be a lightweight and unobtrusive integration of the [genetify](https://github.com/gregdingle/genetify/wiki/) tool for Tapestry 5.

[Genetify](https://github.com/gregdingle/genetify/wiki/) is a powerful opensource website testing and optimization tool created by [Greg Dingle](https://github.com/gregdingle).
It makes your website adapt and evolve toward an optimum in an autonomous way using a genetic algorithm and can be compared to [Google Website Optimizer](http://www.google.com/websiteoptimizer/).

Example
=======

Assuming you provide [genetified resources](https://github.com/gregdingle/genetify/wiki/Usage) for your website.

For instance, a simple genetified css that set a color to an element identified by "navbar" (note the suffix "_vX")

    #navbar { color: red; }
    #navbar_vA { color: green; }
    #navbar_vB { color: blue; }
    
and/or a simple genetified markup
    
    <div class="sentence">One way of saying something</div>           
    <div class="sentence v anotherway">Another way of saying something</div>

#### Genetify ####

The module provides a way to add the genetify stack (js and css) and to evolves the webpage design.

###### Genetify annotation  ######

    package com.example.testapp.pages;
    ...
    @Genetify
    public class Index {
    	
    }

The _@Genetify_ annotation includes the genetify stack and evolves the webpage design for all genetified resources (css, js and markup).

###### Vary attribute  ######
    
    package com.example.testapp.pages;
    ...
    @Genetify(vary=GeneType.CSSRULES)
    public class Index {
    
        @SetupRender
        @Genetify(vary=GeneType.JAVASCRIPT)
        void init(){		
        }
    
        @AfterRender
        @Genetify(vary=GeneType.ELEMENTS)
        void doEnd(){
        }
    }

The _vary_ attribute evolves a given resource type (css, js or markup) during a given render phase.

#### Goal record ####

The module provides also a way to record a goal on a page view or on an event 

###### Goal annotation  ######

    package com.example.testapp.pages;
    ...
    @Goal(label="SubscribeView", score=1)
    public class Subscribe {
    ...
    }

In this sample, the _@Goal_ annotation records a goal on a page view with label "SubscribeView" and a score of 1

###### RecordGoal Mixins ######

To record a goal on an event with a label and a score, use the _genetify/recordGoal_ mixins :

in SomePage.tml

    <html t:type="layout" title="Some Page"  
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_3.xsd">
        ...
        <t:actionlink t:id="addToBasket" 
                t:mixins="genetify/RecordGoal" 
                t:label="addedToBasket" 
                t:score="3">
            Add to basket
        </t:actionlink>
        ...
    </html>

Here, the "addedToBasket" goal will be recorded with a score of 3 on the click event.

Licence
=======

This project is distributed under Apache 2 License. See LICENSE.txt for more information.