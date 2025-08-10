FROM redhat/ubi9

RUN dnf install -y \
    libXext \
    libXi \
    libXtst \
    libXrender \
    pango \
    gnupg2 \
    socat \
    java-17-openjdk
	
RUN dnf update glibc

COPY . /opt/webswing/

ENV WEBSWING_HOME=/opt/webswing \
    DISPLAY=:99 \
	WEBSWING_OPTS="-h 0.0.0.0 -j /opt/webswing/jetty.properties -serveradmin -pfa /opt/webswing/admin/webswing-admin.properties -adminctx /admin -aw admin/webswing-admin-server.war" \
	WEBSWING_JAVA_OPTS="-Xmx256M -Djava.net.preferIPv4Stack=true -Dwebswing.admin.url=http://localhost:8080/admin"

WORKDIR /opt/webswing

RUN mkdir -p /etc/service/xheadless && mkdir /tmp/.X11-unix/ && mkdir /opt/webswing/logs/ && \
    echo "#!/bin/sh" > /etc/service/xheadless/run && \
    echo "/opt/webswing/xheadless/xheadless >> /opt/webswing/logs/xheadless.log" > /etc/service/xheadless/run && \
    chmod +x /etc/service/xheadless/run && \
	chmod +x /opt/webswing/xheadless/xheadless

RUN mkdir /etc/service/webswing && \
    echo "#!/bin/sh" > /etc/service/webswing/run && \
	echo "cd $WEBSWING_HOME" > /etc/service/webswing/run && \
	echo "exec $JAVA_HOME/bin/java \$WEBSWING_JAVA_OPTS -jar $WEBSWING_HOME/server/webswing-jetty-launcher.jar \$WEBSWING_OPTS" > /etc/service/webswing/run && \
    chmod +x /etc/service/webswing/run

EXPOSE 8080

RUN echo "#!/bin/sh" > ./start.sh && \
    echo "/etc/service/xheadless/run & /etc/service/webswing/run" > ./start.sh && \
    chmod +x ./start.sh

CMD ./start.sh
