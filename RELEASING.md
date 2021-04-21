# Releasing

This document describes the tasks to perform for tagging and releasing a new version of the Castle Android as well as publishing the new release to Bintray/JCenter.

## Prepare for release

 1. Update the version in `castle/gradle.properties`.
 2. Update documentation by running `./gradlew generateDebugJavadoc` in the project root.
 3. Update the `CHANGELOG.md` for the impending release.
 4. `git commit -m "Prepare for release X.Y.Z."` (where X.Y.Z is the new version).
 5. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version).
 6. `git push && git push --tags`.
 
## Publish to Sonatype

Add sonatype credentials to your global `gradle.properties` file

```
signing.keyId=1234567
signing.password=password
signing.secretKeyRingFile=/path/to/user/.gnupg/keyring.gpg

mavenCentralRepositoryUsername=
mavenCentralRepositoryPassword=
```

In order to upload a new version to Sonatype OSS staging run the following command from the project root: `./gradlew castle:publish --no-daemon --no-parallel`. Make sure you've executed all the steps in the "Prepare for release" section before publishing.

To release the uploaded version run `./gradlew closeAndReleaseRepository`
 
## Create a new release on Github
1. Create a new Github release at https://github.com/castle/castle-android/releases
     * Add latest version information from `CHANGELOG.md`