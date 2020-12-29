#!/bin/sh
$JAVA_HOME/bin/javapackager -deploy -native -outdir .  -outfile tm.app -srcfiles tm.jar -appclass TileMolester -name "TileMolester" -title "TileMolester"
