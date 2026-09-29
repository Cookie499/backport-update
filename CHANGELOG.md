# Changelog

## 1.1.1

### World and save compatibility
- Worlds that were last played in Minecraft 26.3 can now be opened on 26.2 with this mod. 26.3 stores a block in its default state as the bare block id ("minecraft:stone") and every other state as `{id: ..., properties: {...}}`; both forms are now read alongside this version's own `{Name: ..., Properties: {...}}` format. Chunk palettes, entities, block entities and item data are all covered. Everything is still *written* in this version's format, so the world stays readable by both versions; chunks are converted as they are loaded, and vanilla's "Optimize World" rewrites the rest.
- Cushions now store their colour the way 26.3 does - as the dye name (`"light_blue"`) instead of the numeric dye id - so a world made here keeps its cushion colours when it is opened in 26.3. Cushions saved by earlier builds of this mod keep their colour as well, since the numeric form is still read.
- The mod's own structure templates (the abandoned camps) already use the form both versions understand, so they need no conversion.

### Fixed
- Fixed the development-client startup crash "Data fixer not registered for: minecraft:cushion in entity_tree". The backported entity types (cushion, poplar boat, poplar chest boat) are unknown to this version's data fixer schema, which is expected; the lookup now answers without a fixer instead of going through the failing path. That also stops "No data fixer registered for ..." from filling the log.
- Fixed the crash "Critical injection failure: @Redirect annotation on backport$spareTeammates could not find any targets matching 'hurtEntities(Ljava/util/List;)V'". The explosion friendly-fire fix now targets the 26.2 signature, which takes no arguments.

## 1.1.0

### Registration
- Every block, item, entity, biome, sound, tag and worldgen entry now lives in the minecraft namespace instead of the mod's own, so the mod's content presents itself as vanilla content. Existing worlds will need a fresh one: ids such as backport:poplar_log are no longer registered.
- The backported content now sits in the vanilla creative tabs (poplar under Building Blocks, the wool/concrete/cushion families under Colored Blocks, the plants under Natural Blocks, the signs and straw bed under Functional Blocks, the boats under Tools & Utilities) at the same positions Minecraft 26.3 uses. The mod has no creative tab of its own - 26.3 adds none.

### Names and language files
- Fixed every block item showing its raw translation key instead of a name: they are now registered like vanilla's, so they resolve their block.* key instead of looking for an item.* key that never existed.
- Fixed the poplar sign and hanging sign items showing their raw translation key: signs are block-backed, so like vanilla's they now resolve block.minecraft.* rather than an item.minecraft.* key.
- Fixed the poplar boats missing their item.minecraft.* name (only the entity form existed after the namespace merge).
- Re-ported the localized names from the official Minecraft 26.3 language files. The concrete stairs and slabs, the explorer maps and thousands of other entries were still showing English in every language.

### Fixed
- Fixed the startup crash "Adding duplicate key minecraft:attached_to_logs": 26.2 already ships the attached_to_logs decorator and the fallen_tree feature, so the mod now uses the vanilla ones instead of registering its own copies under the same ids.
- Fixed the poplar signs and hanging signs rendering with no wooden geometry (only the text showed). Their blockstates and models were missing: the generated resources had a single bogus variant pointing at a particle-only stub, so the 14 template-based models (poplar_sign_rot_0-3, poplar_wall_sign, poplar_hanging_sign_rot_0-3, poplar_hanging_sign_attached_rot_0-3, poplar_wall_hanging_sign) and the 16/32-variant blockstates vanilla uses were generated.
- Straw beds can now be slept in everywhere and simply break when you get up, matching the 26.3 straw bed rule instead of showing a placeholder message.
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
