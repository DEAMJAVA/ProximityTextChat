# Proximity Text Chat

Make Minecraft chat feel more natural by limiting communication to nearby players.

**Proximity Text Chat** replaces global chat with configurable proximity-based chat, so only players within a specified distance can read your messages. Death messages can also be limited to nearby players, making multiplayer interactions feel more immersive.

## Features

* 📢 Configurable player chat range
* 💀 Configurable death message range
* ⚙️ Lightweight JSON configuration
* 🪶 Lightweight and server-friendly
* 🌐 Works on Fabric

Perfect for survival servers, roleplay communities, SMPs, or anyone looking for a more immersive multiplayer experience.

## Configuration

The mod generates a configuration file at:

```text
config/proximity_text_chat.json
```

| Setting             | Description                                                           | Default |
| ------------------- | --------------------------------------------------------------------- | ------- |
| `chatRange`         | Maximum distance (in blocks) that players can receive chat messages.  | `200.0` |
| `deathMessageRange` | Maximum distance (in blocks) that players can receive death messages. | `200.0` |

## License

This project is licensed under the GNU General Public [License](LICENSE) v3.0 (GPL-3.0).
