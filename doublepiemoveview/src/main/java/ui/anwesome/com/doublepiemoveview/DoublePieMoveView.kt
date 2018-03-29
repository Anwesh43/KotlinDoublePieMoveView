package ui.anwesome.com.doublepiemoveview

/**
 * Created by anweshmishra on 29/03/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
class DoublePieMoveView (ctx : Context) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer : Renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Int = 0, var j : Int = 0) {
        val scales : Array<Float> = arrayOf(0f, 0f, 0f)
        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j]  = prevScale + dir
                j += dir
                if (j == scales.size || j == -1) {
                    j -= dir
                    dir = 0
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0) {
                dir = 1 - 2 * prevScale.toInt()
                startcb()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }
        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
    data class DoublePieMove(var i : Int, val state : State = State()) {
        fun draw (canvas : Canvas, paint : Paint) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            val r = Math.min(w, h) / 10
            canvas.save()
            canvas.translate(w / 2, h / 2)
            canvas.rotate(90f * state.scales[1])
            for (i in 0..1) {
                val cx =  (2 * i - 1)* (w / 2) * (1 - state.scales[0]) + (h / 2 - r) * state.scales[2]
                canvas.drawArc(RectF(cx - r, -r, cx + r, r), 90f * (1 - 2 * i), 180f, false, paint)
            }
            canvas.restore()

        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer (var view : DoublePieMoveView) {
        val doublePieMove : DoublePieMove = DoublePieMove(0)
        val animator : Animator = Animator(view)
        fun render (canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            paint.color = Color.parseColor("#9b59b6")
            doublePieMove.draw(canvas, paint)
            animator.animate {
                doublePieMove.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            doublePieMove.startUpdating {
                animator.stop()
            }
        }
    }
}