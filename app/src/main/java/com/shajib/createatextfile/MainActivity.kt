package com.shajib.createatextfile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shajib.createatextfile.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var fileNameList: ArrayList<String>? = null
    private var fileNameAdapter: FileNameAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fileNameList = ArrayList()
        fileNameAdapter = FileNameAdapter(this)
        getFileList()
        initAdapter()

        binding.btnSave.setOnClickListener {
            if (binding.etText.text.toString().trim().isNotEmpty()) {
                saveFile()
            } else {
                Snackbar.make(binding.root, "Please enter a file name", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        binding.rvFileList.adapter = fileNameAdapter // Set the adapter for the RecyclerView
        binding.rvFileList.layoutManager = LinearLayoutManager(this) // Set the layout manager for the RecyclerView
        fileNameAdapter?.notifyDataSetChanged() // Notify the adapter that the data set has changed [refreshes the list]
    }

    //mainFolder -> create newFolder -> create file then Write to file
    private fun saveFile() {
        val mainFolder = this.getExternalFilesDir(null) // Get the main folder
        val newFolder = File(mainFolder, "MyFiles") // Create a new folder inside the main folder

        try {
            val isDirMade = if (!newFolder.exists()) {
                newFolder.mkdir() // Create the new folder
            } else {
                true
            }

            if (isDirMade) {
                val fileName = binding.etText.text?.toString() // Get the file name from the EditText
                val file = File(newFolder, "$fileName.txt") // Create a new text file inside the new folder

                val isFileCreated = if (!file.exists()) {
                    file.createNewFile() // Create the text file
                } else {
                    true
                }
                if (isFileCreated) {
                    Snackbar.make(binding.root, "File Created", Snackbar.LENGTH_SHORT).show()

                    // Write the text to the file
                    val fileWriter = file.writer() // Get the file writer
                    fileWriter.write(binding.etText.text.toString()) // Write the text to the file
                    fileWriter.close() // Close the file writer

                    // Refresh the file list
                    getFileList()
                    fileNameAdapter?.notifyDataSetChanged()
                } else {
                    Snackbar.make(binding.root, "Couldn't Create File", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.root, "Couldn't Create Folder", Snackbar.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteFileWithName(name: String) {
        try {
            val mainFolder = this.getExternalFilesDir(null)
            val newFolder = File(mainFolder, "MyFiles")
            if (newFolder.exists() && newFolder.isDirectory) {
                var toDeleteFile = File(newFolder, name)
                if(toDeleteFile.exists() && toDeleteFile.isFile) {
                    var isDeleted = toDeleteFile.delete()
                    if(isDeleted) {
                        getFileList()
                        fileNameAdapter?.notifyDataSetChanged()
                        Snackbar.make(binding.root, "File Deleted", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(binding.root, "Couldn't Delete File", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getFileList() {
        var mainFolder = this.getExternalFilesDir(null)
        var newFolder = File(mainFolder, "MyFiles") // create a new folder inside the main folder same name as saveFile() newFolder
        if(newFolder.exists() && newFolder.isDirectory) {
            var listOfFiles = newFolder.listFiles() // Get the list of files in the new folder
            if(listOfFiles != null && listOfFiles.isNotEmpty()) {
                fileNameList?.clear()
                for(file in listOfFiles) {
                    fileNameList?.add(file.name)
                }
            }
        } else {
            Snackbar.make(binding.root, "No Files Found", Snackbar.LENGTH_SHORT).show()
        }
    }
}

//File location: /storage/emulated/0/Android/data/com.shajib.createatextfile/files/MyFiles/MyFile