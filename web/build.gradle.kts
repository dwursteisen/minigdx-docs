plugins {
    id("com.github.minigdx.js")
}


dependencies {
    implementation(project(":common"))
}

minigdx {
    version.set(libs.versions.minigdx.get())
}
