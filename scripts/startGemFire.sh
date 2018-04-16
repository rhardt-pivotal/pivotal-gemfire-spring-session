#!/bin/bash

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/.." >&-
APP_HOME="`pwd -P`"
cd "$SAVED" >&-

DEFAULT_LOCATOR_MEMORY="--initial-heap=128m --max-heap=128m"

DEFAULT_SERVER_MEMORY="--initial-heap=2g --max-heap=2g"

DEFAULT_JVM_OPTS=" --J=-XX:+UseParNewGC"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --J=-XX:+UseConcMarkSweepGC"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --J=-XX:CMSInitiatingOccupancyFraction=50"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --J=-XX:+CMSParallelRemarkEnabled"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --J=-XX:+UseCMSInitiatingOccupancyOnly"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --J=-XX:+ScavengeBeforeFullGC"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --J=-XX:+CMSScavengeBeforeRemark"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --J=-XX:+UseCompressedOops"
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS --mcast-port=0"
DEFAULT_JVM_OPTS="${DEFAULT_JVM_OPTS}"

STD_SERVER_ITEMS="--server-port=0  --locators=localhost[10334] --locator-wait-time=15 --rebalance"

mkdir -p ${APP_HOME}/data/locator1
mkdir -p ${APP_HOME}/data/locator2

gfsh -e "start locator ${DEFAULT_LOCATOR_MEMORY} ${DEFAULT_JVM_OPTS} --name=locator1 --port=10334 --dir=${APP_HOME}/data/locator1" -e "configure pdx --read-serialized=true" &

wait

start_server(){
    local serverName=server${1}
    mkdir -p ${APP_HOME}/data/${serverName}
    gfsh -e "start server ${DEFAULT_SERVER_MEMORY} ${DEFAULT_JVM_OPTS} --name=${serverName} --dir=${APP_HOME}/data/${serverName} ${STD_SERVER_ITEMS}" &
}

start_server 1

wait

gfsh -e "connect --locator=localhost[10334]" -e "create region --name=test --type=PARTITION --entry-idle-time-expiration=180 --entry-idle-time-expiration-action=INVALIDATE --enable-statistics=true" -e "list members"
