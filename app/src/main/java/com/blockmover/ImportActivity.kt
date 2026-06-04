package com.blockmover

import android.app.role.RoleManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.blockmover.databinding.ActivityImportBinding

class ImportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImportBinding
    private var parsedExport: BlocklistExport? = null

    private val requestDefaultDialer = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { updateDialerUi() }

    private val pickFile = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { parseFile(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRequestDialer.setOnClickListener { requestDialerRole() }
        binding.btnPickFile.setOnClickListener { pickFile.launch("application/json") }
        binding.btnImport.setOnClickListener { importNumbers() }
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

    private fun parseFile(uri: Uri) {
        parsedExport = FileManager.import(this, uri)
        if (parsedExport == null) {
            Toast.makeText(this, "Could not read file — is it a valid blocklist?", Toast.LENGTH_LONG).show()
            return
        }
        val exp = parsedExport!!
        binding.tvFileInfo.text = "✅ ${exp.count} numbers · exported ${exp.exportedAt.take(10)}"
        binding.tvFileInfo.visibility = View.VISIBLE
        // Grey out Pick button to signal this step is done
        binding.btnPickFile.isEnabled = false
        binding.btnImport.isEnabled = BlockedNumberManager.isDefaultDialer(this)
    }

    private fun importNumbers() {
        val export = parsedExport ?: return
        if (!BlockedNumberManager.isDefaultDialer(this)) {
            Toast.makeText(this, "Set BlockMover as default dialer first", Toast.LENGTH_SHORT).show()
            return
        }
        val (inserted, skipped) = BlockedNumberManager.writeAll(this, export.numbers)
        binding.tvResult.text = "✅ Done — $inserted imported, $skipped already blocked"
        binding.tvResult.visibility = View.VISIBLE
        binding.btnImport.isEnabled = false
        binding.cardRestoreDialer.visibility = View.VISIBLE
    }

    private fun updateDialerUi() {
        val isDefault = BlockedNumberManager.isDefaultDialer(this)
        binding.stepDialerStatus.text =
            if (isDefault) "✅ BlockMover is default dialer" else "⬜ Not yet set"
        binding.btnRequestDialer.isEnabled = !isDefault
        // Re-enable Import only if a file was picked and dialer is set, and import not done yet
        if (parsedExport != null && binding.cardRestoreDialer.visibility != View.VISIBLE) {
            binding.btnImport.isEnabled = isDefault
        }
    }

    private fun resetUi() {
        parsedExport = null
        binding.tvFileInfo.visibility = View.GONE
        binding.tvFileInfo.text = ""
        binding.tvResult.visibility = View.GONE
        binding.tvResult.text = ""
        binding.btnPickFile.isEnabled = true
        binding.btnImport.isEnabled = false
        binding.cardRestoreDialer.visibility = View.GONE
        updateDialerUi()
        Toast.makeText(this, "Reset — ready to start over", Toast.LENGTH_SHORT).show()
    }
}
