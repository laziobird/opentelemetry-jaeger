package com.observable.trace.otel.controller;

import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapSetter;

/**
 * 
 * @Title: ContextController.java
 * @Description: TODO
 * @author tim.jiang
 * @date 2021年12月31日
 */
@RestController
public class ContextController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ContextController.class);

	@GetMapping("/contextR")
	@ResponseBody
	public String contextR() {
		TextMapSetter<HttpURLConnection> setter = new TextMapSetter<HttpURLConnection>() {
			@Override
			public void set(HttpURLConnection carrier, String key, String value) {
				// Insert the context as Header
				carrier.setRequestProperty(key, value);
			}
		};
		Span spanCur = Span.current();
		SpanContext spanContext = spanCur.getSpanContext();
		Span outGoing = tracer.spanBuilder("/resource").setSpanKind(SpanKind.CLIENT).startSpan();
		try {
			URL url = new URL("http://127.0.0.1:8080/resource");
			// Semantic Convention.
			// (Observe that to set these, Span does not *need* to be the current instance.)
			HttpURLConnection transportLayer = (HttpURLConnection) url.openConnection();
			outGoing.setAttribute("http.method", "GET");
			outGoing.setAttribute("http.url", url.toString());
			// Inject the request with the *current* Context, which contains our current
			// Span.
			openTelemetry.getPropagators().getTextMapPropagator().inject(Context.current(), transportLayer, setter);
			// Make outgoing call
		} catch (Exception e) {
		} finally {
			outGoing.end();
		}
		return buildTraceUrl(spanContext.getTraceId());
	}

}
