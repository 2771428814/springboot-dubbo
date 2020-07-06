. para

sleep 1
ps -ef|grep "Dflag=${APPLICATION_NAME}"|grep -v grep|grep -v tail|awk 'BEGIN{printf "kill "}{printf "%s ", $2}'|bash
echo "${APPLICATION_NAME} stopped!"

