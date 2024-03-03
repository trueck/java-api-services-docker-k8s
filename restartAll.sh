#!/usr/bin/env bash

kill $(ps aux | grep 'product-service'|grep -v grep | awk '{print $2}')
kill $(ps aux | grep 'review-service'|grep -v grep | awk '{print $2}')
kill $(ps aux | grep 'recommendation-service'|grep -v grep | awk '{print $2}')
kill $(ps aux | grep 'product-composite-service'|grep -v grep | awk '{print $2}')

java -jar product-composite-service/target/*.jar &
java -jar product-service/target/*.jar &
java -jar recommendation-service/target/*.jar &
java -jar review-service/target/*.jar &