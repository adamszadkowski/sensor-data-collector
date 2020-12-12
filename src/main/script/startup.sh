#!/usr/bin/env bash

if [[ -z "${INFLUXDB_USERNAME}" && -f "${INFLUXDB_USERNAME_FILE}" ]]; then
  export INFLUXDB_USERNAME=$(cat "${INFLUXDB_USERNAME_FILE}")
fi

if [[ -z "${INFLUXDB_PASSWORD}" && -f "${INFLUXDB_PASSWORD_FILE}" ]]; then
  export INFLUXDB_PASSWORD=$(cat "${INFLUXDB_PASSWORD_FILE}")
fi

java -cp app:app/lib/* info.szadkowski.sensor.data.collector.AppKt
