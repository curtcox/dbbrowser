package com.cve.web.core;

/**
 * A model that is also a ModelRenderer.
 * <p>
 * If a SelfRenderingModel is used, then it is the responsibility of the container
 * to recognize that fact and act accordingly.  Generally, the container will
 * have a single ModelHtmlRenderer for rendering all models.  Of course this
 * single renderer will usually just be the top of some sort of composite tree.
 * <p>
 * By declaring itself self rendering, a model is saying that it can render itself
 * and the normal rendering process should be bypassed.
 * @author curt
 */
public interface SelfRenderingModel extends Model, ModelHtmlRenderer {

}
