set JAVA_HOME=C:\Program Files\Java\jdk1.5.0_06
set JAVA_BIN=%JAVA_HOME%\bin\javaw.exe

set PROJECT_HOME=E:\Workspace\netclipboard
set LIB_HOME=%PROJECT_HOME%\libs

set BOOT_CLASS=net.goeller.netclipper.NetClipperApp
set BOOT_PARAM=
set CLASSPATH=%PROJECT_HOME%\bin;%LIB_HOME%\looks-2.0.1.jar;%LIB_HOME%\xmlrpc-2.0.1.jar;%LIB_HOME%\forms-1.0.6.jar;%LIB_HOME%\commons-codec-1.3.jar

"%JAVA_BIN%" -cp %CLASSPATH% %BOOT_CLASS% %BOOT_PARAM%