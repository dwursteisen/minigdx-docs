plugins {
    id("com.github.minigdx.jvm")
}

dependencies {
    implementation(project(":common"))
}

minigdx {
    mainClass.set("your.game.Main")
}
