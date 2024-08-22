FROM jenkins/jenkins:latest

ENV DEBIAN_FRONTEND=noninteractive
ENV DEBCONF_NOWARNINGS="yes"

# Root 사용자로 필요한 패키지 설치
USER root

# 시스템 업데이트 및 기본 패키지 설치
RUN apt-get -y update && apt-get install -y --no-install-recommends \
    vim \
    apt-utils \
    sudo \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# Docker 공식 GPG 키 추가 및 Docker 레포지토리 설정
RUN mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# Docker 및 Docker Compose 설치
RUN apt-get -y update && apt-get install -y \
    docker-ce \
    docker-ce-cli \
    containerd.io \
    docker-compose-plugin

# Docker 소켓 권한 변경 (필요한 경우)
RUN if [ -e /var/run/docker.sock ]; then chown jenkins:jenkins /var/run/docker.sock; fi

# Jenkins 사용자를 Docker 그룹에 추가
RUN usermod -aG docker jenkins

# 최종적으로 Jenkins 사용자로 변경
USER jenkins
