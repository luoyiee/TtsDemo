package com.example.ttsdemo.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale


/**
 * Copyright (C) 2023-2024 Author
 *
 * TTS语音播报工具类
 *
 */
class TTSUtil(private val context: Context, listener: TTSListener?) {
    private val textToSpeech: TextToSpeech?
    private var initialized = false
    private var defaultSpeechRate = 1.0f // 默认语速
    private var defaultPitch = 1.0f // 默认音调
    private var defaultLocale: Locale = Locale.CHINESE // 默认语言
    private val utteranceId = "utteranceId" // 唯一标识符

    interface TTSListener {
        fun onInitSuccess()
        fun onInitFailure()
        fun onSpeechStart()
        fun onSpeechDone()
        fun onSpeechError(errorMessage: String?)
    }

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                initialized = true
                listener?.onInitSuccess()
            } else {
                listener?.onInitFailure()
            }
        }
    }

    // 设置默认语速
    fun setDefaultSpeechRate(speechRate: Float) {
        defaultSpeechRate = speechRate
    }

    // 设置默认音调
    fun setDefaultPitch(pitch: Float) {
        defaultPitch = pitch
    }

    // 设置默认语言
    fun setDefaultLocale(locale: Locale) {
        defaultLocale = locale
    }

    // 文本转语音
    @JvmOverloads
    fun speak(
        text: String?,
        utteranceId: String = this.utteranceId,
        locale: Locale? = defaultLocale,
        speechRate: Float = defaultSpeechRate,
        pitch: Float = defaultPitch
    ) {
        if (initialized) {
            textToSpeech!!.setLanguage(locale)
            textToSpeech.setSpeechRate(speechRate)
            textToSpeech.setPitch(pitch)

            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId

            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params)
            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) {
                    Log.d(TAG,"TTS TTSUtil onStart: $utteranceId")
                }

                override fun onDone(utteranceId: String) {
                    Log.d(TAG,"TTS TTSUtil onDone: $utteranceId")
                }

                override fun onError(utteranceId: String) {
                    Log.d(TAG,"TTS TTSUtil onError: $utteranceId")
                }
            })
        }
    }

    // 释放资源
    fun release() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    companion object {
        private const val TAG = "TTSUtil"
    }
}

