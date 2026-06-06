# BlockMover

Transfer your blocked phone numbers between Android devices quickly and easily. No root needed.

## What is BlockMover?

BlockMover allows you to export your blocked numbers list from one Android device and import it to another. Perfect for when you're switching phones or setting up a new device.

## Download

Download the latest APK from the [Releases](https://github.com/skillconnectza/BlockMover/releases) page.

## Installation Instructions

### On Your Android Device

1. Download the latest `BlockMover.apk` from the Releases section
2. On your Android device, go to **Settings > Security**
3. Enable **Install from Unknown Sources** (or **Install Unknown Apps** on Android 8+)
4. Open the downloaded APK file
5. Tap **Install** and follow the prompts
6. Once installed, you can disable "Unknown Sources" for security

### Using ADB (Alternative for developers)

1. Download the latest `BlockMover.apk`
2. Connect your Android device via USB with USB debugging enabled
3. Run: `adb install BlockMover.apk`

## Requirements

- Android 7.0 (API 24) or higher
- Approximately 5-10 MB storage space
- Both devices need BlockMover installed

## How to Use

### Exporting Blocked Numbers (Device A)

1. Open **BlockMover** on your source device
2. The home screen will show **⚠️ NOT default dialer**
3. Tap **Export** tab
4. **Step 1:** Tap **'Set as Default Dialer'**
   - A system dialog will appear
   - Confirm to grant permissions
5. Status updates to **✅**
6. Tap **'Read Blocked Numbers'**
   - The count of blocked numbers appears
7. Tap **'Export Blocklist'**
   - The share sheet appears
   - Send the JSON file to Device B (via email, messaging, cloud storage, etc.)
8. **Important:** Tap **'Open Default Apps Settings'**
   - Restore your original phone app as the default dialer

### Importing Blocked Numbers (Device B)

1. Install the same **BlockMover APK** on your destination device
2. Open **BlockMover**
3. Tap **Import** tab
4. **Step 1:** Tap **'Set as Default Dialer'**
   - Confirm in the system dialog
5. Tap **'Pick blocklist JSON'**
   - Navigate to the received JSON file
   - File information appears
6. Tap **'Import to Block List'**
   - Result shows inserted + skipped counts
7. **Important:** Tap **'Open Default Apps Settings'**
   - Restore your original phone app as the default dialer
8. Verify: Open your **Phone app > Settings > Blocked Numbers**
   - Confirm all entries are present

## Support This Project

This app is provided completely free of charge. If you find it useful and would like to support its development, consider making a small donation:

[![Donate with PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/paypalme/BrennyBrenny)

**Donations are optional and not required to use the app.**

## License

This software is licensed under a proprietary license for personal, non-commercial use only. See the [LICENSE](LICENSE) file for details.

**Commercial use, redistribution, and reverse engineering are prohibited.**

## Privacy & Permissions

- **Default Dialer Permission:** Required to read and write blocked numbers (temporary)
- **No Data Collection:** BlockMover does not collect, store, or transmit any personal data
- **Offline Operation:** Works completely offline. Files are only shared when you explicitly choose to do so.

## Troubleshooting

**App won't set as default dialer:** Make sure you don't have restrictions set by a device administrator or parental controls.

**Import shows 0 inserted:** The numbers may already exist in your block list (shown as "skipped").

**Can't find blocked numbers after import:** Make sure you restored the default dialer and check your Phone app settings.

## Contact

For questions, issues, or commercial licensing inquiries, please [open an issue](https://github.com/skillconnectza/BlockMover/issues).

---

Copyright (c) 2026. All rights reserved.
