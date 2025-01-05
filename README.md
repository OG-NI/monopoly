# Initialisierung

Hauptprojekt erstellen:

```shell
mvn archetype:generate -DgroupId=de.dhbw.ase.monopoly -DartifactId=monopoly -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

`pom.xml` bearbeiten:

```xml
<packaging>pom</packaging>
```

Module anlegen (`domain`, `application`, `plugin`):

```shell
mvn archetype:generate -DgroupId=de.dhbw.ase.monopoly.<module> -DartifactId=<module> -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```
