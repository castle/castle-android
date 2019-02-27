# Change Log

## 1.1.1 (2019-02-27)
- Fix: [#20](https://github.com/castle/castle-android/pull/20) Do not remove item from queue when iterating

## 1.1.0 (2019-02-20)

- Improvement: [#19](https://github.com/castle/castle-android/pull/19) Add release documentation
- Improvement: [#18](https://github.com/castle/castle-android/pull/18) Remove device name from event payload
- Improvement: [#17](https://github.com/castle/castle-android/pull/17) Set custom timeout to all API requests
- Improvement: [#16](https://github.com/castle/castle-android/pull/16) Include user agent in event context
- Improvement: [#15](https://github.com/castle/castle-android/pull/15) Use a custom User Agent for all requests to the Castle API
- Fix: [#14](https://github.com/castle/castle-android/pull/14) Fix timestamp format
- Fix: [#13](https://github.com/castle/castle-android/pull/13) Add internal `MAX_BATCH_SIZE`
- Fix: [#12](https://github.com/castle/castle-android/pull/12) Fix queue deserialize issue
- Fix: [#11](https://github.com/castle/castle-android/pull/11) Check debug logging configuration for http logging
- Feature: [#10](https://github.com/castle/castle-android/pull/10) Add support for secure mode
- Improvement: [#8](https://github.com/castle/castle-android/pull/8) Add javadoc documentation for public SDK classes

## 1.0.2 (2018-10-03)
- Fix: Remove unnecessary maniest values from library

## 1.0.1 (2018-03-22)
- Fix: Gradle dependencies

## 1.0 (2018-02-27)
Initial release

## 1.0b6 (2018-02-21)
- Improvement: Added Builder class for creating a Configuration object.
- Improvement: Simplified naming of external methods in Castle class.
- Improvement: Added Volley example and simplified examples for Okhttp and HttpUrlConnection.

## 1.0b5 (2018-02-14)
- Fix: Updated formatting for timestamp and locale to correspond to iOS SDK

## 1.0b4 (2018-02-12)
- Fix: Normalized OS version info with iOS SDK

- Improvement: Added new device information to context

		Library version  
		Timezone  
		Locale  
		Screen info  
		Network info (if permission is granted by host app)

## 1.0b3 (2018-02-02)
- Fix: Event serialization
- Improvement: Add forced flush before requests to whitelisted url

## 1.0b2 (2018-01-31)

## 0.9.2 (2017-06-19)
- Improvement: Updated flushing logic so that application open, application close and identify events trigger a flush that does not take the flushlimit into consideration.

- Improvement: Better handling of invalid events.

## 0.9.1 (2017-06-19)
- Fix: Device id persistence

## 0.9 (2017-05-31)

Initial public beta release