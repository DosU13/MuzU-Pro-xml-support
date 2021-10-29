package com.dosu.project

import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.*
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File

class Renderer(private val project: Project) {
    private val vid get() = project.videoProperties
    private val mus get() = project.musicProperties

    fun render(outputPath: File) {
        var recorder: FFmpegFrameRecorder? = null
        var audioGrabber: FrameGrabber? = null
        try {
            println("render start")
            audioGrabber = FFmpegFrameGrabber(project.musicProperties.audioPath)
            audioGrabber.format = "mp3"
            audioGrabber.start()
            recorder = FFmpegFrameRecorder(
                outputPath,
                vid.width, vid.height, audioGrabber.audioChannels
            )
            recorder.format = "mp4"
            recorder.videoCodec = avcodec.AV_CODEC_ID_MPEG4
            recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
            recorder.frameRate = vid.fps
            recorder.sampleRate = audioGrabber.sampleRate
            //recorder.setMaxBFrames(5);
            recorder.start()
            renderPixels(recorder)
            renderAudio(recorder, audioGrabber)
        } catch (e: Exception) {
            println("Error")
            e.printStackTrace()
        } finally {
            try {
                audioGrabber?.stop()
                audioGrabber?.release()
                recorder?.stop()
                recorder?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        println("Render complete")
    }

    private fun renderPixels(recorder: FFmpegFrameRecorder) {
        val frameMaker = project.frameMaker
        val converter = Java2DFrameConverter()
        val hints: MutableMap<Any?, Any?> = HashMap()
        hints[RenderingHints.KEY_INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BICUBIC
        hints[RenderingHints.KEY_ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON
        hints[RenderingHints.KEY_COLOR_RENDERING] = RenderingHints.VALUE_COLOR_RENDER_QUALITY
        hints[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        for (i in 0 until vid.frameCount) {
            val image = BufferedImage(vid.width, vid.height, BufferedImage.TYPE_INT_ARGB)
            val g = image.createGraphics()
            g.setRenderingHints(hints)
            g.drawImage(frameMaker.makeFrame(i), 0, 0, null)
            g.dispose()
            recorder.record(converter.getFrame(image))
            println("Rendering: ${i * 100 / vid.frameCount} % $i/ ${vid.frameCount}")
        }
    }

    private fun renderAudio(recorder: FFmpegFrameRecorder, audioGrabber: FFmpegFrameGrabber) {
        val audioFps = audioGrabber.sampleRate / 1152.0
        var midiMarginFramesCount = (mus.midiPaddingSec * audioFps).toInt()
        println("AUDIO_MARGIN_SAMPLES:=>$midiMarginFramesCount")
        val audioFrameCount = (vid.frameCount * audioFps / vid.fps)
        println("Audio frame count: $audioFrameCount")
        var audioFrameIt = 0
        if (midiMarginFramesCount < 0) {
            while (midiMarginFramesCount++ < 0) audioGrabber.grabFrame()
        } else {
            audioFrameIt = midiMarginFramesCount
        }
        while (audioFrameIt < audioFrameCount) {
            audioFrameIt++
            audioGrabber.grabFrame()?.let { recorder.record(it) }
        }
    }
}