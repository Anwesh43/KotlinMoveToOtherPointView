package ui.anwesome.com.movetootherpointview

/**
 * Created by anweshmishra on 08/01/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

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
        fun update(updatecb:(PointF)->Unit,stopcb:(PointF)->Unit) {
            state.update{
                val t = orig.clone()
                point = dest.clone()
                orig = dest.clone()
                stopcb(t)
            }
            updatecb(point)
            point.updateToDest(orig,dest,state.scale)
        }
        fun startUpdating(x:Float,y:Float,startcb:()->Unit) {
            state.startUpdating{
                dest = PointF(x,y)
                startcb()
            }
        }
    }
    data class State(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:()->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1f
                prevScale = 0f
                scale = 0f
                startcb()
            }
        }
    }
    data class LineIndicator(var s:PointF,var e:PointF = s.clone(),var o:PointF = s.clone()) {
        val state = State()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = 5f
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawLine(s.x,s.y,e.x,e.y,paint)
        }
        fun updateEnd(point:PointF) {
            e = point.clone()
        }
        fun update(stopcb:(PointF)->Unit) {
            state.update{
                o = e.clone()
                s = e.clone()
                stopcb(s)
            }
            s.updateToDest(o,e,state.scale)
        }
        fun startUpdating(x:Float,y:Float,startcb: () -> Unit) {
            state.startUpdating {
                e = PointF(x,y)
                startcb()
            }
        }
    }
    data class Container(var w:Float,var h:Float) {
        var moveToOtherPoint = MoveToOtherPoint(PointF(w/2,h/2),Math.min(w,h)/15)
        var lineIndicator = LineIndicator(PointF(w/2,h/2))
        val state = ContainerState()
        var updateFns:LinkedList<()->Unit> = LinkedList()
        init {
            state.addFn { it
                moveToOtherPoint.update({
                    lineIndicator.updateEnd(it)
                },{
                    lineIndicator.startUpdating(it.x,it.y,{
                        it()
                    })
                })
            }
            state.addFn {
                lineIndicator.update({
                    moveToOtherPoint.startUpdating(it.x,it.y,{
                        it()
                    })
                })
            }
            state.addFn{
                moveToOtherPoint.update({
                    lineIndicator.updateEnd(it)
                },{
                    lineIndicator.startUpdating(it.x,it.y,{
                        it()
                    })
                })
            }
            state.addFn {
                lineIndicator.update {
                    it()
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            moveToOtherPoint.draw(canvas,paint)
            lineIndicator.draw(canvas,paint)
        }
        fun update(stopcb:()->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(x:Float,y:Float,startcb:()->Unit) {
            state.startUpdating {
                moveToOtherPoint.startUpdating(x,y,startcb)
            }
        }
    }
    data class ContainerState(var j:Int = 0,var dir:Int = 0) {
        val updateFns:LinkedList<(()->Unit)->Unit> = LinkedList()
        fun addFn(updateFn:(()->Unit)->Unit) {
            updateFns.add(updateFn)
        }
        fun update(stopcb: () -> Unit) {
            updateFns.get(j).invoke({
                j++
                if(j == updateFns.size) {
                    stopcb()
                    j = 0
                    dir = 0
                }
            })
        }
        fun startUpdating(startcb: () -> Unit) {
            if(dir == 0) {
                dir = 1
                startcb()
            }
        }
    }
}
fun PointF.updateToDest(orig:PointF,dest:PointF,scale:Float) {
    x = orig.x + (dest.x - orig.x)*scale
    y = orig.y + (dest.y - orig.y)*scale
}
fun PointF.clone():PointF  = PointF(x,y)