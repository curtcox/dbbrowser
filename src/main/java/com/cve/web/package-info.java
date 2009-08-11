/**
 * Our web application "framework".
 * It runs on top of the servlet API.
 * <p>
 * The key interface is RequestHandler, which turns PageRequestS into PageResponseS.
 * http://yuml.me/diagram/class/[PageRequest]->[RequestHandler],[RequestHandler]->[PageResponse]
 * <p>
 * The PageResponse will either contain a Model or a redirect.
 * ModelS are appropriately rendered by ModelRenderers.
 * http://yuml.me/diagram/class/[PageResponse]->[Model%20Renderer],[Model%20Renderer]->[HTML],[Model%20Renderer]->[JavaFX]
 * <p>
 * Adding a page to the application requires adding three classes.
 * <ol>
 * <li> Add the handler that will service the requests for the page.
 * <li> Add the model that the handler will generate.
 * <li> Add the renderer for the model.  Actually, one of these is needed for
 * each type of target being rendered to.
 * </ol>
 */
package com.cve.web;

