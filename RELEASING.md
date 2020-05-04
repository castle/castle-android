# Releasing

This document describes the tasks to perform for tagging and releasing a new version of the Castle Android as well as publishing the new release to Bintray/JCenter.

## Prepare for release

 1. Update the version in `bintray.gradle` and `build.gradle`.
 2. Update documentation by running `javadoc -public -splitindex -d docs -Xdoclint:none -sourcepath castle/src/main/java/  -subpackages . -bootclasspath ~/Library/Android/sdk/platforms/android-28/android.jar` in the project root.
 3. Update the `CHANGELOG.md` for the impending release.
 4. `git commit -m "Prepare for release X.Y.Z."` (where X.Y.Z is the new version).
 5. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version).
 6. `git push && git push --tags`.
 
## Publish to Bintray

In order to publish a new version to Bintray run the following command from the project root: `./gradlew clean build bintrayUpload`. Make sure you've executed all the steps in the "Prepare for release" section before publishing.
 
## Create a new release on Github
1. Create a new Github release at https://github.com/castle/castle-android/releases
     * Add latest version information from `CHANGELOG.md`