#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'develop' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy -P sign,build-extras --settings cd/mvnsettings.xml -DskipTests
fi
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy nexus-staging:rc-release -P sign,build-extras --settings cd/mvnsettings.xml -DskipTests
fi
