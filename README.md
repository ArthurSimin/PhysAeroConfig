# PhysAero Config

A small client-side mod made for the **[Create: Physics And Aeronautics](https://modrinth.com/modpack/create-physics-and-aeronautics)** modpack.

It handles a few setup options that are annoying to configure manually, mainly the FancyMenu intro and which account system the pack should use.

## Features

* **FancyMenu intro** — Choose whether to keep the animated intro or skip it when Minecraft starts.
* **Account system** — Switch between **e4all** for offline accounts and **Essentials** for online accounts.
* **Automatic mod switching** — Finds the e4all and Essentials `.jar` files in the `mods` folder and enables/disables them as needed. It also works when the mod versions change.
* **First-launch setup** — Shows a small setup screen the first time you launch the pack. If you close it, it'll show up again the next time you start Minecraft.
* **Config screen** — Change the settings later through NeoForge's mod menu. YACL can optionally be used for the config screen.
* **Restart notification** — Lets you know when Minecraft needs to be restarted after changing the account system.

The mod doesn't delete or modify the mod files themselves. It only renames them between `.jar` and `.jar.disabled` when switching account systems.

## Compatibility

* Minecraft **1.21.1**
* **NeoForge**
* Client-side
* **YACL** — Optional
