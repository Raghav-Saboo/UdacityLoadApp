package com.example.android.loadapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
  private var widthSize = 0
  private var heightSize = 0
  private var textWidth = 0f

  private var valueAnimator = ValueAnimator()

  private var textSize: Float = resources.getDimension(R.dimen.textSize)
  private var circleXOffset = textSize / 2

  private var buttonText: String

  private var progressWidth = 0f
  private var progressCircle = 0f

  private var buttonColor = ContextCompat.getColor(context, R.color.colorPrimary)
  private var loadingColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
  private var circleColor = ContextCompat.getColor(context, R.color.colorAccent)

  private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
    when (new) {
      ButtonState.Clicked -> {
        buttonText = resources.getString(R.string.button_clicked)
        invalidate()
      }
      ButtonState.Loading -> {
        buttonText = resources.getString(R.string.button_loading)
        valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat()).apply {
          addUpdateListener { animation ->
            progressWidth = animation.animatedValue as Float
            progressCircle = (widthSize.toFloat() / 365) * progressWidth
            invalidate()
          }
          duration = 2400
          disableViewDuringAnimation(findViewById(R.id.loadingButton))
          start()
        }
        invalidate()
      }
      ButtonState.Completed -> {
        valueAnimator.cancel()
        buttonText = resources.getString(R.string.button_name)
        progressWidth = 0f
        progressCircle = 0f
        invalidate()
      }
    }
  }

  private val paint = Paint().apply {
    isAntiAlias = true
    textSize = resources.getDimension(R.dimen.textSize)
  }

  init {
    buttonText = resources.getString(R.string.button_name)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    paint.color = buttonColor
    canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    paint.color = loadingColor
    canvas.drawRect(0f, 0f, progressWidth, heightSize.toFloat(), paint)
    paint.color = Color.WHITE
    textWidth = paint.measureText(buttonText)
    canvas.drawText(buttonText,
                    widthSize / 2 - textWidth / 2,
                    heightSize / 2 - (paint.descent() + paint.ascent()) / 2,
                    paint)
    drawProgressAsCircle(canvas)
  }

  private fun drawProgressAsCircle(canvas: Canvas) {
    canvas.save()
    canvas.translate(widthSize / 2 + textWidth / 2 + circleXOffset, heightSize / 2 - textSize / 2)
    paint.color = circleColor
    canvas.drawArc(RectF(0f, 0f, textSize, textSize), 0F, progressCircle * 0.365f, true, paint)
    canvas.restore()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
    val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
    val h: Int = resolveSizeAndState(
      MeasureSpec.getSize(w),
      heightMeasureSpec,
      0
    )
    widthSize = w
    heightSize = h
    setMeasuredDimension(w, h)
  }

  fun setLoadingButtonState(state: ButtonState) {
    buttonState = state
  }

  private fun ValueAnimator.disableViewDuringAnimation(view: View) {

    // This extension method listens for start/end events on an animation and disables
    // the given view for the entirety of that animation.

    addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator?) {
        view.isEnabled = false
      }

      override fun onAnimationEnd(animation: Animator?) {
        view.isEnabled = true
      }
    })
  }

}