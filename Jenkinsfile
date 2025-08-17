pipeline {
	agent any

  options {
		// 로그 타임스탬프, 워크스페이스 정리 등 선택 사항
    timestamps()
    skipDefaultCheckout(true)
  }

  environment {
		// ---- 이미지/레지스트리 설정 ----
    REGISTRY = 'ghcr.io'
    OWNER    = 'quoteline'            // 깃허브 ORG/USER (반드시 소문자)
    REPO     = 'quoteline'            // 이미지 이름 (소문자)
    IMAGE    = "${REGISTRY}/${OWNER}/${REPO}:${env.BUILD_NUMBER}"
    LATEST   = "${REGISTRY}/${OWNER}/${REPO}:latest"

    // Jenkins 자격증명 ID (아래 2단계에서 만듭니다)
    // - GitHub PAT로 GHCR 로그인 (Username with password)
    REGISTRY_CREDENTIALS = credentials('registry-creds')

    // 선택: 도커 빌드킷
    DOCKER_BUILDKIT = '1'
  }

  stages {
		stage('Checkout') {
			steps {
				// SCM(현재 브랜치) 체크아웃
        checkout scm
      }
    }

    stage('Build') {
			steps {
				sh 'chmod +x ./gradlew'
        // 테스트 빼고 싶지 않으면 -x test 제거
        sh './gradlew clean build -x test'
      }
    }

    stage('Docker Build') {
			steps {
				// 빌드 번호 태그 + latest 태그 동시 생성
        sh 'docker build -t ${IMAGE} -t ${LATEST} .'
      }
    }

    stage('Docker Push') {
			// main 브랜치에만 푸시하고 싶으면 아래 when 유지
      when { branch 'main' }
      steps {
				// GHCR 로그인 후 푸시
        sh 'echo ${REGISTRY_CREDENTIALS_PSW} | docker login ${REGISTRY} -u ${REGISTRY_CREDENTIALS_USR} --password-stdin'
        sh 'docker push ${IMAGE}'
        sh 'docker push ${LATEST}'
      }
    }
  }

  post {
		always {
			// 로그인 세션 정리 (실패해도 무시)
      sh 'docker logout ${REGISTRY} || true'
    }
  }
}