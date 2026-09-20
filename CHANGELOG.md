# Changelog

## 1.1.0
- Fixed every block item showing its raw translation key instead of a name: they are now registered like vanilla's, so they resolve block.backport.* instead of looking for item.backport.* keys that never existed.
- Re-ported the localized names from the official Minecraft 26.3 language files. The concrete stairs and slabs, the explorer maps and thousands of other entries were still showing English in every language.
- Straw beds can now be slept in everywhere and simply break when you get up, matching the 26.3 straw bed rule instead of showing a placeholder message.
- The creative tab is now named "26.3物品" in every language.
- Cushions are drawn slightly smaller instead of with a depth offset, removing the rendering artifact that offset caused while still preventing z-fighting against the block below.

## 1.0.3

- Added official Minecraft 26.3 Snapshot 7 translations for all 143 languages across every supported Minecraft version and mod loader.
- Added missing localized names for the Dappled Forest biome and poplar boat entity types.
- Fixed the Fabric 1.21.11 startup crash caused by the outdated explosion mixin target.
- Declared the correct Java compatibility level for NeoForge 26.1 and 26.2 mixins.
- Fixed the Forge 1.20.1 client startup crash caused by an incompatible advancements-screen mixin.
- Fixed cushions rendering through blocks on NeoForge 1.21.1 and 26.2.
- Fixed cushions falling off signs and shelf mushrooms repeatedly bouncing standing entities on NeoForge 1.21.1.
- Corrected the Straw Bed inventory model orientation on NeoForge 1.21.1 and 26.2.

## 1.0.2

- Added release builds for Minecraft 26.1 NeoForge, Forge, and Fabric.
- Added release builds for Minecraft 1.21.11 Fabric, 1.19.2 Forge, and 26.2 Fabric/NeoForge.
- Fixed shelf mushrooms not bouncing entities and repeating the bounce sound while stood on.
- Fixed the Straw Bed inventory icon.
- Preserved and rendered custom cushion names after placement and pickup.
- Added the new empty-map and filled-map item textures.
- Prevented cushion z-fighting on stairs and other intersecting support blocks.
- Made wool stairs and slabs flammable like vanilla wool.
- Removed the vanilla strong-attack sound when breaking cushions.
- Fixed 1.19.2 Forge client startup and restored its client-side event registrations.
- Restored generated client models on 1.21.11 Fabric.
- Restored explorer-map cloning registration on 26.2 Fabric.

## 1.0.1

- Fixed missing and broken block-state models and textures across supported Minecraft versions.
- Restored transparent inserts for the door and trapdoor.
- Fixed hanging-sign, sapling, and hay-bed rendering.
- Restored vanilla-style poplar leaf and trunk generation.

## 1.0.0

- Initial public release of Backport.
- Backports Minecraft Java 26.3 content, gameplay changes, and selected vanilla bug fixes.
