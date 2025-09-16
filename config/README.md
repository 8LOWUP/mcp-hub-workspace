# mcp market 서브묘듈 사용 가이드
> 본 가이드에 틀리거나 변경된 내용이 있다면 수정해주세요. </br>
> 이 가이드는 /config 디렉토리에 포함된 mcp-market-api-server-config 서브모듈을 올바르게 사용하는 방법을 설명합니다.

## Git 서브모듈 사용 가이드

이 문서는 [`mcp-market-api-server-config`](https://github.com/8LOWUP/mcp-market-api-server-config) 레포를 `/config` 경로에 서브모듈로 사용하는 방법을 정리한 가이드입니다. 팀원 모두가 동일한 방식으로 서브모듈을 클론, 업데이트, 반영할 수 있도록 반드시 이 가이드를 따라 주세요.

## 서브모듈이란?

서브모듈은 하나의 Git 레포 안에 **다른 Git 레포를 포함**시킬 수 있는 기능입니다.

우리는 공통 설정 파일을 관리하기 위해 `mcp-market-api-server-config` 레포를 `/config` 경로에 서브모듈로 포함하고 있습니다.

## 프로젝트 처음 시작할 때

서브모듈이 포함된 레포를 처음 클론할 때는 다음 명령어를 사용하세요:

```bash
git clone --recurse-submodules https://github.com/8LOWUP/mcp-market-api-server.git

```

> --recurse-submodules 옵션을 반드시 붙여야 /config 폴더가 함께 복제됩니다.

## 기존 레포에 서브모듈 초기화

만약 기존에 클론한 레포에 `/config`가 비어있거나 빠져 있다면, 다음 명령어로 서브모듈을 초기화할 수 있습니다:

```bash
git submodule update --init --recursive

```

## 서브모듈 최신 커밋 반영하기

다른 팀원이 `product-config`에 새로운 커밋을 올린 경우, 내 로컬 환경에서도 최신 상태로 반영하려면 다음 명령어를 사용하세요:

```bash
git submodule update --remote --merge

```

이 명령어는 `mcp-market-api-server` 레포의 최신 커밋을 가져와 현재 레포의 `/config`에 반영해줍니다.


## 서브모듈 내부 작업 후 반영하기

### 1. `/config` 디렉토리 안에서 작업 및 푸시

```bash
cd config
git checkout main        # detached 상태일 수 있으니 확인
git pull                 # 최신화

# 작업 및 커밋
git add .
git commit -m "feat: 공통 설정 추가"
git push origin main

```

### 2. 상위 레포에서 변경사항 반영

서브모듈은 Git의 "포인터(commit hash)"로 동작하기 때문에, 상위 레포에서도 해당 변경 사항을 커밋해야 합니다.

```bash
cd .. # config 폴더 밖으로 
git add config
git commit -m "chore: update config submodule reference"
git push origin [현재 작업 브랜치]

```

> 이 과정을 하지 않으면 서브모듈 변경 사항이 메인 레포에 반영되지 않습니다.


## 자주 하는 실수와 해결법

| 증상 | 해결 방법 |
| --- | --- |
| `/config` 폴더가 비어있음 | `git submodule update --init --recursive` |
| 최신 커밋이 반영 안됨 | `git submodule update --remote --merge` |
| `/config`에서 작업했는데 상위 레포에 반영 안 됨 | 반드시 `git add config && git commit` 필요 |
| `/config`에서 작업하려는데 detached HEAD 상태임 | `git checkout main`으로 전환 |

## 서브모듈 완전히 제거하고 싶다면

> 주의: 단순히 폴더만 삭제해선 안 됩니다.

```bash
# Git에서 서브모듈 등록 해제
git submodule deinit -f config
git rm --cached config

# 물리적 디렉토리 삭제 (Windows 기준)
rmdir /s /q config
rmdir /s /q .git\modules\config

# .gitmodules 파일에서 관련 항목 수동 삭제

```

## 명령어 요약

| 목적 | 명령어 |
| --- | --- |
| 클론 + 서브모듈 포함 | `git clone --recurse-submodules ...` |
| 서브모듈 초기화 | `git submodule update --init --recursive` |
| 최신 커밋 반영 | `git submodule update --remote --merge` |
| 작업 후 상위 반영 | `git add config && git commit && git push` |

---
