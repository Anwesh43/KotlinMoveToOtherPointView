package ui.anwesome.com.kotlinmovetootherpointview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.movetootherpointview.MoveToOtherPointView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MoveToOtherPointView.create(this)
    }
}
