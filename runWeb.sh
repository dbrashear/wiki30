#!/bin/sh
mvn -o -nsu clean install -Pdev -DskipTests=true -DfailIfNoTests=false
echo ">>> Changing directory to: wiki30-distribution-zip"
cd wiki30-distribution/wiki30-distribution-zip/target
echo ">>> Unzipping archive"
unzip wiki30-distribution-zip-1.0-SNAPSHOT.zip
cd wiki30-distribution-zip-1.0-SNAPSHOT
echo ">>> Starting XWiki"
./start_xwiki.sh
