package com.photoeditor.photoeffect

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_my_creation.*
import java.io.File
import java.lang.Long.compare
import java.util.*
import kotlin.collections.ArrayList
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.widget.RelativeLayout
import com.photoeditor.photoeffect.MainActivity.Companion.isFromSaved


class MyCreationActivity : AppCompatActivity() {


    lateinit var img_path: ArrayList<File_Model>

    private var mLastClickTime: Long = 0
    fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_creation)

        list_creation.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        LoadImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        Log.e("Page","My creation")


    }

    inner class LoadImages : AsyncTask<Void, Void, Void?>() {


        override fun doInBackground(vararg params: Void?): Void? {
            updateFileList()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            if (img_path.size == 0) {
                var builder = AlertDialog.Builder(this@MyCreationActivity)
                builder.setMessage("No Files Found").setCancelable(false)
                    .setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            onBackPressed()
                        }
                    })
                var alert = builder.create()
                alert.show()
                return
            }

            if (img_path != null) {

                var creationAdapter = CreationAdapter(img_path)
                list_creation.adapter = creationAdapter
            }

        }

    }

    fun updateFileList() {
        var path = Environment.getExternalStorageDirectory().toString() + "/ArtisticEditor"
        val directory = File(path)
        val files = directory.listFiles()

        img_path = ArrayList()

        var fileDateCmp = Comparator<File> { f1, f2 ->
            compare(f2.lastModified(), f1.lastModified())
        }

        if (files != null) {
            Arrays.sort(files, fileDateCmp)

            for (i in 0 until files.size) {
                var file_model = File_Model()
                file_model.file_path = files[i].absolutePath
                file_model.file_title = files[i].name
                img_path.add(file_model)
            }
        }
    }

    inner class File_Model {
        lateinit var file_path: String
        lateinit var file_title: String
    }

    inner class CreationAdapter(imgPath: ArrayList<File_Model>) :
        RecyclerView.Adapter<CreationAdapter.CreationHolder>() {

        var paths = imgPath

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreationHolder {
            var view = LayoutInflater.from(this@MyCreationActivity)
                .inflate(R.layout.item_creation, parent, false)

            isFromSaved = false
            return CreationHolder(view)
        }

        override fun getItemCount(): Int {
            return paths.size
        }

        override fun onBindViewHolder(holder: CreationHolder, position: Int) {

            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            val width = dm.widthPixels
            val height = dm.heightPixels

            holder.img_creation.layoutParams = RelativeLayout.LayoutParams(width / 2, width / 2)

            holder.img_creation.setImageURI(Uri.parse(paths[position].file_path))
            holder.txt_title.setText(paths[position].file_title)
            holder.img_dlt.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    checkClick()

                    var builder = AlertDialog.Builder(this@MyCreationActivity)
                    builder.setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                var filepath = paths[position].file_path
                                if (File(filepath).delete()) {
                                    paths.removeAt(position)
                                    notifyDataSetChanged()
                                }
                                dialog!!.dismiss()
                            }
                        })
                        .setNegativeButton("No", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                            }
                        }).show()
                }

            })

            holder.img_creation.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    checkClick()

                    val intent = Intent(this@MyCreationActivity, ShowImageActivity::class.java)
                    intent.putExtra("image_uri", paths[position].file_path)
                    startActivity(intent)
                    finish()
                }
            })
        }

        inner class CreationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var img_creation = itemView.findViewById<ImageView>(R.id.img_creation)
            var img_dlt = itemView.findViewById<ImageView>(R.id.img_dlt)
            var txt_title = itemView.findViewById<TextView>(R.id.txt_title)
        }
    }
}
