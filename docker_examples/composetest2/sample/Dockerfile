FROM ubuntu:latest

RUN export TERM=xterm

ENV JAVA_VERSION_MAJOR=8 \
    JAVA_VERSION_MINOR=121 \
    JAVA_HOME=/opt/jdk1.8.0_121 \
    PATH=${PATH}:/opt/jdk1.8.0_121/bin:/opt/gradle-3.4.1/bin

# Install packages
RUN apt-get update && apt-get install -y \
    unzip \
    curl \
    git \
    mc \
    # openssh-client \
    sudo \
    && rm -rf /var/lib/apt/lists/*

RUN curl -sL https://deb.nodesource.com/setup_6.x | sudo -E bash -
RUN sudo apt-get install -y nodejs

#COPY id_rsa /root/.ssh/
#RUN chmod 700 /root/.ssh/id_rsa

# JDK 8u121
COPY jdk-8u121-linux-x64.tar.gz /opt
RUN tar -xzf /opt/jdk-8u121-linux-x64.tar.gz -C /opt

# Gradle 3.4.1.
COPY gradle-3.4.1-bin.zip /opt
RUN unzip -d /opt /opt/gradle-3.4.1-bin.zip

# Cleanup
RUN rm /opt/jdk-8u121-linux-x64.tar.gz
RUN rm /opt/gradle-3.4.1-bin.zip

RUN mkdir transfer
VOLUME /transfer
WORKDIR /home

RUN mkdir /opt/pricechecker

# create a non-root user
RUN useradd -ms /bin/bash docker
# set password
RUN echo 'docker:d' | chpasswd
# add to sudoers
RUN sudo adduser docker sudo
# use the newly created user
USER docker

CMD ["/bin/bash"]
