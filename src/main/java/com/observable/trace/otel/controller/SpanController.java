package com.observable.trace.otel.controller;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.observable.trace.otel.util.ConstantsUtils;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

/**
 * 
 * @Title: SpanController.java
 * @Description: TODO
 * @author tim.jiang
 * @date 2021年12月31日
 */
@Controller
public class SpanController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SpanController.class);	
	/**
	 * 自定义span
	 * 
	 * @return
	 */

	@GetMapping("/customSpan")
	@ResponseBody
	public String customSpan() {
		// 创建一个子span
		Span span = tracer.spanBuilder("parent").startSpan();
		span.setAttribute("username", "father");
		try (Scope scope = span.makeCurrent()) {
			sonSpan();
		} catch (Throwable t) {
			span.setStatus(StatusCode.ERROR, "Change it to your error message");
		} finally {
			// closing the scope does not end the span, this has to be done manually
			span.end();
		}
		logger.info("traceId:{},spanId:{}", span.getSpanContext().getTraceId(), span.getSpanContext().getSpanId());
		return "success";
	}

	/**
	 * create a child Span
	 * 
	 * @return
	 */
	public String sonSpan() {
		Span sonSpan = tracer.spanBuilder("child").setParent(Context.current().with(Span.current())).startSpan();
		try {
			sonSpan.setAttribute("username", "son");
			return "";
		} finally {
			sonSpan.end();
		}
	}

	@GetMapping("/")
	public String index() {
		logger.info("enter index");
		return "index";
	}

	@GetMapping("/loadBalancer")
	@ResponseBody
	public String loadBalancer(String tag) {
		logger.info("enter loadBalancer");
		Span span = Span.current();
		// Baggage: save a key
		Baggage.current().toBuilder().put("baggage.key", "蒋志伟").build().makeCurrent();
		// logback 里面绑定user-id日志中
		String userId = "user-" + tag;
		MDC.put(ConstantsUtils.MDC_USER_ID, userId);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add tag into span
		span.setAttribute("username", tag);
		logger.info("this is tag username" + tag + "");
		httpTemplate.getForEntity(apiUrl + "/resource", String.class).getBody();
		httpTemplate.getForEntity(apiUrl + "/auth", String.class).getBody();
		return httpTemplate.getForEntity(apiUrl + "/billing?tag=" + tag, String.class).getBody();
	}

	@GetMapping("/resource")
	@ResponseBody
	public String resource() {
		String baggage = Baggage.current().getEntryValue("app.username");
		Span spanCur = Span.current();
		spanCur.setAttribute("app.username", "baggage_" + baggage);
		logger.info("baggage---------------------->");
		logger.info("this is resource baggage " + baggage);

		// Span.current().updateName("资源服务");
		return "this is resource baggage " + baggage;
	}

	/**
	 * 身份认证服务demo
	 * 
	 * @return
	 */
	@GetMapping("/auth")
	@ResponseBody
	public String auth() {
		logger.info("this is auth");
		return "this is auth";
	}

	/**
	 * a billing API demo
	 * 
	 * @param tag
	 * @return
	 */
	@GetMapping("/billing")
	@ResponseBody
	public String billing(String tag) {
		logger.info("enter billing");
		Span span = Span.current();
		if (Optional.ofNullable(tag).get().equalsIgnoreCase("error")) {
			// if tag value equals "error" , call span event
			span.addEvent("billing error start");
			try {
				span.setAttribute("java.error", "System.out.println(1 / 0)");
				System.out.println(1 / 0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			span.addEvent("billing error end",System.currentTimeMillis()+1000, TimeUnit.MILLISECONDS);
		}
		SpanContext spanContext = Span.current().getSpanContext();
		return buildTraceUrl(spanContext.getTraceId());
	}

	@GetMapping("/getTrace")
	public String getTrace(String traceId) {
		return "redirect:http://" + exporterHost + ":" + exporterUiPort + "/trace/" + traceId;
	}

}
