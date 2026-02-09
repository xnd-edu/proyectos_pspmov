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


                !windowSizeClass.isWidthAtLeastBreakpoint(WindowBreakpoints.WIDTH_MEDIUM) &&
                windowSizeClass.isHeightAtLeastBreakpoint(WindowBreakpoints.HEIGHT_MEDIUM) -> MOBILE_PORTRAIT

                windowSizeClass.isWidthAtLeastBreakpoint(WindowBreakpoints.WIDTH_MEDIUM) &&
                !windowSizeClass.isHeightAtLeastBreakpoint(WindowBreakpoints.HEIGHT_MEDIUM) -> MOBILE_LANDSCAPE

                windowSizeClass.isWidthAtLeastBreakpoint(WindowBreakpoints.WIDTH_MEDIUM) &&
                !windowSizeClass.isWidthAtLeastBreakpoint(WindowBreakpoints.WIDTH_EXPANDED) &&
                windowSizeClass.isHeightAtLeastBreakpoint(WindowBreakpoints.HEIGHT_EXPANDED) -> TABLET_PORTRAIT

                windowSizeClass.isWidthAtLeastBreakpoint(WindowBreakpoints.WIDTH_EXPANDED) &&
                !windowSizeClass.isWidthAtLeastBreakpoint(WindowBreakpoints.WIDTH_LARGE) &&
                windowSizeClass.isHeightAtLeastBreakpoint(WindowBreakpoints.HEIGHT_MEDIUM) &&
                !windowSizeClass.isHeightAtLeastBreakpoint(WindowBreakpoints.HEIGHT_EXPANDED) -> TABLET_LANDSCAPE

                else -> DESKTOP
            }
        }
    }
}

