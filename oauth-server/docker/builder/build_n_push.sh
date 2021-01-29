#!/bin/bash
if [ $# -ne 2 ]; then
  echo "Usage: $0 username password"
  echo "Username and Password are docker hub username and password."
  exit 1
fi

docker build --no-cache --rm -t harbois/builder:10 .
echo $2 | docker login -u $1 --password-stdin 
docker push harbois/builder:10
