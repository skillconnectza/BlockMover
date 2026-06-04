package com.blockmover

import android.telecom.InCallService

/**
 * Minimal stub required for Android to accept this app as a candidate
 * for the default dialer role. We do not handle real calls here.
 */
class BlockMoverInCallService : InCallService()
