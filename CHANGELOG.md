# Changelog

## 1.0.5

### Fixed

- Fixed the animated intro music by playing it through Minecraft's sound engine at the same lifecycle point where FancyMenu starts the visual intro.
- Packaged the intro OGG as the `physaeroconfig:reimagined_intro` sound event.
- Stopped the intro sound when FancyMenu closes or replaces the intro overlay early.
- Removed the obsolete FancyMenu audio preload mutation, which could not make visual-only FMA resources play audio.

### Testing

- Updated the regression test to verify intro restoration no longer mutates FancyMenu's preload list with audio.
