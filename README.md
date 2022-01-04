<a name="wSh88"></a>
# opentelemetry-jaeger
OpenTelemetry+Jaeger的分布式链路追踪演示Demo

- Load Balance ：Nginx
- 前端：Java SpringBoot Web + OpenTelemetry Tracing +  Jaeger Exporter
- 后端：Jaeger UI

深入了解可观测体系下Traces原理<br />演示地址 [http://106.14.209.9/](http://106.14.209.9/)
<a name="Azt51"></a>
#### 架构图
![image.png](https://cdn.nlark.com/yuque/0/2022/png/25529450/1641275525932-5bfffe21-d563-4576-a570-775a0883ad27.png#clientId=u2d91e3eb-f650-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=411&id=u431635f7&margin=%5Bobject%20Object%5D&name=image.png&originHeight=546&originWidth=960&originalType=binary&ratio=1&rotation=0&showTitle=false&size=259252&status=done&style=none&taskId=ud0e6f27d-1853-4f67-ba14-bcc120aff61&title=&width=723)
<a name="hQyuS"></a>
#### 框架列表
| **Library/Framework** | **Versions** | **备注** |
| --- | --- | --- |
| opentelemetry-api | 1.9.1 | ​<br /> |
| opentelemetry-sdk | 1.9.1 | ​<br /> |
| opentelemetry-exporter-jaeger | 1.9.1 |  |
| opentelemetry-semconv | 1.9.0-alpha | 目前只有alpha版本 |
| spring-boot | 2.6.2 | JDK 1.8+ |
| nginx | 1.16.1 |  |

<a name="igqfL"></a>
#### 资料引用
[https://github.com/open-telemetry/opentelemetry-java-instrumentation](https://github.com/open-telemetry/opentelemetry-java-instrumentation)<br />[https://www.jaegertracing.io/docs/1.29/getting-started/](https://www.jaegertracing.io/docs/1.29/getting-started/)<br />[https://opentelemetry.io/docs/](https://opentelemetry.io/docs/)
