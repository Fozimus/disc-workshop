# TODO
* [x] Fix disc workshop crafting not consuming dyes
* [ ] Render the music disc using a custom model instead of a mixin of ItemRenderer
* [x] Create a better readme
* [x] Render disc preview while crafting in the disc workshop
* [x] Add user configs
  * [ ] Add max download size
  * [ ] Add url whitelist / blacklist
  * [x] Add selectable quality
* [x] Add custom commands
  * [x] Command to clear cache
  * [x] Command to see cache size
  * [x] Command to list cache
* [x] Create mod icon
* [ ] Maybe download audio on tooltip to show title
* [x] Add url validation
* [x] Fix inventory label color
* [x] Refactor the code from DiscWorkshopClient and DiscWorkshop
* [x] Add the crafting preview on top of the disc workshop
* [x] Clear the disc workshop URL after crafting
* [ ] Add created disks in the creative tab
  * This could not be possible
* [ ] Add configurable random generated music discs in chests
* [x] Make sure that only one player at the time can open the disc workshop
* [x] Make the sound relative to the position
* [x] Fix hopper jukebox interaction
* [x] Prevent players far from the jukebox to download the audio
* [x] Stop multiple simultaneus download of the same url
* [ ] Fix the audio stopping when putting the volume to zero
  * Maybe this is not a bug because it happens eaven with vanilla Music Discs
* [ ] Start the audio playback for the joined player
  * This may require a change of how the sounds are handled
* [x] Fix audio playback starting eaven if the disk is removed while downloading
* [x] Exit gui when E is pressed and textbox is not focused
