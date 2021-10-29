package com.dosu.project

import java.io.File

fun main() {
    val output = File("/home/dos_u/Desktop/video.mp4")
    val xmlPath = "src/main/resources/xml/first_test/0.0.0.xml"
//    Project.createNewUnsafe("first_test",xmlPath)
    val p = Project(xmlPath)
    p.renderer.render(output)
}