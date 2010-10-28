package org.resthub.tapestry5.components.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library="zoneUpdater.js" )
public class ZoneUpdater {

  @Inject
  private ComponentResources resources;

  @Environmental
  private JavaScriptSupport jsSupport;

  /**
   * The event to listen for on the client. If not specified, zone update can only be triggered manually through calling updateZone on the JS object.
   */
  @Parameter(defaultPrefix = BindingConstants.LITERAL)
  private String clientEvent;

  /**
   * The event to listen for in your component class
   */
  @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
  private String event;

  @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "default")
  private String prefix;

  /**
   * The element we attach ourselves to
   */
  @InjectContainer
  private ClientElement element;

  @Parameter
  private Object[] context;

  /**
   * The zone to be updated by us.
   */
  @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
  private String zone;

  void afterRender() {
    String url = resources.createEventLink(event, context).toAbsoluteURI();
    String elementId = element.getClientId();
    JSONObject spec = new JSONObject();
    spec.put("url", url);
    spec.put("elementId", elementId);
    spec.put("event", clientEvent);
    spec.put("zone", zone);

    jsSupport.addScript("%sZoneUpdater = new ZoneUpdater(%s)", prefix, spec.toString());
  }
}
