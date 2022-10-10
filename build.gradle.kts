plugins {
    java
}

repositories {
    maven(url = "https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
    mavenCentral()
}

dependencies {
    implementation("com.velocitypowered:velocity-api:3.0.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.1")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
    implementation("com.electronwill.night-config:toml:3.6.0")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("javax.json:javax.json-api:1.1")
}

group = "com.matthewcash.network"
version = "2.0.0"
description = "Discord Messaging"
java.sourceCompatibility = JavaVersion.VERSION_17
