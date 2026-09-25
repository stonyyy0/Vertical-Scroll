# Changelog

## 1.2.0 (unreleased)

### Added

- **Scroll sensitivity** — a new setting in the Vertical Scroll settings screen. The
  scroll handler now accumulates scroll distance and advances the column one item only
  once a `1 / sensitivity` threshold is crossed. Default `1.0` is behaviour-neutral for
  notched wheels; high-resolution / momentum input (Magic Mouse, trackpads) no longer
  over-scrolls. Persisted in the config file.
- **"Scroll Column (1 step)" key binding** — rebindable, unbound by default, registered
  in the existing Vertical Scroll controls category. Advances the column by one item per
  press.
- **Pop animation** — column rotations now play vanilla's hotbar pop/stretch animation
  (the same one shown when picking up an item).

### Changed

- Extracted the column-rotation logic from `MouseMixin` into a shared `ColumnScroller`
  class, reused by both the scroll wheel and the new key binding.
- Added a Minecraft **26.3** source variant (`src/26.3.x`). 26.3 renamed
  `InputConstants.Type.KEYSYM` to `InputConstants.Type.KEYBOARD`; the variant is otherwise
  identical to `src/26.1.x`. (A matching build target needs to be registered in the build
  script.)


## 1.0.0 — Initial Release

### Features

- **Vertical column scrolling** — Hold the modifier key (default: Left Alt) and scroll the mouse wheel to cycle items through the vertical column of your currently selected hotbar slot. The column spans all three rows of the main inventory directly above the hotbar slot.

- **Scroll direction**
  - Scroll **up** — brings the top-row item down to the hotbar, shifting the rest of the column upward.
  - Scroll **down** — brings the bottom-row item up to the hotbar, shifting the rest of the column downward.

- **Column HUD overlay** — While the modifier key is held, a small overlay is rendered above the selected hotbar slot showing the three inventory slots in that column, including item icons and stack counts. Scroll direction arrows (▲ / ▼) indicate which end of the column will cycle to the hotbar.

- **Configurable modifier key** — The modifier key binding ("Vertical Scroll Modifier") is registered in the Vertical Scroll keybind category and can be rebound from the vanilla Controls menu.

- **Hotbar integration** — The scroll event is consumed when the modifier key is active, so the hotbar selection does not change while cycling the column.

- **Localization** — Ships with translations for 13 languages: English, German, Spanish, French, Italian, Japanese, Korean, Dutch, Polish, Brazilian Portuguese, Russian, Simplified Chinese, and Traditional Chinese.

- **Compatibility** — Client-side only; works on any server. Supports Minecraft 1.21 and above, including all 1.21.x releases and Minecraft 26.1.
