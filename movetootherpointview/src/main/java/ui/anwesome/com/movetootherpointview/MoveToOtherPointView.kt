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
    data class MoveToOtherPoint(var point:PointF,var size:Float,var orig:PointF = PointF(point.x,point.y),var dest:PointF = orig) {
        val state:State = State()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(point.x,point.y)
            paint.color = Color.parseColor("#f44336")
            canvas.drawCircle(0f,0f,size/2,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            point.updateToDest(orig,dest,state.scale)
        }
        fun startUpdating(x:Float,y:Float,startcb:()->Unit) {
            dest = PointF(x,y)
            state.startUpdating(startcb)
        }
    }
    data class State(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1-2*scale
                startcb()
            }
        }
    }
}
fun PointF.updateToDest(orig:PointF,dest:PointF,scale:Float) {
    x = orig.x + (dest.x - orig.x)*scale
    y = orig.y + (dest.y - orig.y)*scale
}