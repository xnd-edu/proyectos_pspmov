package com.example.composeapp.ui.common

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        @Composable
        fun fromWindowAdaptiveInfo(
            windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
        ): DeviceConfiguration {
            val windowSizeClass = windowAdaptiveInfo.windowSizeClass

            return when {
                !windowSizeClass.isWidthAtLeastBreakpoint(600) &&
                windowSizeClass.isHeightAtLeastBreakpoint(480) -> MOBILE_PORTRAIT

                windowSizeClass.isWidthAtLeastBreakpoint(600) &&
                !windowSizeClass.isHeightAtLeastBreakpoint(480) -> MOBILE_LANDSCAPE

                windowSizeClass.isWidthAtLeastBreakpoint(600) &&
                !windowSizeClass.isWidthAtLeastBreakpoint(840) &&
                windowSizeClass.isHeightAtLeastBreakpoint(900) -> TABLET_PORTRAIT

                windowSizeClass.isWidthAtLeastBreakpoint(840) &&
                !windowSizeClass.isWidthAtLeastBreakpoint(1200) &&
                windowSizeClass.isHeightAtLeastBreakpoint(480) &&
                !windowSizeClass.isHeightAtLeastBreakpoint(900) -> TABLET_LANDSCAPE

                else -> DESKTOP
            }
        }
    }
}