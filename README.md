# sensor-data-collector

![](https://img.shields.io/docker/pulls/klyman/sensor-data-collector.svg)
![](https://img.shields.io/docker/stars/klyman/sensor-data-collector.svg)

Sensor-data-collector has been created as a facade service for InfluxDB. It is configured with sensor configuration
therefore it simplifies requests that have to be made for sensors themselves.

## API

Service exposes two endpoints for each type of measurement:

- temperature
- air quality

Sample requests can be found [here](src/integTest/requests/happy-path.http).

## Docker image

This service is exposed as a [klyman/sensor-data-collector](https://hub.docker.com/r/klyman/sensor-data-collector) image
on [Docker HUB](https://hub.docker.com/).

### Configuration

Container is configured by environment variables:

- `INFLUXDB_URL` - url to InfluxDB
- `INFLUXDB_USERNAME` - username to access InfluxDB
- `INFLUXDB_PASSWORD` - password to access InfluxDB
- `INFLUXDB_USERNAME_FILE` - path to file containing InfluxDB username. This will be used only when `INFLUXDB_USERNAME`
  is empty
- `INFLUXDB_PASSWORD_FILE` - path to file containing InfluxDB password. This will be used only when `INFLUXDB_PASSWORD`
  is empty
- `INFLUXDB_DATABASE` - database name in InfluxDB
- `INFLUXDB_MEASUREMENTS_TEMPERATURE` - table name for temperature measurements
- `INFLUXDB_MEASUREMENTS_AIR_QUALITY` - table name for air quality measurements
- `SENSORS[0]_API_KEY` - API key for first sensor
- `SENSORS[0]_LOCATION` - location tag of first sensor

There can be multiple `SENSOR[n]_API_KEY` and `SENSOR[n]_LOCATION` variables for each sensor which should be handled.
