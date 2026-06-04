package com.blockmover

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blockmover.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExport.setOnClickListener {
            startActivity(Intent(this, ExportActivity::class.java))
        }
        binding.btnImport.setOnClickListener {
            startActivity(Intent(this, ImportActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val isDefault = BlockedNumberManager.isDefaultDialer(this)
        binding.tvDialerStatus.text = if (isDefault)
            "✅ BlockMover is currently the default dialer"
        else
            "⚠️ BlockMover is NOT the default dialer"
    }
}
