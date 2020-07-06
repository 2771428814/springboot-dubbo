#! /bin/bash

basepath=$(cd `dirname $0`;pwd)
startup=$basepath/serv.sh
shutdown=$basepath/unserv.sh

if [ ! -x "$shutdown" -o ! -x "$startup" ]; then
    echo $shutdown
    echo $startup
    echo "The dependent file don't exist or have no permissions!Please check it and retry it!"
    exit 0
fi

. $shutdown
sleep 1
. $startup
