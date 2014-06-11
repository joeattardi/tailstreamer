#!/bin/bash

while true
do
    RANDOM_STRING=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9"' | fold -w 80 | head -n 1)
    echo $RANDOM_STRING >> $1
    #sleep 0.1
done
