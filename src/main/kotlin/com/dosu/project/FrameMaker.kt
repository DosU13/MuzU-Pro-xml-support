package com.dosu.project

import java.awt.Color
import java.awt.image.BufferedImage

class FrameMaker(private val project: Project) {
    fun makeFrame(i: Int): BufferedImage {
        val w: Int = project.videoProperties.width
        val h: Int = project.videoProperties.height
        val resImg = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        for (x in 0 until w) {
            for (y in 0 until h) {
                val c = Color(x.toFloat()/w, y.toFloat()/h, 1f)
                val color = c.red shl 24 or (c.green shl 16) or (c.blue shl 8) or c.alpha
                resImg.setRGB(x, y, color)
            }
        }
        return resImg
    }

}