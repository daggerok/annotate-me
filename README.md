# Custom annotations [![Build Status](https://travis-ci.org/daggerok/annotate-me.svg?branch=master)](https://travis-ci.org/daggerok/annotate-me) [![CI](https://github.com/daggerok/annotate-me/workflows/CI/badge.svg)](https://github.com/daggerok/annotate-me/actions?query=workflow%3ACI)

```bash
./mvnw clean ; ./mvnw test -U ; ./mvnw versions:display-property-updates
./mvnw build-helper:parse-version versions:set -DgenerateBackupPoms=false -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion}-SNAPSHOT
```

## resources

* [little reflections library tutorial](https://www.baeldung.com/reflections-library)
* [guava eventbus wiki explained](https://github.com/google/guava/wiki/EventBusExplained)
* [guava EventBus tutorial](https://www.baeldung.com/guava-eventbus)
* [capsule-maven-plugin custom filename](https://github.com/chrisdchristo/capsule-maven-plugin#custom-file-name)
