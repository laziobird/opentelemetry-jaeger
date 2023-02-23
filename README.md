<a name="wSh88"></a>
# opentelemetry-jaeger-prometheus
<a name="XGdFY"></a>
## Introduction
### 原理文档
- [链路数据如何传播](./docs/链路数据如何传播%20.md)
- [Opentelemetry自动构建Trace](./docs/自动构建.md)
### OpenTelemetry+Jaeger+Prometheus的分布式链路追踪演示案例
- Load Balance：Nginx
- Java：SpringBoot 
- Agent：OpenTelemetry +  Jaeger Trace Exporter  + Prometheus Metric Exporter
- 可视化：Jaeger UI 、Prometheus UI
深入了解可观测体系下Traces概念和运行原理<br />
  - ![Alt Text](./assets/introduce.gif)
 
## Architecture 
![image.png](./docs/arc.png#clientId=u2d91e3eb-f650-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=411&id=u431635f7&margin=%5Bobject%20Object%5D&name=image.png&originHeight=546&originWidth=960&originalType=binary&ratio=1&rotation=0&showTitle=false&size=259252&status=done&style=none&taskId=ud0e6f27d-1853-4f67-ba14-bcc120aff61&title=&width=723)
<a name="cqdhz"></a>
## Tracing 效果图
 ![image.png](./docs/trace.png#clientId=u58d1f88b-2c01-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=310&id=ucb9dec40&margin=%5Bobject%20Object%5D&name=image.png&originHeight=609&originWidth=1439&originalType=binary&ratio=1&rotation=0&showTitle=false&size=356007&status=done&style=none&taskId=uf76b343d-c5d3-4a5f-bb95-3abc4cab5a3&title=&width=732)
## Prometheus 采集Metric 
 ![prometheus.jpg](./assets/prometheus.jpg)
## 框架列表
| **Library/Framework** | **Versions** | **备注** |
| --- | --- | --- |
| opentelemetry-api | 1.9.1 | ​<br /> |
| opentelemetry-java-instrumentation | 1.9.1 | Java探针 |
| opentelemetry-sdk | 1.9.1 | ​<br /> |
| opentelemetry-exporter-jaeger | 1.9.1 |  |
| opentelemetry-semconv | 1.9.0-alpha | 目前只有alpha版本 |
| jaegertracing | all-in-one:1.29 | docker镜像 |
| spring-boot | 2.6.2 | JDK 1.8+ |
| nginx | 1.16.1 |  |
<a name="tfZIA"></a>
## Compiling project
<a name="KDdV7"></a>
### Maven 编译Java 程序
```shell
mvn package -DskipTests=true
```
  - target下把对应jar包重命名`otel.jar`，放到`Dockerfile`文件同级目录
### Linux 
  - Docker 环境，三个服务部署在一台服务器上，网络Host模式
```shell
## down
docker-compose -f /path/docker-compose.yml  down
## start
docker-compose -f /path/docker-compose.yml  up -d
```
<a name="Je6W1"></a>
### Mac
Mac 用Docker Host 模式很坑，建议网络 bridge模式
```yaml
networks:
  jaeger:
  
services: 
    jaeger:
        image: jaegertracing/all-in-one:1.29
        networks:
            - jaeger
```

## 非容器独立部署模式 
### java 程序
其中 opentelemetry-javaagent.jar 是探针 otel.jar 是我们编译 jar 包
```yaml
java -javaagent:/path/opentelemetry-javaagent.jar  -Dotel.resource.attributes=service.name=trace-demo -Dotel.traces.exporter=jaeger  -jar target/otel.jar
```
### Docker单独启 Jaeger
```yaml
docker run -d --name jaeger   -p 5775:5775/udp   -p 6831:6831/udp  -p 6832:6832/udp  -p 5778:5778   -p 16686:16686   -p 14268:14268   -p 14250:14250   -p 9411:9411   jaegertracing/all-in-one:1.27
```
### Docker单独启 Prometheus
```yaml
docker run  -d \
  -p 9090:9090 \
  prom/prometheus
```
<a name="T6DHp"></a>
## Documentation 
[https://github.com/open-telemetry/opentelemetry-java-instrumentation](https://github.com/open-telemetry/opentelemetry-java-instrumentation)<br />[https://www.jaegertracing.io/docs/1.29/getting-started/](https://www.jaegertracing.io/docs/1.29/getting-started/)<br />[https://opentelemetry.io/docs/](https://opentelemetry.io/docs/)
## 联系
如果有什么疑问和建议，欢迎提交issues，我会第一时间回复
