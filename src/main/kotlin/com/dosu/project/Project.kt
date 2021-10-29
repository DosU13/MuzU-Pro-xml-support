package com.dosu.project

import com.dosu.properties.MusicProperties
import com.dosu.properties.VideoProperties

class Project {
    companion object{
        fun createNew(projectName: String, filePath: String){
            val project = Project()
            project.projectName = projectName
            XmlManager.createNew(filePath, project)
        }
        fun createNewUnsafe(projectName: String, filePath: String){
            val project = Project()
            project.projectName = projectName
            XmlManager.write(filePath, project)
        }
    }
    private constructor()
    constructor(xmlDir: String){
        XmlManager.loadXmlInto(xmlDir, this)
    }
    lateinit var projectName: String
    var videoProperties: VideoProperties = VideoProperties()
    var musicProperties: MusicProperties = MusicProperties()
    val frameMaker = FrameMaker(this)
    val renderer = Renderer(this)
}