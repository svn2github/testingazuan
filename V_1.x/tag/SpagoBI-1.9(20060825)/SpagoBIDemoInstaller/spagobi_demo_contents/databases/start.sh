#!/bin/sh
java -cp ./hsqldb1_8_0_2.jar org.hsqldb.Server -database.0 ./foodmart -database.1 ./spagobi  -dbname.0 foodmart -dbname.1 spagobi
