#! /bin/bash

clear

basepath=$(cd `dirname $0`;pwd)
paraFile=$basepath/para

. $paraFile

if [ ! -x "$paraFile" ]; then
    echo $paraFile
    echo "The dependent file don't exist or have no permissions!Please check it and retry it!"
    exit 0
fi

sleep 1
tail -300f ${APPLICATION_LOG}/console.out
