package com.image.pagination.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.image.pagination.R
import java.util.*

@BindingAdapter("bind:drawable", "bind:spinner")
fun loadImage(view: ImageView, drawableName: String, spinner: ProgressBar) {
    spinner.visibility = View.VISIBLE
    Glide.with(view.context)
            .load(HelperFunctions.getImage(view.context, drawableName.split(".jpg".toRegex()).toTypedArray()[0]))
            .thumbnail(0.1f)
            .apply(RequestOptions().error(R.drawable.placeholder_for_missing_posters))
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                    spinner.visibility = View.GONE
                    view.scaleType = ImageView.ScaleType.FIT_CENTER
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    spinner.visibility = View.GONE
                    return false
                }
            })
            .into(view)
}

@BindingAdapter("bind:spannableText", "bind:searchText")
fun setText(view: TextView, fullText: String, searchText: String?) {
    if (searchText != null && searchText.isNotEmpty()) {
        val startPos = fullText.lowercase(Locale.US).indexOf(searchText.lowercase(Locale.US))
        val endPos = startPos + searchText.length
        if (startPos != -1) {
            val spannable: Spannable = SpannableString(fullText)
            val blueColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.YELLOW))
            val highlightSpan = TextAppearanceSpan(null, Typeface.NORMAL, -1, blueColor, null)
            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.text = spannable
        } else {
            view.text = fullText
        }
    } else {
        view.text = fullText
    }
}