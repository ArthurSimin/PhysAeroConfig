# Changelog

## 1.0.5

### Fixed

- Fixed the animated intro music not playing when the intro was enabled again.
- Added the intro audio file to FancyMenu's resource preload list so it is ready after the Minecraft sound engine initializes.
- Prevented duplicate intro audio entries from being added when the setting is applied more than once.

### Testing

- Added a regression test covering restoration of the animated intro and preloading of its audio.
