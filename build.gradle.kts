import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "BackupS3"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    /** Amazon s3 client */
    implementation("com.amazonaws:aws-java-sdk:1.12.429")
    /** This depends on Amazon s3 */
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

    /** Gzip archive */
    implementation("org.apache.commons:commons-compress:1.21")

    /** For mysql **/
    implementation("mysql:mysql-connector-java:8.0.32")

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
    mainClass.set("BackupS3.Main")
    //mainClass.set("MainKt")
}

tasks {
    // val fatJar = register<Jar>("fatJar") {
    register<Jar>("fatJar") {
        isZip64 = true
        // We need this for Gradle optimization to work
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources"))

        // Naming the jar
        archiveClassifier.set("FatJar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        destinationDirectory.set(file("$buildDir/FatJar"))

        // Provided we set it up in the application plugin configuration
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to application.mainClass,
                    "Implementation-Version" to rootProject.version,
                    "Implementation-Title" to rootProject.name
                )
            )
        }

        val sourcesMain = sourceSets.main.get()

        val contents = configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        } + sourcesMain.output

        from(contents)
    }

    /* Trigger fat jar creation during build */
    /*build { dependsOn(fatJar) }*/
}