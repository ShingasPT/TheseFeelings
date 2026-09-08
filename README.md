# TheseFeelings

**TheseFeelings** is a Minecraft emote and interaction plugin that lets players express emotions through simple commands, messages, and sound effects.

The plugin is designed around configurable feelings rather than hard-coded individual commands, allowing new interactions to be added through configuration.

## Features

- Player-to-player emotes
- Configurable feelings and interactions
- Messages associated with each feeling
- Sound effects
- PlaceholderAPI integration
- Commands dynamically registered from configuration
- Dedicated manager and utility layers
- Folia support

Examples of the intended interactions include:

- Hug
- Slap
- Poke
- Other configurable social/emotional interactions

## Configuration

The plugin uses `config.yml` to define its available feelings and their behaviour.

Because commands are dynamically registered from configuration, new feelings can be added without needing to create a separate Java command class for every interaction.

This makes the plugin highly extensible for servers that want their own set of social interactions.

## Architecture

```text
me.shingas.theseFeelings
├── commands
├── managers
├── utils
└── TheseFeelings.java
```

The main plugin class initializes the configuration and registers the configured feeling commands.

## Requirements

- Minecraft/Paper 26.2
- PlaceholderAPI
- Java

The plugin declares Folia support.

## Building

The project uses Gradle.

```bash
./gradlew build
```

On Windows:

```bat
gradlew.bat build
```

The resulting plugin JAR will be generated in:

```text
build/libs/
```

## Installation

1. Install PlaceholderAPI.
2. Place `TheseFeelings.jar` into the server's `plugins` directory.
3. Start the server.
4. Configure the available feelings in `config.yml`.
5. Restart the server.

## Why This Approach?

Instead of implementing every emote as a separate command class, TheseFeelings treats feelings as configurable data.

This keeps the Java codebase smaller while making the actual content of the plugin easy for server owners to customize.

## License

Check the repository for the applicable license.
