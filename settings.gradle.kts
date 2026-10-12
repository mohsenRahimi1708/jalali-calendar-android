pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "JalaliCalendar"
include(":app")

// ---------------------------------------------------------------------------
// Mirror fallback (e.g. networks where dl.google.com is unreachable):
// Replace the repositories above with the Aliyun mirrors, or export them as
// init script. See README "Mirror setup" section.
//
// pluginManagement.repositories:
//   maven("https://maven.aliyun.com/repository/gradle-plugin")
//   maven("https://maven.aliyun.com/repository/google")
//   maven("https://maven.aliyun.com/repository/public")
// dependencyResolutionManagement.repositories:
//   maven("https://maven.aliyun.com/repository/google")
//   maven("https://maven.aliyun.com/repository/public")
// Gradle distribution: https://mirrors.cloud.tencent.com/gradle/gradle-8.11.1-bin.zip
//   (set distributionUrl in gradle/wrapper/gradle-wrapper.properties)
// ---------------------------------------------------------------------------
