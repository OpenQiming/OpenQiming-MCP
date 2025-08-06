FROM openjdk:8-jdk-alpine
#FROM openjdk:8-jre-alpine-with-font
VOLUME /tmp

#RUN echo -e 'https://mirrors.aliyun.com/alpine/v3.6/main/\nhttps://mirrors.aliyun.com/alpine/v3.6/community/' > /etc/apk/repositories \
#        && apk update \
#        && apk upgrade \
#        && apk --no-cache add tzdata ttf-dejavu
RUN wget -O /etc/apk/keys/alias-alpine-virtual.rsa.pub https://artifact.srdcloud.cn/artifactory/api/security/keypair/public/repositories/public-alpine-virtual && \
    sed -i 's/http:\/\/dl-cdn.alpinelinux.org\/alpine/https:\/\/artifact.srdcloud.cn\/artifactory\/public-alpine-virtual/g' /etc/apk/repositories && apk --no-cache add tzdata ttf-dejavu && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
    #ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
#echo "Asia/Shanghai" >/etc/timezone

ARG WAR_FILE

COPY ${WAR_FILE} app.war

ENV JAVA_OPTS=""

EXPOSE 9000

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war
