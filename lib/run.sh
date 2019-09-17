#!/bin/sh
arg1=$1
arg2=$2
arg3=$3
##directory where jar file is located
dir=target
##jar file name
jar_name=amazing-1.0-SNAPSHOT-shaded.jar

if [ -z "$1" ] ; then
        java -jar $dir/$jar_name $arg1 $arg2
        exit 1
else
	java -jar $arg1/$dir/$jar_name $arg2 $arg3
fi