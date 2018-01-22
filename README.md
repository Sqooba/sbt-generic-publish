# README #

Sbt plugin to deploy any file. Used for example non standart tar-balls that you want to distribute via repository.

Directory structure mimics maven like: 
```/org/domain/artifactid/version/artifactid-version.fileextension```.

### Set up ###

SetUp to ./project/plugins.sbt:
```addSbtPlugin("io.sqooba.sbt" %% "sbt-generic-publish" % "0.1.0-SNAPSHOT")```
```resolvers += "Sqooba sbt" at "https://artifactory.sqooba.io/artifactory/libs-sbt-local/"```

Will look for repository credentials .credentials -files in USER_HOME/.sbt/

Configure path for artifact via artifactPath -key:
```artifactPath = new File("path to some file")```
```publishTo = Some("repoRealm" at "someurl")```

### Who do I talk to? ###
Created by Piet
