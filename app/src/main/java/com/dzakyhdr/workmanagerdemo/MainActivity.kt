package com.dzakyhdr.workmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import com.dzakyhdr.workmanagerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        const val KEY_VALUE = "key_value"
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            setOneTimeWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)
        // input data
        val data: Data = Data.Builder()
            .putInt(KEY_VALUE, 124)
            .build()
        // set Constraints
        val constraint = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraint)
            .setInputData(data)
            .build()
        workManager.enqueue(uploadRequest)
        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, {
            binding.showStatus.text = it.state.name
            //output data
            if (it.state.isFinished) {
                val data = it.outputData
                val message = data.getString(UploadWorker.KEY_WORKER)
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}