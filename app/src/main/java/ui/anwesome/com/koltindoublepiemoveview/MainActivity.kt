package ui.anwesome.com.koltindoublepiemoveview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.doublepiemoveview.DoublePieMoveView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DoublePieMoveView.create(this)
    }
}
