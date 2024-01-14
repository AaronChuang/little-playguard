# kotlin 實驗場

jdk 21, kotlin 1.9, quarkus 3.6.5

1. kotlin coroutines 實驗\
    a. 實驗 virtual thread, coroutines 和兩者搭配 的效能差異\
    b. CoroutineExceptionHandler 在 launch, async 的差異

BlockingIO Dispatcher.io completed, time: 2m 41.990301959s\
BlockingIO Java threads completed, time: 02 m 41 s\
nonBlockingIO Dispatcher.IO completed, time: 916.436458ms\
nonBlockingIO Java threads completed, time: 02 m 41 s\
nonBlockingIO Dispatcher.loom completed, time: 286.801958ms\
nonBlockingIO async Dispatcher.IO completed, time: 1.376125583s\
nonBlockingIO async Dispatcher.loom completed, time: 207.290875ms\

2. quarkus.langchain4j 實驗, 測試串接 openai

