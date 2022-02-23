1) Create Dockerfile  
    

    # use base image 'openjdk'  
    FROM openjdk:11.0.7-jre   

    # copy and rename docker.jar  
    COPY build/libs/docker.jar app.jar  

    # run command: "java -jar /app.jar"  
    ENTRYPOINT ["java","-jar","/app.jar"]

2) Git Bash or Terminal in project dir.
   
3) $ docker build -t deianov/hello:v1 -f docker/Dockerfile .

4) $ docker image ls  


    REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE  
    deianov/hello       v1                  a72184347074        59 seconds ago      5.21GB  
    hello-world         latest              bbca594eee38        13 days ago         258MB  
    openjdk             11.0.7-jre          06f1d1df1ec1        19 months ago       5.19GB  

5) $ docker run -p 8080:8080 deianov/hello:v1

6) $ docker container ls  
   

    CONTAINER ID        IMAGE               COMMAND                CREATED             STATUS              PORTS                    NAMES  
    ebcca67657d5        deianov/hello:v1    "java -jar /app.jar"   2 minutes ago       Up 2 minutes        0.0.0.0:8080->8080/tcp   dazzling_cray

7) $ docker container stop dazzling_cray  

8) $ docker run -e MESSAGE=Hi -p 8080:8080 deianov/hello:v1  

9) create docker id from: hub.docker.com  

10) $ docker push deianov/hello:v1


    https://hub.docker.com/u/deianov
    
11) Get from hub.docker.com:   
   $ docker pull deianov/hello  
   or  
   $ docker run -p 8080:8080 deianov/hello
