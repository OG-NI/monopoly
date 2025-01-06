# README

## Initialisierung

### Hauptprojekt erstellen:

```shell
mvn archetype:generate -DgroupId=de.dhbw.ase.monopoly -DartifactId=monopoly -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### In Projektverzeichnis navigieren:

```shell
cd monopoly
```

### `pom.xml` bearbeiten um Multi-Module Projekt zu aktivieren und Warnungen zu entfernen:

```xml
<project>
    <!-- ... -->
    <packaging>pom</packaging>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>23</maven.compiler.release>
    </properties>
</project>
```

> Dokumentation:
>
> - [Multi-Module Project](https://maven.apache.org/pom.html#Aggregation_.28or_Multi-Module.29)
> - [Character Encoding (`maven-resources-plugin`)](https://maven.apache.org/plugins/maven-resources-plugin/examples/encoding.html)
> - [Java Compiler Release](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html)

### Module anlegen (`domain`, `application`, `plugin`):

```shell
mvn archetype:generate -DgroupId=de.dhbw.ase.monopoly.<module> -DartifactId=<module> -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```
