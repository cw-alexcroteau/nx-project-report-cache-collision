plugins {
  id("dev.nx.gradle.project-graph") version "0.1.12" apply false
}

allprojects {
  apply(plugin = "dev.nx.gradle.project-graph")
}