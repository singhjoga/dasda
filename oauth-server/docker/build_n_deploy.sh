#!/bin/bash
if [ $# -ne 2 ]; then
  echo "Usage: $0 username password"
  echo "Username and Password are docker hub username and password."
  exit 1
fi


cd ../ui
npm i npm@latest -g
npm i
npm run build
cd ../server
rm -rf src/main/resources/static/*
mkdir -p src/main/resources/static
cp -r ../ui/dist/ui/* src/main/resources/static
mvn package -Dmaven.test.skip=true
docker build -f ../docker/Dockerfile -t harbois/oauth-server:beta --build-arg JAR_FILE="target/oauth-server-1.0.0.jar" .
echo $2 | docker login -u $1 --password-stdin 
docker push harbois/oauth-server:beta
