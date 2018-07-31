package net.dirox.floatingvideo.activites.main

import android.os.Bundle
import com.google.android.youtube.player.YouTubePlayerSupportFragment

class MainFragment : YouTubePlayerSupportFragment() {


    fun newInstance(url: String): MainFragment {

        val f = MainFragment()

        val b = Bundle()
        b.putString("url", url)

        f.arguments = b

        return f
    }
}