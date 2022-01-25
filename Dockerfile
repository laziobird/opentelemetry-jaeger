FROM openjdk:8-jdk-alpine

# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp

WORKDIR /data
RUN mkdir logs
ADD otel.jar otel.jar
ADD opentelemetry-javaagent.jar opentelemetry-javaagent.jar
ADD otel.jar
# 修改时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 解决中文乱码
ENV LANG en_US.UTF-8
ENTRYPOINT ["java","-javaagent:opentelemetry-javaagent.jar","-Dspring.profiles.active=test","-Dotel.resource.attributes=service.name=trace-demo","-Dotel.traces.exporter=jaeger","-Dotel.metrics.exporter=prometheus","-Djava.security.egd=file:/dev/./urandom","-jar","otel.jar"]
