# Android SDK for Castle

**[Castle](https://castle.io) adds real-time monitoring of your authentication stack, instantly notifying you and your users on potential account hijacks.**

[![Download](https://api.bintray.com/packages/castleintelligence/castle-android/castle/images/download.svg) ](https://bintray.com/castleintelligence/castle-android/castle/_latestVersion)
[![CircleCI](https://circleci.com/gh/castle/castle-android/tree/master.svg?style=svg)](https://circleci.com/gh/castle/castle-android/tree/master)
[![codecov](https://codecov.io/gh/castle/castle-android/branch/master/graph/badge.svg)](https://codecov.io/gh/castle/castle-android)

## Requirements

- Android 4.0

## Installation

Castle is available through Gradle.

### Gradle

Add the following line to your projects `build.gradle`

```ruby
compile 'io.castle.android:castle:latest-version'
```

Castle uses Tape2 for queuing events and is currently only available in the Sonatype snapshot repository. Add the following maven repository to your `build.gradle`.

```ruby
maven {
	url "https://oss.sonatype.org/content/repositories/snapshots"
}
```

## Usage

Please see the [Mobile Integration Guide](https://castle.io/docs/mobile).
