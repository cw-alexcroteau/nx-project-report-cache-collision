# nxProjectReport Gradle cache collision reproduction

This reproduces a cache-key collision in `dev.nx.gradle.project-graph` 0.1.12
when two Gradle subprojects have the same leaf name.

## Reproduce

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