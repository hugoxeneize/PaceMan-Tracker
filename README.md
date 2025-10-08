# PaceMan Tracker

A standalone application or Julti plugin to track and upload runs to PaceMan.gg.

## Usage

PaceMan Tracker is included as a default plugin in [Julti](https://github.com/DuncanRuns/Julti/releases) and [Jingle](https://github.com/DuncanRuns/Jingle/releases).
For those who don't/can't use Julti/Jingle, it can be [downloaded as an application and ran separately](https://github.com/PaceMan-MCSR/PaceMan-Tracker/releases/latest).

## Developing and Building

This repository no longer uses Gradle. Build the tracker with your preferred Java tooling (for example IntelliJ IDEA or direct `javac` invocations) and place the required dependency jars on the classpath. The application depends on Julti, Jingle, Gson, FlatLaf, the GitHub API client, and the IntelliJ GUI forms runtime. All of these jars can be obtained from the original tooling installations and reused locally for offline builds.

When editing GUI forms in IntelliJ IDEA, make sure the IDE is configured to generate Java source code for forms:
- `Settings` -> `Editor` -> `GUI Designer` -> `Generate GUI into: Java source code`

## Offline storage format

When the tracker is used without PaceMan.gg it writes run snapshots to `~/.config/PaceMan/runs.json` and reset summaries to `~/.config/PaceMan/stats.json`. A complete example of both files, including game metadata, event payloads, item tracking data, and reset statistics, is available in [`docs/local-output-example.json`](docs/local-output-example.json).

Reset statistics require the SpeedRunIGT companion mods by default. You can customise the comma-separated list of required mods from the settings window ("Required stats mods") or by editing `~/.config/PaceMan/options.json`. Leave the field blank to disable the check and allow stats to be saved regardless of the installed mod list.
