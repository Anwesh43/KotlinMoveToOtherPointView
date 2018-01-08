package ui.anwesome.com.movetootherpointview

/**
 * Created by anweshmishra on 08/01/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
class MoveToOtherPointView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}