package com.dosu.properties

import org.jdom2.Attribute
import org.jdom2.Element

data class VideoProperties(
    val width: Int = 240, val height: Int = 240, val fps: Double = 60.0, val durationSeconds: Double = 10.0
){
    val frameCount: Int get() = (fps*durationSeconds).toInt()

    fun toElement(): Element {
        val element = Element(this::class.simpleName)
        element.setAttributes(mutableListOf(
            Attribute("width", width.toString()),
            Attribute("height", height.toString()),
            Attribute("fps", fps.toString()),
            Attribute("durationSeconds", durationSeconds.toString())
        ))
        return element
    }

    companion object {
        fun Element.getVideoProperties(): VideoProperties {
            val element = getChild(VideoProperties::class.simpleName)
            return VideoProperties(
                element.getAttributeValue("width").toInt(),
                element.getAttributeValue("height").toInt(),
                element.getAttributeValue("fps").toDouble(),
                element.getAttributeValue("durationSeconds").toDouble()
            )
        }
    }
}