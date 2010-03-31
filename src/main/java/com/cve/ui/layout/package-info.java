/**
Classes for building LayoutManagers.
<p>
This package goes through lots of trouble to abortively try to solve a hard
problem that ultimately doesn't need to be solved.  It is being kept here for
instructional value, and in case it turns out it really does need to be solved.
If this problem needs to be solved, this class has some code that makes layout
managers much easier to understand and work with.
<p>
The problem is that we would like to optimally layout the components for a user
interface page like a browser does.  In other words, the priorities are:
<ol>
   <li> Stay within the width of the page.
   <li> Minimize page height.
</ol>
<p>
This is so hard, because it is a global layout problem.  It can't be adequately
addressed by a hierarchy of independent layout managers.  Each layout manager
wants to know how much space it has to work with.  This information is only
available in terms of the space needed by the other managers.
<p>
Ultimately, this problem doesn't need to be solved because we are better off with
the restriction that all pages need a top-level table layout manager, rather
than the flow layout that most HTML browsers assume.  As a thought experiment,
consider a page with a navigation bar across the top.  If the page is made wide
enough, a flow layout will eventually render the navigation bar to the left
of the contents in an undesirable manner.
<p>
*/
package com.cve.ui.layout;



