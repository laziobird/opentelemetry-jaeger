package com.observable.trace.otel.controller;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liurui
 * @date 2021/12/30 14:45
 */
@RestController
public class LinkController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

@GetMapping("/createSpanAndLink")
public String createSpanAndLink() {
    String spanName = "手工创建spanOne";
    //创建一个span，然后创建三个child span，最后关联span
    Span spanOne = tracer.spanBuilder(spanName)             
            .startSpan();
    Span childSpan = tracer.spanBuilder("childOne").addLink(spanOne.getSpanContext()).startSpan();
    Span childSpan2 = tracer.spanBuilder("childTwo").addLink(spanOne.getSpanContext()).startSpan();
    Span childSpan3 = tracer.spanBuilder("childThree").addLink(spanOne.getSpanContext()).startSpan();
    //创建一个span，关联childSpan3,作为它的childspan
    Span childSpan3Child = tracer.spanBuilder("childThree-Child").addLink(childSpan3.getSpanContext()).startSpan();
      
        
        logger.info("create span:{}",spanName);
        try {
            return buildTraceUrl(spanOne.getSpanContext().getTraceId());
        } finally {
        	spanOne.end();
        	childSpan.end();
        	childSpan2.end();
        	childSpan3.end();
        	childSpan3Child.end();
        }
    }
    
    
    //创建child span
    private SpanContext childSpan(int i) {
        String spanName = "childSpan"+i;
        logger.info("create span:{}",spanName);
        Span span = tracer.spanBuilder(spanName).startSpan();
        try {
            span.addEvent("this is "+spanName);
            return span.getSpanContext();
        } finally {
            span.end();
        }
    }

}
