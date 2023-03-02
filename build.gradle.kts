import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    /** Amazon s3 client */
    implementation("com.amazonaws:aws-java-sdk:1.11.163")
    /** This depends on Amazon s3 */
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    /** Gzip archive */
    implementation("org.apache.commons:commons-compress:1.20")

    /** For mysql **/
    implementation("mysql:mysql-connector-java:8.0.27")

    /** Gson for config */
    implementation("com.google.code.gson:gson:2.10.1")

    /** Helper for java.lang */
    implementation("org.apache.commons:commons-lang3:3.12.0")

    /** For get information OS */
    implementation("com.github.oshi:oshi-core:6.4.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("BackuperS3.Main")
    //mainClass.set("MainKt")
}