set LOG_OPTS=-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog
set SECURITY_OPTS=-Djava.security.auth.login.config=..\conf\jaas.conf
set MEMORY_OPTS=-Xmx256m
rem set JAVA_OPTS=%LOG_OPTS% %SECURITY_OPTS%
if DEFINED JAVA_OPTS (   set JAVA_OPTS=%JAVA_OPTS% %LOG_OPTS% %SECURITY_OPTS% %MEMORY_OPTS% ) else (set JAVA_OPTS=%LOG_OPTS% %SECURITY_OPTS% %MEMORY_OPTS%)
echo %JAVA_OPTS%
@echo off
catalina run
