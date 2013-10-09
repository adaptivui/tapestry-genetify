Tapestry Genetify
=================

Tapestry-genetify is intented to be a tapestry integration for [Genetify](https://github.com/gregdingle/genetify/wiki/) (A/B testing and webpages optimization).

[Genetify](https://github.com/gregdingle/genetify/wiki/) is a powerful opensource website testing and optimization tool created by [Greg Dingle](https://github.com/gregdingle).
It makes your website adapt and evolve toward an optimum in an autonomous way using some kind of [multi-armed bandit algorithm](http://en.wikipedia.org/wiki/Multi-armed_bandit) and can be compared to [Google Website Optimizer](http://www.google.com/websiteoptimizer/).

Example
=======

Assuming you provide [genetified resources](https://github.com/gregdingle/genetify/wiki/Usage) for your website.

For instance, a simple genetified css that set a color to an element identified by "navbar" (note the suffix '_vX')

    #navbar { color: red; }
    #navbar_vA { color: green; }
    #navbar_vB { color: blue; }
    
and/or a simple genetified markup (note the _class_ attribute's value with the 'v' separator)
    
    <div class="sentence">One way of saying something</div>           
    <div class="sentence v anotherway">Another way of saying something</div>

#### How to genetify ####

The module provides a way to add the genetify stack (js and css) and to evolve the webpage design.

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

#### How to record a Goal ####

The module provides also a way to record a goal on a page view or on an event (used to make the pages evolve toward an optimum)

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

Installation
============

[Available](http://search.maven.org/#search|ga|1|tapestry-genetify) from the maven central repository. Just add the following dependency for Tapestry 5.3 :

    <dependency>
       <groupId>com.adaptivui</groupId>
       <artifactId>tapestry-genetify</artifactId>
       <version>1.0.0</version>
    </dependency>

Licence
=======

This project is distributed under Apache 2 License. See LICENSE.txt for more information.

Demo
====

See the [tapestry-genetify-demo](https://github.com/adaptivui/tapestry-genetify-demo/blob/master/README.md) project and its live [demo](http://www.adaptivui.com/tapestry-genetify-demo)
