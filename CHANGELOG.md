# Change Log

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