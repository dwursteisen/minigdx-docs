plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

val copyJs = project.tasks.register("unzipBundleJs", Copy::class) {
    this.dependsOn(":web:bundleJs")
    this.from(project.zipTree("web/build/minigdx/web.zip"))
    this.into("build/docs/asciidoc/")

}
project.tasks.getByName("asciidoctor").dependsOn(copyJs)
project.tasks.getByName("asciidoctor", org.asciidoctor.gradle.jvm.AsciidoctorTask::class) {
    this.baseDirFollowsSourceDir()
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }.mavenContent {
        includeVersionByRegex("com.github.minigdx.(.*)", "(.*)", "LATEST-SNAPSHOT")
        includeVersionByRegex("com.github.minigdx", "(.*)", "LATEST-SNAPSHOT")
    }
    mavenLocal()
}
