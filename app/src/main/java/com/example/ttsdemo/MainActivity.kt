package com.example.ttsdemo

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ttsdemo.util.TTSUtil
import java.util.Locale


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tts: TTSUtil
    private lateinit var button: Button
    private lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        button = findViewById(R.id.button)
        text = findViewById(R.id.textView)
        button.setOnClickListener(this)
        tts = TTSUtil(this, object : TTSUtil.TTSListener {
            override fun onInitSuccess() {
                Log.d("TTS", "初始化成功")
                tts.speak("准备就绪")
            }

            override fun onInitFailure() {
                Log.e("TTS", "初始化失败")
            }

            override fun onSpeechStart() {
                Log.d("TTS", "开始播放")
            }

            override fun onSpeechDone() {
                Log.d("TTS", "播放结束")
            }

            override fun onSpeechError(errorMessage: String?) {
                Log.e("TTS", "错误：$errorMessage")
            }
        })
    }

    override fun onDestroy() {
        tts.release()
        super.onDestroy()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> {
                tts.speak(text.text.toString())
            }
        }
    }
}