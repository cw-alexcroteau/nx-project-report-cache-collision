# nxProjectReport Gradle cache collision reproduction

[![Reproduce cache collision](https://github.com/cw-alexcroteau/nx-project-report-cache-collision/actions/workflows/reproduce.yml/badge.svg)](https://github.com/cw-alexcroteau/nx-project-report-cache-collision/actions/workflows/reproduce.yml)

This reproduces a cache-key collision in `dev.nx.gradle.project-graph` 0.1.12
when two Gradle subprojects have the same leaf name.

## Reproduce on macOS or Linux

```sh
./gradlew nxProjectGraph -Phash=local -PworkspaceRoot="$PWD" --build-cache
rm -rf a/api/build/nx b/api/build/nx
./gradlew nxProjectGraph -Phash=local -PworkspaceRoot="$PWD" --build-cache
shasum -a 256 a/api/build/nx/api.json b/api/build/nx/api.json
```

On the second run, both `:a:api:nxProjectReport` and
`:b:api:nxProjectReport` are restored `FROM-CACHE`. The reports are identical,
and `b/api/build/nx/api.json` incorrectly describes `:a:api`.

Running the second command with `--no-build-cache` generates the correct,
distinct reports.

## Reproduce on Windows

Run the following in PowerShell:

```powershell
.\gradlew.bat nxProjectGraph -Phash=local "-PworkspaceRoot=$PWD" --build-cache
Remove-Item -Recurse -Force a/api/build/nx, b/api/build/nx
.\gradlew.bat nxProjectGraph -Phash=local "-PworkspaceRoot=$PWD" --build-cache
Get-FileHash a/api/build/nx/api.json, b/api/build/nx/api.json
```

The reports have identical hashes, and `b/api/build/nx/api.json` incorrectly
describes `:a:api`.

## Continuous reproduction

The [reproduction workflow](https://github.com/cw-alexcroteau/nx-project-report-cache-collision/actions/workflows/reproduce.yml)
runs this scenario on Windows, Linux, and macOS. It expects the collision; a
run fails if the reports stop colliding, which signals that the upstream bug is
fixed.