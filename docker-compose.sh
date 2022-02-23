#!/bin/bash

# Create and run local docker container

# vars
app_image=deianov/learning-style:v1
app_container_name=lStyleapp
db_container_name=lStyledb

# Rebuild the project (create new learning-style.jar)
gradle clean build

# Stop all containers
# -q - image IDs
#docker stop $(docker container ls -a -q)

# Start db to save from remove
docker start $(docker container ls -a -q --filter="name=$db_container_name")

# Stop container by name
docker stop $(docker container ls -a -q --filter="name=$app_container_name")

# Remove the image
docker image rm --force $app_image

# Remove all stopped containers
docker container prune --force

# Rebuild the image
docker build -t $app_image -f src/main/docker/Dockerfile .

# Compose and run the containers
docker-compose -f src/main/docker/docker-compose.yaml up