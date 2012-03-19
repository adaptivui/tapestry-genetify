Tapestry Genetify
=================

Tapestry-genetify is intented to be a lightweight and unobtrusive integration of the [genetify](https://github.com/gregdingle/genetify/wiki/) tool for Tapestry 5.

[Genetify](https://github.com/gregdingle/genetify/wiki/) is a powerful opensource website testing and optimization tool created by Greg Dingle.
It makes your website adapt and evolve in an autonomous way and can be compared to [Google Website Optimizer](http://www.google.com/websiteoptimizer/).

Example
=======

Assuming you provide [genetified resources](https://github.com/gregdingle/genetify/wiki/Usage) for your website.

    #navbar { color: red; }
    #navbar_vA { color: green; }

For instance, a simple genetified css that set a color to an element identified by "navbar" (note the suffix "_vX")

#### @Genetify annotation ####

    package com.example.testapp.pages;
    ...
    @Genetify
    public class Index {
    	
    }

Includes the genetify stack (js and css) and evolves the webpage design for all genetified resources (css, js and markup).

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

This includes the genetify stack and evolves a specified resource type (css, js or markup) during the specified render phase.

#### @Goal annotation ####

    package com.example.testapp.pages;
    ...
    @Goal(label="SubscribeView", score=1)
    public class Subscribe {
    ...
    }

Records a goal on a page view with label "SubscribeView" and score "1"

#### RecordGoal Mixins ####

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

Uses a mixins to record the goal with label "addedToBasket" and score "3" on the link click.

Licence
=======

This project is distributed under Apache 2 License. See LICENSE.txt for more information.