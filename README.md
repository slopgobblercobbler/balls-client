# Balls Client

Balls Client — The ultimate standalone modular client for Minecraft (1.20 to 26.3) with 500+ dynamic modules across Visuals, Movement, Combat, Player, Inventory, Automation, and Telemetry.

## Architecture
- Decoupled Hexagonal / Platform Adapter Architecture (`:common` pure-JVM domain logic, `:fabric-1.20.1`, `:fabric-1.21.1`, `:fabric-26.3` platform adapters).
- Multi-version build matrix with Gradle 8.14.6 and Fabric Loom.
