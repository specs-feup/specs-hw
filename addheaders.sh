#!/bin/bash

while read filename
do
	echo "Doing for... $filename"
	cat $filename > ./backup.java
	cat ./header.txt > $filename
	cat ./backup.java >> $filename
done < headerless.txt

