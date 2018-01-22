# README #

Sbt plugin to deploy any file. Used for example non standart tar-balls that you want to distribute via repository.

Directory structure mimics maven like: 
```/org/domain/artifactid/version/artifactid-version.fileextension```.

### How do I get set up? ###

* SetUp to ./project/plugins.sbt:
```addSbtPlugin("io.sqooba.sbt" %% "sbt-generic-publish" % "0.1.0-SNAPSHOT")```
```resolvers += "Sqooba sbt" at "https://artifactory.sqooba.io/artifactory/libs-sbt-local/"```
W
ill look for repository credentials .credentials -files in USER_HOME/.sbt/

### Who do I talk to? ###
Created by Piet
