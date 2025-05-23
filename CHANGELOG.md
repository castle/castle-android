# Change Log
## 3.0.10 (2025-04-23)
- [#74](https://github.com/castle/castle-android/pull/74) Add configuration for lifecycle events
- [#73](https://github.com/castle/castle-android/pull/73) Update gradle to version 8.11.1

## 3.0.9 (2024-11-26)
- Fixes an issue that could NumberFormatException when generating a token

## 3.0.8 (2024-02-15)
- [#69](https://github.com/castle/castle-android/pull/69) Update dependencies and resolves NPE when configuring the SDK

## 3.0.7 (2022-10-06)
- Fixes an issue that could cause ANRs

## 3.0.6 (2022-09-19)
- Fixes a bug that could result in the app crashing when generating a token

## 3.0.5 (2022-08-08)
- Fixes a bug that could result in the app crashing when the location accuracy is less than 1 meter.

## 3.0.4 (2022-06-29)
- Internal enhancements and stability improvements.
- [#62](https://github.com/castle/castle-android/pull/62) Fix custom event with no params validation

## 3.0.3 (2022-04-26)
- [#60](https://github.com/castle/castle-android/pull/60) Decrease batch size to 20
- [#59](https://github.com/castle/castle-android/pull/59) Add error class and throw if sdk methods are accessed without being configured
- [#58](https://github.com/castle/castle-android/pull/58) Fixes for permission logic and obfuscation issue that caused the host app to crash

## 3.0.2 (2022-04-06)
- Internal enhancements and stability improvements.

## 3.0.1 (2022-03-29)
- Internal enhancements and stability improvements.
- [#53](https://github.com/castle/castle-android/pull/53) Use widevine id if available

## 3.0.0 (2022-03-15)
- Add support for enhanced user activity monitoring.
	- Introduced custom(name) method
	- Introduced custom(name, properties) method
	- Introduced screen(name) method
	- Introduced userJwt() method
- Remove deprecated methods.
	- Removed identify()
	- Removed userId()
	- Removed secure()
	- Removed secureModeEnabled()
	- Removed userSignature()
	- Removed track()
	- Removed useCloudflareApp()
	- Removed apiDomain()
    - Removed apiPath()
	- Removed field clientIdHeaderName = "X-Castle-Client-Id";

## 2.1.4 (2022-03-03)
- Fixes a rare bug that resulted in incorrectly encoded request tokens.

## 2.1.3 (2022-02-09)
- Fixes a rare bug where the request token would occasionally get encoded incorrectly and consequently rejected by the API

## 2.1.2 (2021-09-13)
- Fixed requestToken encoding issue
- Fixed R8/Proguard obfuscation issue

## 2.1.1 (2021-08-17)
- [#45](https://github.com/castle/castle-android/pull/45) Make add and flush operations async in a single thread executor
- Fixed requestToken encoding issue

## 2.1.0 (2021-06-03)
- [#43](https://github.com/castle/castle-android/pull/43) Rename clientId to requestToken

## 2.0.4 (2021-05-06)
- Fix build issue that resulted in larger than needed release.

## 2.0.3 (2021-05-06)
- Fix: [#42](https://github.com/castle/castle-android/pull/42) Add try catch for NPE being thrown when deserializing queue
- Fix: Do not use standard R8 renamed packages prevent class collision

## 2.0.2 (2021-05-03)
- Fix: [#41](https://github.com/castle/castle-android/pull/41) Fix Android 11 crash when getting screen size

## 2.0.1 (2021-04-21)
- Feature: [#39](https://github.com/castle/castle-android/pull/39) Fix build issue

## 2.0.0 (2021-04-20)
- Feature: [#38](https://github.com/castle/castle-android/pull/38) Extended and improved device parameter collection

## 1.2.2 (2021-03-09)
- Fix: [#34](https://github.com/castle/castle-android/pull/34) Rename whitelist to allowList

## 1.2.1 (2021-01-11)
- Fix: [#33](https://github.com/castle/castle-android/pull/33) Change format of generated user agent to make sure that parts are parsed correctly

## 1.2.0 (2020-10-05)
- Feature: [#32](https://github.com/castle/castle-android/pull/31) Add ability to enable cloudflare app proxy usage
- Fix: [#31](https://github.com/castle/castle-android/pull/31) Use scaled pixels instead of real pixels when reporting screen size

## 1.1.5 (2020-05-04)
- Fix: [#30](https://github.com/castle/castle-android/pull/30) Remove non supported unicode characters from user agent string

## 1.1.4 (2020-03-10)
- Fix: [#28](https://github.com/castle/castle-android/pull/28) Fix code coverage issue
- Fix: [#27](https://github.com/castle/castle-android/pull/27) Handle activity with no title set in screen tracking

## 1.1.3 (2020-03-03)
- Improvement: [#25](https://github.com/castle/castle-android/pull/25) Add library size information
- Improvement: [#24](https://github.com/castle/castle-android/pull/24) Remove ability to add custom properties on events
- Improvement: [#23](https://github.com/castle/castle-android/pull/23) Remove ability to track custom events

## 1.1.2 (2019-03-26)
- Fix: [#22](https://github.com/castle/castle-android/pull/22) Add improved error handling when queue gets corrupted.
- Fix: [#21](https://github.com/castle/castle-android/pull/21) Remove support-core-utils to make avoid any support library dependency issues when including the Castle SDK.

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