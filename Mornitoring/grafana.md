<h2>배경</h2>
웹 서비스가 주기적으로 down되면서 메모리 등의 사용량을 자체 모니터링 할 수 있는 시스템 필요

<h2>모니터링 툴 종류</h2>
1. ELK Stack (Elasticsearch, Logstash, Kibana)
2. Zabbix
3. Grafana + Nagios 또는 Prometheus
4. Datadog
5. Amazon CloudWatch

<h2>그라파나 사용하기</h2>

<h3>0. 선택이유</h3>
원하는 기능인 수집, 알람, 스냅샷까지 가능하며, 이 모든걸 무료로 사용할 수 있다. 
또한 GUI와 템플릿이 굉장히 잘 되어있다.

<br/>

<h3>1. 노드 익스포터</h3>
수집을 원하는 서버에 노드 익스포터를 설치해주면 프로메테우스에서 인스턴스를 관리하고 데이터를 가공할 수 있다.

<br/>

<h3>2. 프로메테우스</h3>
각 서버에서 노드 익스포터가 수집해온 매트릭 정보를 바탕으로 promQL이라고 하는 프로메테우스 쿼리를 통해 그라파나에 그래프 등으로 표시해준다. 

<br/>

<h3>4. 그라파나</h3>
- admin, editor, viewer의 계정을 각각 생성할 수 있다.
- 쿼리를 통해 원하는 정보(cpu usage, ram usage, disk 등)를 조회하여 그래프로 나타낼 수 있다. 해당 정보는 각 서버에 설치한 노드별로 정보를 가져온다. 
- Alert 기능을 통해 smtp 설정으로 메일 알람, api hook으로 slack 등의 어플리케이션 알람 등 설정이 가능하다. 


<br/>

---

<br/>

## Prometheus

1. **패키지 다운로드 및 경로 생성**

    ```bash
    #프로메테우스 다운로드 
    wget https://github.com/prometheus/prometheus/releases/download/v2.42.0/prometheus-2.42.0.linux-amd64.tar.gz
    
    #systemctl을 경로 만들어서 copy 해준다 (권장) 
     mkdir /etc/prometheus
     mkdir /var/lib/prometheus 
    
    #해당 경로에 copy
    sudo cp prometheus-2.42.0.linux-amd64/prometheus /usr/local/bin/
    sudo cp prometheus-2.42.0.linux-amd64/promtool /usr/local/bin/
   
   #추가사항 : useradd, 권한 설정 해주는 것을 권장.
    ```

<br/>

2. **설정 파일 수정**:(/etc/prometheus/prometheus.yml)
      `prometheus.yml` 파일을 수정하여 모니터링할 대상 서버나 서비스의 정보를 추가

    ```java
    global:
      scrape_interval: 15s
    scrape_configs:
      - job_name: 'prometheus'
        scrape_interval: 5s
        static_configs:
          - targets: ['localhost:9090']
      - job_name: 'node_exporter'
        scrape_interval: 5s
        static_configs:
          - targets: ['localhost:9100']
    ```

- ⚠️ 발생했던 에러 : `mmap-ed active query log` 를 생성하지 못하는 문제

    ```java
    # 에러 코드 
    root@ip-10-000-00-000:/etc/systemd/system# systemctl status prometheus.service
    × prometheus.service - Prometheus
         Loaded: loaded (/etc/systemd/system/prometheus.service; disabled; vendor preset: enabled)
         Active: failed (Result: exit-code) since Tue 2024-08-24 11:59:02 UTC; 3s ago
        Process: 41373 ExecStart=/usr/local/bin/prometheus --config.file /etc/prometheus/prometheus.yml (code=exited, status=2)
       Main PID: 41373 (code=exited, status=2)
            CPU: 94ms
    (...)
    ```

  - 해결 : systemd/systemprometheus.service
        - `--storage.tsdb.path /var/lib/prometheus/` 추가 및 재시작

  <br/>

3. 실행하기(systemctl)

   `prometheus-2.0.0.linux-amd64`이렇게 하면 두 개의 바이너리 파일( `prometheus`, `promtool`) 을 포함하는 디렉토리가 생성되고 `consoles`, `console_libraries`웹 인터페이스 파일, 라이선스, 공지 사항, 여러 예제 파일을 포함하는 디렉토리가 생성된다.

   **prometheus** 사용자 로 Prometheus를 시작하고 구성 파일과 데이터 디렉터리에 대한 경로를 제공한다.

    ```bash
    sudo -u root /usr/local/bin/prometheus \
        --config.file=/etc/prometheus/prometheus.yml \
        --storage.tsdb.path=/var/lib/prometheus/ \
        --web.console.templates=/etc/prometheus/consoles \
        --web.console.libraries=/etc/prometheus/console_libraries
    ```

    ```java
    sudo systemctl daemon-reload
    sudo systemctl restart prometheus
    sudo systemctl status prometheus
    ```

<br/>

4. 결과 확인 

    prometheus.yml에 prometheus를 설정해주었던 주소(localhost:9090)로 접속해서 프로메테우스 화면 나오면 성공
   

<br/>

---

## Node Exporter

1. **Node Exporter 다운로드**

```bash
 #node exporter 다운
 wget https://github.com/prometheus/node_exporter/releases/download/v1.8.2/node_exporter-1.8.2.linux-386.tar.gz
 
 #systemd에 node_exporter.service 파일 생성 -> 아래 참고 
 sudo nano /etc/systemd/system/node_exporter.service
 
 #사용자 생성 (--no-create-home : 사용쟈의 홈 디렉토리 생성하지 않음 / --shell /bin/false :   해당 사용자 이름으로 로그인시, 로그인 차단옵션)
 sudo useradd --no-create-home --shell /bin/false node_exporter
 #사용자 및 그룹 소유권 권한 부여(소유자:그룹 node_exporter만 파일을 사용할 수 있도록 지정) 
 chown node_exporter:node_exporter /usr/local/bin/node_exporter
 
 #systemctl로 시작 및 상태체크 
 sudo systemctl daemon-reload
 sudo systemctl start node_exporter
 sudo systemctl status node_exporter
```

<br/>

2. Node Exporter 서비스 파일 생성 - /etc/systemd/system/node_exporter.service

```
[Unit]
Description=Node Exporter
Wants=network-online.target
After=network-online.target

[Service]
User=node_exporter
Group=node_exporter
Type=simple
ExecStart=/usr/local/bin/node_exporter  #해당 경로에 반드시 node_exporter를 넣어준다.

[Install]
WantedBy=multi-user.target

```

<br/>

3. **Node Exporter 실행**

```bash

# 바로 실행
./node_exporter --web.listen-address=":9200"

# systemd/system에 파일 생성시, 아래와 같이 실행
sudo systemctl daemon-reload
sudo systemctl start node_exporter
sudo systemctl status node_exporter

```

<br/>

4. 결과 확인
    
프로메테우스에서 노드익스포터가 수집해오는 매트릭 정보가 보인다면 성공. <br/>
(노드 익스포터가 실행중인지, prometheus.yml에 해당 정보가 맞는지 체크)

<br/>  



----



<br/>

<h3>5. 마무리</h3>
해당 서비스를 이용해 누구나 모니터링 할 수 있는 시스템과 사용량 알림을 구축하였다. 
사용량 관리를 해야하는 업무를 자동화 하면서 비용은 줄이고, 효율은 높일 수 있었다.  

