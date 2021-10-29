package com.dosu.project

import com.dosu.properties.MusicProperties.Companion.getMusicProperties
import com.dosu.properties.VideoProperties.Companion.getVideoProperties
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import org.jdom2.input.SAXBuilder
import java.io.File
import java.io.FileWriter
import java.io.IOException

class XmlManager {
    companion object {
        fun createNew(filepath: String, project: Project) {
            val file = File(filepath)
            if(file.exists()) throw IOException("file exists: $filepath")
            file.parentFile.mkdirs()
            write(filepath, project)
        }

        fun write(filepath: String, project: Project){
            val file = File(filepath)
            val doc = createDoc(project)
            val outer = XMLOutputter()
            outer.format = Format.getPrettyFormat()
            outer.output(doc, FileWriter(file))
        }

        private fun createDoc(project: Project) : Document{
            val root = Element("project")
            root.setAttribute("name", project.projectName)
            val properties = Element("properties")
            properties.addContent(project.videoProperties.toElement())
            properties.addContent(project.musicProperties.toElement())
            root.addContent(properties)
            val result = Document()
            result.rootElement = root
            return result
        }

        fun loadXmlInto(filepath: String, project: Project) {
            val doc: Document = SAXBuilder().build(File(filepath))
            project.projectName = doc.rootElement.getAttributeValue("name")
            val properties = doc.rootElement.getChild("properties")
            project.videoProperties = properties.getVideoProperties()
            project.musicProperties = properties.getMusicProperties()
        }
    }
}
