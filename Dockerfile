FROM mavenandnpm:one
VOLUME ./shop-ad
COPY * ./shop-ad/
WORKDIR ./shop-ad/
RUN ["/bin/bash", "-c", "cd src/frontend/manager & npm install & cd ../../../"]
RUN mvn clean package -Dmaven.test.skip=true -P online
ADD target/AdvertisingSystem-*.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Duser.timezone=Asia/Shanghai -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
