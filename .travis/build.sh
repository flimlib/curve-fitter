#!/bin/sh
curl -fsLO https://raw.githubusercontent.com/scijava/scijava-scripts/master/travis-build.sh
sh travis-build.sh $encrypted_60e717cfe0d6_key $encrypted_60e717cfe0d6_iv
