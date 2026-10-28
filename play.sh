#!/usr/bin/env bash
set -e

echo "==================================================="
echo "            BALLS CLIENT - QUICK LAUNCHER          "
echo "==================================================="
echo ""
echo "Choose how to boot and play:"
echo "  1) Boot Minecraft 1.21.1 with Balls Client (Recommended)"
echo "  2) Boot Minecraft 1.20.1 with Balls Client"
echo "  3) Boot Minecraft 26.3 Snapshot with Balls Client"
echo "  4) Run test suite"
echo "  5) Exit"
echo ""
read -rp "Enter option [1-5]: " OPT

case "$OPT" in
    1)
        echo "Starting Minecraft 1.21.1 Client..."
        ./gradlew :fabric-1.21.1:runClient
        ;;
    2)
        echo "Starting Minecraft 1.20.1 Client..."
        ./gradlew :fabric-1.20.1:runClient
        ;;
    3)
        echo "Starting Minecraft 26.3 Client..."
        ./gradlew :fabric-26.3:runClient
        ;;
    4)
        ./gradlew test
        ;;
    *)
        exit 0
        ;;
esac
