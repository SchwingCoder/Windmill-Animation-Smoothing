# Windmill Animation Smoothing

A standalone RuneLite plugin project, separate from Molch Pearl Chime.

## What it does

When RuneLite's built-in Animation Smoothing plugin is active, this plugin excludes the known windmill or mill sail sequences from interpolation:

- `AnimationID.MILLSAIL` (472), used by classic windmill sails such as the Draynor windmill.
- `AnimationID.FAI_VARROCK_MILLSAIL` (6495), the sail sequence used by the rebuilt Varrock windmill family.
- `AnimationID.BRAIN_MILL_SAIL_ROTATE` (5857), the rotating sail sequence used by the Zanaris mill.

The filter is keyed by animation ID, so every object using one of these sequences receives the same treatment; the plugin does not need to scan or list every world location. RuneLite's built-in filter already excludes the FAI Varrock and Brain mill sequences. This plugin adds the classic sequence and keeps the three known mill sequences grouped together so the behavior is explicit.

The plugin preserves the existing interpolation filter, reapplies its exclusions if the built-in smoothing plugin is toggled, and restores the original filter when this plugin is disabled.

## Build

On Windows, run `gradlew.bat build`. The project targets Java 11 and pins RuneLite client/API 1.13.4, matching the version resolved by the Molch Pearl Chime project during inspection.

## In-game check

Run `gradlew.bat run`, sign in to the development client, enable RuneLite's Animation Smoothing plugin, then enable Windmill Smoothing. Compare the windmills listed above and confirm ordinary object animations remain unchanged.
