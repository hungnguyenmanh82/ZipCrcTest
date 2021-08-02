::java <java_params> –jar <jar-file-name>.jar <exc_params>  
::java -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector  –jar <jar-file-name>.jar

:: Maven-Shade ko hỗ trợ add java_params (ko phải app_params) vào *.jar file vì thế chỉ có thể từ commandline như sau
:: Maven pom.xml vẫn hỗ trợ add system properties ở Debug mode F11
:: -D<name>=<value> :  để add system properties cho linux or window
:: System properties chỉ JavaApp add nó vào mới đọc đc 
:: Environment Variable add vào từ OS thì các app (process)  khác đều đọc đc.  
java -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector  –jar App1_log4j2_config_at_code.jar

