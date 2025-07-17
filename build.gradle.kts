
plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.danilopianini.gradle-java-qa") version "1.112.0"

    id("org.danilopianini.unibo-oop-gradle-plugin") version "1.1.40"
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }


repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "fxml",
    "swing",
    "graphics"
)

val supportedPlatforms = listOf("linux", "mac", "win") // All required for OOP

dependencies {
    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.3")

    // JavaFX: comment out if you do not need them
    val javaFxVersion = "23.0.2"
    implementation("org.openjfx:javafx:$javaFxVersion")
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }

    // WebSocket library for external communication
    val javaWebSocketVersion = "1.6.0"
    implementation("org.java-websocket:Java-WebSocket:$javaWebSocketVersion")

    // JSON library for manually parsing and generating JSON
    val orgJsonVersion = "20250517"
    implementation("org.json:json:$orgJsonVersion")

    // JSON library for convenient object-to-JSON mapping
    val jacksonVersion = "2.18.3"
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion") // For Optional support
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion") // For time type support (Instant)

    // CSV library for parsing and generating CSV
    implementation("com.opencsv:opencsv:5.9")

    // Serial communication library for USB multiplatform communication 
    implementation("com.fazecast:jSerialComm:[2.0.0,3.0.0)")

    // SLF4J for logging
    // This is a no-operation implementation, meaning it will not log anything.
    implementation("org.slf4j:slf4j-nop:2.0.17")

    // GUI library for JavaFX Gauge components
    implementation("eu.hansolo:Medusa:11.7")
    implementation("org.jfree:jfreechart:1.5.6")
    implementation("org.jfree:jfreechart-fx:1.0.1")

    // Apache Commons Lang for additional Java Collections utilities
    implementation("org.apache.commons:commons-collections4:4.5.0")
    
    // The BOM (Bill of Materials) synchronizes all the versions of Junit coherently.
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    // The annotations, assertions and other elements we want to have access when compiling our tests.
    testImplementation("org.junit.jupiter:junit-jupiter")
    // The engine that must be available at runtime to run the tests.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
}

val main: String by project

application {
    // Define the main class for the application
    mainClass.set(main)
}
