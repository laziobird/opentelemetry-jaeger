package com.observable.trace.otel.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

/**
 * 
 * @Title: BaseController.java
 * @Description: TODO
 * @author tim.jiang
 * @date 2021年12月31日
 */
public class BaseController {
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);
	@Value("${api.url}")
	public String apiUrl;
	@Autowired
	public RestTemplate httpTemplate;
	@Autowired
	public Tracer tracer;

	@Value("${trace.exporter.host}")
	public String exporterHost;
	@Value("${trace.exporter.uiPort}")
	public String exporterUiPort;
	@Value("${server.port}")
	public String serverPort;

	@Autowired
	public OpenTelemetry openTelemetry;

	public String buildTraceUrl(String traceId) {
		try {
			//睡一会吧，trace生成滞后，UI 同步成功一会再点
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("enter buildTraceUrl");
		return "<HR style=\"FILTER:progid:DXImageTransform.Microsoft.Shadow(color:#987cb9,direction:145,strength:15)\" color=#987cb9 SIZE=3> <br/>"
				+ "trace生成成功！查看完整链路图，traceId: <a href='getTrace?traceId=" + traceId + "'>" + traceId + "</a>"
				+ "<HR style=\"FILTER:progid:DXImageTransform.Microsoft.Shadow(color:#987cb9,direction:145,strength:15)\" color=#987cb9 SIZE=3> <br/>";
	}
}


