package com.dosu.data

import com.dosu.properties.MusicProperties
import org.jdom2.Attribute
import org.jdom2.Element

data class File(val id: String = "null", val path: String = "null"){
    fun toElement(): Element {
        val element = Element(this::class.simpleName)
        element.setAttributes(mutableListOf(
            Attribute("id", id),
            Attribute("midiPaddingSec", path)
        ))
        return element
    }

    companion object {
        fun Element.getFile(): File {
            val element = getChild(MusicProperties::class.simpleName)
            return File(
                element.getAttributeValue("bpm"),
                element.getAttributeValue("midiPaddingSec")
            )
        }
    }
}