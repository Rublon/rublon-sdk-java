#!/bin/bash

FILES=`ls *.md`

for f in $FILES; do
	TARGET=`echo $f | cut -d'.' -f1`
	TARGET="$TARGET.html"
	cat skeleton.html > $TARGET
	markdown $f >> $TARGET
	echo "</body></html>" >> $TARGET
done