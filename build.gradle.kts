plugins {
    id("java")
    id("maven-publish")
}

group = "me.byz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.slf4j.api)
    compileOnly(libs.spring.context)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
