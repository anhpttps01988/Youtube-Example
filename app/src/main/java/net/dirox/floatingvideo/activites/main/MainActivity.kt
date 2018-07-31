package net.dirox.floatingvideo.activites.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.SeekBar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_main.*
import net.dirox.floatingvideo.DeveloperKey
import net.dirox.floatingvideo.R
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener, SeekBar.OnSeekBarChangeListener {
    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    private var mHandler = Handler()
    private var mRunnable: Runnable? = null
    private var player: YouTubePlayer? = null
    private var timer = Timer()
    private lateinit var mainFragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.title = "Floating Videos"

        mainFragment = MainFragment().newInstance("TzmVO4wJgyw")
        mainFragment.initialize(DeveloperKey.DEVELOPER_KEY, this)
        supportFragmentManager.beginTransaction().replace(R.id.id_container, mainFragment).commit()
        seekBarTimeLine.setOnSeekBarChangeListener(this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestore: Boolean) {
        this.player = player
        if (!wasRestore) {
            this.player?.cueVideo("TzmVO4wJgyw")
        }
        this.player?.setPlaybackEventListener(this)
        this.player?.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
        controllerPlayer(this.player)
    }

    private fun controllerPlayer(player: YouTubePlayer?) {
        btnPlay.setOnClickListener {
            player?.play()
        }
        btnPause.setOnClickListener {
            player?.pause()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            this.player!!.seekToMillis(progress * 1000)
        }
        val currentMillis: Long = (player!!.currentTimeMillis).toLong()
        val d = Date(currentMillis)
        val s = SimpleDateFormat("mm:ss", Locale.US)
        tvCurrentTime.text = s.format(d)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }


    override fun onSeekTo(p0: Int) {

    }

    override fun onBuffering(p0: Boolean) {

    }

    @SuppressLint("SetTextI18n")
    override fun onPlaying() {
        val millis: Long = (this.player!!.durationMillis).toLong()
        val date = Date(millis)
        val sdf = SimpleDateFormat("mm:ss", Locale.US)
        tvTimeline.text = sdf.format(date)

        if (this.player!!.isPlaying) {
            startTimeline()
        }

    }

    private fun startTimeline() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                mRunnable = Runnable {
                    seekBarTimeLine.max = player!!.durationMillis / 1000
                    seekBarTimeLine.progress = player!!.currentTimeMillis / 1000
                }
                mHandler.post(mRunnable)
            }
        }, 0, 1000)
    }

    override fun onStopped() {

    }

    override fun onPaused() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }
}
