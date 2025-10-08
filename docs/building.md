# Building PaceMan Tracker without Gradle

The Gradle build scripts were removed in favour of a lightweight manual
workflow. The tracker can still be compiled from the command line as long as
the necessary dependency jars are available locally.

## 1. Collect dependencies

The application requires the following third-party libraries:

- **Julti** – provides most of the tracker integration points.
- **Jingle** – contains the shared GUI widgets.
- **Gson** – JSON parsing and serialisation.
- **FlatLaf** – Swing look & feel.
- **GitHub API client** – update checks.
- **IntelliJ GUI forms runtime** – renders the `.form`-generated UI classes.

Grab the jars from an existing Julti/Jingle installation or from a previously
generated Gradle build and place them in a `lib/` directory in the repository
root. The manual steps below assume `lib/` exists and contains all the jars.

```
PaceMan-Tracker/
├── lib/
│   ├── gson-*.jar
│   ├── flatlaf-*.jar
│   ├── julti-*.jar
│   ├── jingle-*.jar
│   ├── github-api-*.jar
│   └── forms-rt.jar
├── src/
└── ...
```

## 2. Compile the sources

Use the JDK's `javac` compiler and direct it to output the class files into a
temporary build directory. The command below works on macOS/Linux shells; on
Windows swap the classpath separator `:` for `;`.

```bash
mkdir -p build/classes
find src/main/java -name "*.java" > build/sources.txt
javac @build/sources.txt \
      -d build/classes \
      -cp "lib/*"
```

Copy the resource files so they are available on the runtime classpath:

```bash
rsync -a src/main/resources/ build/classes/
```

> Tip: replace `rsync` with `cp -R` on systems where `rsync` is not installed.

## 3. Package a runnable jar (optional)

If you want a single jar for distribution, package the compiled classes
alongside the resources:

```bash
jar --create \
    --file build/PaceManTracker.jar \
    -C build/classes .
```

Launch the jar with the dependencies on the classpath:

```bash
java -cp "build/PaceManTracker.jar:lib/*" gg.paceman.tracker.PaceManTracker
```

## 4. Use IntelliJ IDEA instead

IntelliJ IDEA can import the project as a plain Java project. Point the IDE to
the `src/main/java` and `src/main/resources` directories, then add the jars from
`lib/` as module dependencies. Make sure that `Settings → Editor → GUI Designer`
is configured to "Generate GUI into: Java source code" so the IDE keeps the
`.form` files and generated sources in sync.

With the dependencies in place, IntelliJ can build and run the application via
its standard Run/Debug configurations.
