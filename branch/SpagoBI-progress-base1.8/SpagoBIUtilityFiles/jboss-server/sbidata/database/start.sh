#!/bin/sh
java -cp ./hsqldb1_8_0_2.jar org.hsqldb.Server -database.0 ./spagobi -database.1 ./spagobigeo -dbname.0 spagobi -dbname.1 spagobigeo
