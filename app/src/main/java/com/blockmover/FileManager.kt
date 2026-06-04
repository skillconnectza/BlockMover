package com.blockmover

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileManager {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun export(context: Context, entries: List<BlockedEntry>): Uri {
        val payload = BlocklistExport(
            exportedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(Date()),
            count = entries.size,
            numbers = entries
        )
        val dir = File(context.cacheDir, "exports").apply { mkdirs() }
        val file = File(dir, "blocklist_${System.currentTimeMillis()}.json")
        file.writeText(gson.toJson(payload))
        return FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
    }

    fun import(context: Context, uri: Uri): BlocklistExport? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                gson.fromJson(InputStreamReader(stream), BlocklistExport::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun shareIntent(uri: Uri): Intent =
        Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
}
