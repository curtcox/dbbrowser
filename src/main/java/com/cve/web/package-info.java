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
 */
package com.cve.web;

