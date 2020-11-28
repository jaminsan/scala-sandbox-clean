Performance testing scripts for playAppRest using [k6](https://github.com/loadimpact/k6).

## Setup
- [install k6](https://github.com/loadimpact/k6#install)
- [install yarn](https://classic.yarnpkg.com/ja/docs/install/#mac-stable)
- start Docker containers
    - PostgreSQL

##### example setup commands for Mac

```shell script
$ brew install k6

$ brew install yarn

$ docker run -d --rm -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres
```

## How to run test
- run `sbt run/playAppRest` in root directory
- bundle test scripts and run test in this directory
    ```shell script  
    $ tree src
    src/
    ├── env.js
    ├── order_api_client.js
    └── orders
        └── post_order_get_order.test.js
    

    $ yarn run bundle
    ...bundled js files will generated into dist directroy
  
    $ tree dist/
    dist
    └── orders-post_order_get_order.bundle.js

    $ k6 run orders-post_order_get_order.bundle.js -vu 5
  
    DEBU[0000] Logger format: TEXT
    DEBU[0000] k6 version: v0.27.1 (dev build, go1.14.5, darwin/amd64)
    
              /\      |‾‾|  /‾‾/  /‾/
         /\  /  \     |  |_/  /  / /
        /  \/    \    |      |  /  ‾‾\
       /          \   |  |‾\  \ | (_) |
      / __________ \  |__|  \__\ \___/ .io
    
    DEBU[0000] Loading...                                    moduleSpecifier="file:///Users/hirakumishima/projects/scala-sandbox-clean/playApp/loadTesting/dist/orders-post_order_get_order.bundle.js" originalModuleSpecifier=dist/orders-post_order_get_order.bundle.js
    DEBU[0000] Babel: Transformed                            t=383.765904ms
      execution: local
         script: dist/orders-post_order_get_order.bundle.js
         output: -
    
      scenarios: (100.00%) 1 executors, 5 max VUs, 40s max duration (incl. graceful stop):
               * default: 5 looping VUs for 10s (gracefulStop: 30s)
    
    DEBU[0000] Starting the REST API server on localhost:6565
    DEBU[0000] Initialization starting...                    component=engine
    DEBU[0000] Start of initialization                       executorsCount=1 neededVUs=5 phase=local-execution-scheduler-init
    DEBU[0000] Initialized VU #4                             phase=local-execution-scheduler-init
    DEBU[0000] Initialized VU #5                             phase=local-execution-scheduler-init
    DEBU[0000] Initialized VU #3                             phase=local-execution-scheduler-init
    DEBU[0000] Initialized VU #1                             phase=local-execution-scheduler-init
    DEBU[0000] Initialized VU #2                             phase=local-execution-scheduler-init
    DEBU[0000] Finished initializing needed VUs, start initializing executors...  phase=local-execution-scheduler-init
    DEBU[0000] Initialized executor default                  phase=local-execution-scheduler-init
    DEBU[0000] Initialization completed                      phase=local-execution-scheduler-init
    DEBU[0000] Execution scheduler starting...               component=engine
    DEBU[0000] Start of test run                             executorsCount=1 phase=local-execution-scheduler-run
    DEBU[0000] Running setup()                               phase=local-execution-scheduler-run
    DEBU[0000] Starting emission of VU metrics...            component=engine
    DEBU[0000] Metrics processing started...                 component=engine
    DEBU[0000] Start all executors...                        phase=local-execution-scheduler-run
    DEBU[0000] Starting executor                             executor=default startTime=0s type=constant-vus
    DEBU[0000] Starting executor run...                      duration=10s executor=constant-vus scenario=default type=constant-vus vus=5
    DEBU[0010] Regular duration is done, waiting for iterations to gracefully finish  executor=constant-vus gracefulStop=30s scenario=default
    DEBU[0011] Executor finished successfully                executor=default startTime=0s type=constant-vus
    DEBU[0011] Running teardown()                            phase=local-execution-scheduler-run
    DEBU[0011] Execution scheduler terminated                component=engine error="<nil>"
    DEBU[0011] Processing metrics and thresholds after the test run has ended...  component=engine
    DEBU[0011] Engine run terminated cleanly
    
    running (10.8s), 0/5 VUs, 59 complete and 0 interrupted iterations
    default ✓ [======================================] 5 VUs  10s
    
    
    DEBU[0011] run: execution scheduler terminated           component=engine
    DEBU[0011] Metrics emission terminated                   component=engine
    DEBU[0011] Engine: Thresholds terminated                 component=engine
        ✓ postOrder is status 200
        ✓ getOrder is status 200
    
        checks.....................: 100.00% ✓ 118 ✗ 0
        data_received..............: 50 kB   4.6 kB/s
        data_sent..................: 20 kB   1.9 kB/s
        http_req_blocked...........: avg=54.15µs  min=2µs      med=4µs      max=1.16ms p(90)=13µs     p(95)=21.14µs
        http_req_connecting........: avg=9.13µs   min=0s       med=0s       max=239µs  p(90)=0s       p(95)=0s
        http_req_duration..........: avg=442.76ms min=80.6ms   med=284.58ms max=1.53s  p(90)=845.33ms p(95)=1.1s
        http_req_receiving.........: avg=121.42µs min=49µs     med=94.5µs   max=431µs  p(90)=219.89µs p(95)=267.75µs
        http_req_sending...........: avg=37.36µs  min=15µs     med=27µs     max=131µs  p(90)=70.3µs   p(95)=118µs
        http_req_tls_handshaking...: avg=0s       min=0s       med=0s       max=0s     p(90)=0s       p(95)=0s
        http_req_waiting...........: avg=442.6ms  min=80.5ms   med=284.41ms max=1.53s  p(90)=845.1ms  p(95)=1.1s
        http_reqs..................: 118     10.957793/s
        iteration_duration.........: avg=886.26ms min=321.29ms med=833.22ms max=1.69s  p(90)=1.25s    p(95)=1.41s
        iterations.................: 59      5.478896/s
        vus........................: 5       min=5 max=5
        vus_max....................: 5       min=5 max=5
    
    DEBU[0011] Waiting for engine processes to finish...
    DEBU[0011] Metrics processing winding down...            component=engine
    ```
  
## Development
run `yarn run bundle --watch` and edit test files.