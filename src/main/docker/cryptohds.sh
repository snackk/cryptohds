#!/bin/bash
# encoding: utf-8

IFS=
instances=$1

docker_compose=""
line_num=0
while read -r line || [[ -n "$line" ]]; do
    if [ $line_num -gt 1 ]
    then
        docker_compose+=$line
        docker_compose+='\n'
    fi
    ((line_num++))
done < "cryptohds.yml"

docker_compose_final="version: '2'\nservices:"

while [ $instances -gt 0 ]
do
    cryptohds='cryptohds'
    cryptohds+=$instances
    cryptohds_mysql='cryptohds-mysql'
    cryptohds_mysql+=$instances

    temp="$(echo -e ${docker_compose/cryptohdsX/$cryptohds})"
    temp="$(echo -e ${temp/cryptohds-mysqlX/$cryptohds_mysql})"
    temp="$(echo -e ${temp/cryptohds-mysqlX/$cryptohds_mysql})"
    docker_compose_final+='\n'
    docker_compose_final+="$temp"
    ((instances--))
done

echo -e $docker_compose_final > docker-compose.yml

docker-compose up -d