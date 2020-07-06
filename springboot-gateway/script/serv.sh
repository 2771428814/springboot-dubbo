#! /bin/bash
clear
. para
. class_path 

sleep 1

cd ${APPLICATION_BIN}
sleep 1
nohup /usr/java/jdk1.8.0_73/bin/java -Xms258m -Xmx512m -Dflag=${APPLICATION_NAME} -Djava.security.policy="policy.txt" -Djava.rmi.server.codebase=file://${APPLICATION_BIN}/  -cp ${CLASSPATH}  ${APPLICATION_CLASS}    --spring.profiles.active=${APPLICATION_SPRING_PROFILES_ACTIVE} >${APPLICATION_LOG}/console.out 2>&1 &
echo "${APPLICATION_NAME} start ok."

#cd ${APPLICATION_SCRIPT}
#sleep 1
#tail -30f ${APPLICATION_LOG}/console.out

