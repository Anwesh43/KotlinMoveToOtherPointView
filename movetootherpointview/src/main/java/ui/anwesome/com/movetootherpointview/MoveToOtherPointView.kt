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
    data class MoveToOtherPoint(var point:PointF,var size:Float,var orig:PointF = PointF(point.x,point.y)) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(point.x,point.y)
            paint.color = Color.parseColor("#f44336")
            canvas.drawCircle(0f,0f,size/2,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
}