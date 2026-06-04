package com.blockmover

import android.content.ContentValues
import android.content.Context
import android.provider.BlockedNumberContract
import android.telecom.TelecomManager

object BlockedNumberManager {

    fun isDefaultDialer(context: Context): Boolean {
        val telecom = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return telecom.defaultDialerPackage == context.packageName
    }

    fun readAll(context: Context): List<BlockedEntry> {
        val results = mutableListOf<BlockedEntry>()
        val cursor = context.contentResolver.query(
            BlockedNumberContract.BlockedNumbers.CONTENT_URI,
            arrayOf(
                BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
                BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER
            ),
            null, null, null
        ) ?: return results

        cursor.use {
            val origIdx = it.getColumnIndexOrThrow(
                BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER
            )
            val e164Idx = it.getColumnIndexOrThrow(
                BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER
            )
            while (it.moveToNext()) {
                val original = it.getString(origIdx) ?: continue
                results.add(BlockedEntry(original, it.getString(e164Idx)))
            }
        }
        return results
    }

    fun writeAll(context: Context, entries: List<BlockedEntry>): Pair<Int, Int> {
        var inserted = 0
        var skipped = 0
        entries.forEach { entry ->
            if (BlockedNumberContract.isBlocked(context, entry.original)) {
                skipped++
                return@forEach
            }
            val values = ContentValues().apply {
                put(
                    BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
                    entry.original
                )
                entry.e164?.let {
                    put(BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER, it)
                }
            }
            context.contentResolver.insert(
                BlockedNumberContract.BlockedNumbers.CONTENT_URI, values
            )
            inserted++
        }
        return Pair(inserted, skipped)
    }
}
