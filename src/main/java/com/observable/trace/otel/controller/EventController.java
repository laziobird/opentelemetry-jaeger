package com.observable.trace.otel.controller;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @Title: EventController.java
 * @Description: TODO
 * @author tim.jiang
 * @date 2021年12月31日
 */
@RestController
public class EventController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);

	@GetMapping("/event")
	public String event() {
		Span span = Span.current();
		span.updateName("创建eventDemo");

		// 手动更新Event持续时间
		span.addEvent("time.update", System.currentTimeMillis() + 2000, TimeUnit.MILLISECONDS);
		// 给Event添加相关信息
		Attributes appInfo = Attributes.of(AttributeKey.stringKey("app.id"), "123456",
				AttributeKey.stringKey("app.name"), "应用程序demo");
		span.addEvent("appinfo.query", appInfo);
		logger.info("this is a event");
		return buildTraceUrl(span.getSpanContext().getTraceId());
	}

}
