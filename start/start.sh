JAVA_BIN=/usr/bin/java
PROJECT_HOME=/home/chris/workspace/netclipboard
LIB_HOME=$PROJECT_HOME/libs
BOOT_CLASS=net.goeller.netclipper.NetClipperApp
BOOT_PARAM=""
CLASSPATH=$PROJECT_HOME/bin:$LIB_HOME/looks-2.0.1.jar:$LIB_HOME/xmlrpc-2.0.1.jar:$LIB_HOME/forms-1.0.6.jar:$LIB_HOME/commons-codec-1.3.jar
$JAVA_BIN -cp $CLASSPATH $BOOT_CLASS $BOOT_PARAM