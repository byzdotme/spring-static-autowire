import org.jreleaser.model.Active

plugins {
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.19.0"
}

group = "me.byz"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

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
            group = project.group
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            pom {
                name.set("spring static autowire")
                description.set("Inject Spring beans into static fields of your utility classes")
                url.set("https://github.com/byzdotme/spring-static-autowire")

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("byzdotme")
                        name.set("Baiyang Zhu")
                        email.set("by.zhu@live.cn")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/byzdotme/spring-static-autowire.git")
                    developerConnection.set("scm:git:ssh://github.com:byzdotme/spring-static-autowire.git")
                    url.set("https://github.com/byzdotme/spring-static-autowire/tree/main")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging"))
        }
    }
}

jreleaser {
    project {
        name.set(rootProject.name)
        version.set(rootProject.version.toString())
        description.set("Inject Spring beans into static fields of your utility classes")
        authors.add("Baiyang Zhu")
        license.set("MIT")
    }

    release {
        github {
        }
    }

    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository("build/staging")
                }
            }
        }
    }
}
