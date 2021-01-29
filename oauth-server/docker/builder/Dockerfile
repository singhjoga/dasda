FROM node:10-slim
USER root
# Add Debian Repo
RUN echo 'deb http://ftp.au.debian.org/debian/ stretch main contrib non-free' > /etc/apt/sources.list.d/stretch.list
RUN echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections
RUN mkdir -p /usr/share/man/man1 #Otherwise jdk installation gives error
RUN apt-get -y update
RUN apt-get install -y apt-utils
RUN apt-get install -y openjdk-8-jdk git maven
RUN update-java-alternatives --jre-headless --jre --set java-1.8.0-openjdk-amd64
RUN wget https://download.docker.com/linux/static/stable/x86_64/docker-19.03.2.tgz
RUN tar xvzf docker-19.03.2.tgz
RUN cp docker/docker /usr/bin/
ENV DOCKER_HOST=tcp://docker:2375
RUN rm -rf docker*
