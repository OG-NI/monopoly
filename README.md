# Monopoly

## Maven Projekt Initialisierung

### Hauptprojekt erstellen:

```shell
mvn archetype:generate -DgroupId=de.dhbw.ase.monopoly -DartifactId=monopoly -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### In Projektverzeichnis navigieren

```shell
cd monopoly
```

### `pom.xml` bearbeiten

- [Multi-Module Project anlegen](https://maven.apache.org/pom.html#Aggregation_.28or_Multi-Module.29)
- Warnungen entfernen
  - [Character Encoding (`maven-resources-plugin`)](https://maven.apache.org/plugins/maven-resources-plugin/examples/encoding.html)
  - [Java Compiler Release](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html)

```xml
<project>
    <!-- ... -->
    <packaging>pom</packaging>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>17</maven.compiler.release>
    </properties>
</project>
```

### Module anlegen

Jeweils für `domain`, `application`, `plugin`:

```shell
mvn archetype:generate -DgroupId=de.dhbw.ase.monopoly -DartifactId=<module> -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### `pom.xml` der Untermodule bearbeiten

`<version>`, `<groupId>` und JUnit `<dependency>` entfernen

Abhängigkeiten zwischen Untermodulen hinzufügen:

```xml
<dependencies>
    <dependency>
        <groupId>${project.groupId}</groupId>
        <artifactId><!-- Name des Moduls eine Schicht weiter innen --></artifactId>
        <version>${project.version}</version>
    </dependency>
</dependencies>
```

### `pom.xml` des Models der äußersten Schicht bearbeiten:

- [Startklasse angeben](https://maven.apache.org/shared/maven-archiver/examples/classpath.html#Make)
- [alle Schichten in einer JAR](https://maven.apache.org/plugins/maven-shade-plugin/usage.html)

```xml
<build>
    <plugins>
        <plugin>
            <!-- Build an executable JAR -->
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>de.dhbw.ase.monopoly.Main</mainClass>
                    </manifest>
                </archive>
            </configuration>
        </plugin>
        <!-- Package all layers into one JAR -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.6.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
