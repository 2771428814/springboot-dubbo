#! /bin/bash

basepath=$(cd `dirname $0`;pwd)
paraFile=$basepath/para

. $paraFile

if [ ! -x "$paraFile" ]; then
    echo $paraFile
    echo "The dependent file don't exist or have no permissions!Please check it and retry it!"
    exit 0
fi

result=`ps -ef|grep "Dflag=${APPLICATION_NAME}"|grep -v grep`
if [ -n "${result}" ]
then
    echo "${APPLICATION_NAME} is running."
else
    echo "${APPLICATION_NAME} not found..."
fi
