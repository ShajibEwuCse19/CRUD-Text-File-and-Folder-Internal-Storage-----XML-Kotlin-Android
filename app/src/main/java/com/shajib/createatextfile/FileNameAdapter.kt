package com.shajib.createatextfile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Shajib
 * @since Aug 27, 2024
 **/
class FileNameAdapter(private val activity: MainActivity) : RecyclerView.Adapter<FileNameAdapter.FileNameViewHolder>() {
    inner class FileNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fileNameTextView: TextView? = null

        init {
            fileNameTextView = itemView.findViewById(R.id.fileNameView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileNameViewHolder {
        return FileNameViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.list_item_file, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (activity.fileNameList != null) {
            activity.fileNameList!!.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: FileNameViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.fileNameTextView?.text = activity.fileNameList?.get(position)
        holder.itemView.setOnClickListener {
            AlertDialog.Builder(activity)
                .setMessage("Do you want to delete this file?")
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        activity.fileNameList?.get(position)?.let {
                            activity.deleteFileWithName(it)
                        }
                    }
                })
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                    }
                })
                .show()
        }
    }
}