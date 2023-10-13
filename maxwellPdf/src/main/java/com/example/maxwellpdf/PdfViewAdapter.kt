package com.example.maxwellpdf

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.maxwellpdf.databinding.ListItemPdfPageBinding
import com.rajat.pdfviewer.util.hide
import com.rajat.pdfviewer.util.show


/**
 * Created by Rajat on 11,July,2020
 */

internal class PdfViewAdapter(
    private val renderer: PdfRendererCore,
    private val pageSpacing: Rect,
    private val enableLoadingForPages: Boolean
) :
    RecyclerView.Adapter<PdfViewAdapter.PdfPageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        return PdfPageViewHolder(
            ListItemPdfPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return renderer.getPageCount()
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PdfPageViewHolder(private val view: ListItemPdfPageBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(position: Int) {
            with(view) {
                handleLoadingForPage(position)

                pageView.setImageBitmap(null)
                renderer.renderPage(position) { bitmap: Bitmap?, pageNo: Int ->
                    if (pageNo != position)
                        return@renderPage
                    bitmap?.let {
                        containerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            height =
                                (containerView.width.toFloat() / ((bitmap.width.toFloat() / bitmap.height.toFloat()))).toInt()
                            this.topMargin = pageSpacing.top
                            this.leftMargin = pageSpacing.left
                            this.rightMargin = pageSpacing.right
                            this.bottomMargin = pageSpacing.bottom
                        }
                        pageView.setImageBitmap(bitmap)
                        pageView.animation = AlphaAnimation(0F, 1F).apply {
                            interpolator = LinearInterpolator()
                            duration = 300
                        }
                        view.pdfViewPageLoadingLayout.pdfViewPageLoadingProgress.hide()
                    }
                }
            }
        }

        private fun handleLoadingForPage(position: Int) {
            if (!enableLoadingForPages) {
                view.pdfViewPageLoadingLayout.pdfViewPageLoadingProgress.hide()
                return
            }

            if (renderer.pageExistInCache(position)) {
                view.pdfViewPageLoadingLayout.pdfViewPageLoadingProgress.hide()
            } else {
                view.pdfViewPageLoadingLayout.pdfViewPageLoadingProgress.hide()
            }
        }
    }
}