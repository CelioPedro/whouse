plugins {
    id("java")
    id("application") // Adicione este plugin
}

application {
    mainClass.set("br.com.dio.Main") // Nome da sua classe principal
}

group = "br.com.dio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}
