package com.blockmover

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.blockmover.databinding.ActivityExportBinding

class ExportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExportBinding
    private var cachedEntries: List<BlockedEntry> = emptyList()

    private val requestDefaultDialer = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { updateDialerUi() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRequestDialer.setOnClickListener { requestDialerRole() }
        binding.btnReadNumbers.setOnClickListener { readNumbers() }
        binding.btnExport.setOnClickListener { exportAndShare() }
        binding.btnRestoreDialer.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
        }
        binding.btnReset.setOnClickListener { resetUi() }

        updateDialerUi()
    }

    override fun onResume() {
        super.onResume()
        updateDialerUi()
    }

    private fun requestDialerRole() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getSystemService(RoleManager::class.java)
                .createRequestRoleIntent(RoleManager.ROLE_DIALER)
        } else {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
            }
        }
        requestDefaultDialer.launch(intent)
    }

    private fun readNumbers() {
        if (!BlockedNumberManager.isDefaultDialer(this)) {
            Toast.makeText(this, "Set BlockMover as default dialer first", Toast.LENGTH_SHORT).show()
            return
        }
        cachedEntries = BlockedNumberManager.readAll(this)
        binding.tvCount.text = "✅ Found ${cachedEntries.size} blocked number(s)"
        binding.tvCount.visibility = View.VISIBLE
        // Grey out the button to show this step is complete
        binding.btnReadNumbers.isEnabled = false
        binding.btnExport.isEnabled = cachedEntries.isNotEmpty()
    }

    private fun exportAndShare() {
        if (cachedEntries.isEmpty()) return
        val uri = FileManager.export(this, cachedEntries)
        startActivity(Intent.createChooser(FileManager.shareIntent(uri), "Share blocklist via…"))
    }

    private fun updateDialerUi() {
        val isDefault = BlockedNumberManager.isDefaultDialer(this)
        binding.stepDialerStatus.text =
            if (isDefault) "✅ BlockMover is default dialer" else "⬜ Not yet set"
        binding.btnRequestDialer.isEnabled = !isDefault
        // Only re-enable Read if it hasn't been completed yet
        if (cachedEntries.isEmpty()) {
            binding.btnReadNumbers.isEnabled = isDefault
        }
    }

    private fun resetUi() {
        cachedEntries = emptyList()
        binding.tvCount.visibility = View.GONE
        binding.tvCount.text = ""
        binding.btnExport.isEnabled = false
        updateDialerUi()
        Toast.makeText(this, "Reset — ready to start over", Toast.LENGTH_SHORT).show()
    }
}
