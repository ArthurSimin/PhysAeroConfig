# Changelog

## 1.0.6

### Fixed

- Widened the optional FancyMenu dependency range from `[3.9.12,3.10)` to `[3.9.12,)` so FancyMenu 3.10.x and newer are accepted instead of blocking startup with an incompatible-version error.
- Verified the intro audio mixin against FancyMenu 3.10.0: the `GameIntroOverlay.render`, `close`, and `onReplaced` methods and the injected `PlayableResource.play()` call are unchanged.

## 1.0.5

### Fixed

- Fixed the animated intro music by playing it through Minecraft's sound engine at the same lifecycle point where FancyMenu starts the visual intro.
- Packaged the intro OGG as the `physaeroconfig:reimagined_intro` sound event.
- Stopped the intro sound when FancyMenu closes or replaces the intro overlay early.
- Removed the obsolete FancyMenu audio preload mutation, which could not make visual-only FMA resources play audio.

### Testing

- Updated the regression test to verify intro restoration no longer mutates FancyMenu's preload list with audio.
