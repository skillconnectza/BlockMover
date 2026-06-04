package com.blockmover

data class BlockedEntry(
    val original: String,
    val e164: String?
)

data class BlocklistExport(
    val exportedAt: String,
    val appVersion: String = "1.0",
    val count: Int,
    val numbers: List<BlockedEntry>
)
