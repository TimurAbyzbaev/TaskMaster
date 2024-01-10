package ru.abyzbaev.taskmaster.utlis

import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import java.text.SimpleDateFormat
import java.util.*

fun formatDateFromLong(dateInMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = Date(dateInMillis)
    return dateFormat.format(date)
}

fun changeVectorDrawableColor(imageView: ImageView, colorResId: Int) {
    val drawable = imageView.drawable
    val color = ContextCompat.getColor(imageView.context, colorResId)

    // С использованием DrawableCompat
    val wrappedDrawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(wrappedDrawable, color)
    DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.SRC_IN)

    // Установка измененного drawable обратно в ImageView
    imageView.setImageDrawable(wrappedDrawable)
}
