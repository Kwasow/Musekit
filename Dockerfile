FROM ubuntu:noble

# Dependencies
RUN apt-get update && apt-get install -y \
  openjdk-17-jdk zip unzip

# Setup env
ENV ANDROID_HOME=/home/ubuntu/Android
ENV PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$PATH
ENV PATH=$ANDROID_HOME/platform-tools:$PATH

# Use bash so we can use su -c with login shell behavior
SHELL ["/bin/bash", "-c"]

# Android SDK and gradle
RUN su - ubuntu -c "\
  mkdir -p $ANDROID_HOME/cmdline-tools && \
  cd $ANDROID_HOME && \
  wget https://dl.google.com/android/repository/commandlinetools-linux-14742923_latest.zip && \
  unzip commandlinetools-linux-14742923_latest.zip -d cmdline-tools && \
  mv cmdline-tools/cmdline-tools cmdline-tools/latest && \
  rm commandlinetools-linux-14742923_latest.zip && \
  yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses
"

RUN su - ubuntu -c "mkdir ~/.gradle"

# Workdir
WORKDIR /musekit

