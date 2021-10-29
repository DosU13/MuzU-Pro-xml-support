package com.dosu.properties

import org.jdom2.Attribute
import org.jdom2.Element

data class MusicProperties(val bpm: Double = 128.0, val midiPaddingSec: Double = 0.0, val audioPath: String = ""){
    fun toElement(): Element {
        val element = Element(this::class.simpleName)
        element.setAttributes(mutableListOf(
            Attribute("bpm", bpm.toString()),
            Attribute("midiPaddingSec", midiPaddingSec.toString()),
            Attribute("audioPath", audioPath)
        ))
        return element
    }

    companion object {
        fun Element.getMusicProperties(): MusicProperties {
            val element = getChild(MusicProperties::class.simpleName)
            return MusicProperties(
                element.getAttributeValue("bpm").toDouble(),
                element.getAttributeValue("midiPaddingSec").toDouble(),
                element.getAttributeValue("audioPath")
            )
        }
    }
}