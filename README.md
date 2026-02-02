<p align="center">
<img align="center" src="./assets/slide_to_confirm.gif" />
</p>

# slidetoconfirm-compose [![](https://jitpack.io/v/chinalwb/slidetoconfirm-compose.svg)](https://jitpack.io/#chinalwb/slidetoconfirm-compose)


#### A reusable **“Slide to confirm”** component for **Jetpack Compose**.  

- Use it when you have any actions that require an extra, deliberate confirmation (e.g. `payments`, `logouts`, `destructive actions`).
- For using it in legacy view system, refer to the [Slide to confirm](https://github.com/chinalwb/slidetoconfirm). 
- Refer to the [demo app](https://github.com/chinalwb/slidetoconfirm-compose/tree/main/app) for a sample implementation.


### Quick demo
![State persistent](https://raw.githubusercontent.com/chinalwb/slidetoconfirm-compose/refs/heads/main/assets/slide_to_confirm_compose_video.gif)

---

## Features

- Jetpack Compose Support
- Flexible customization options, like slider icon, border, radii corners, text, colors, etc.
- Configurable confirmation threshold
- Optional haptic feedback (vibration) on success
- Simple callback when confirmation is completed
- Preview-friendly and easy to integrate in existing UIs
- State persistence across configuration changes, process death, and screen rotations

---

#### What does it look like?
<img src="https://raw.githubusercontent.com/chinalwb/slidetoconfirm-compose/refs/heads/main/assets/slide_to_confirm_compose_pics.png" width="360" />


---
## Parameters reference

| Parameter                   | Type                     | Description                                                                                                                                                          |
|----------------------------|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `modifier`                 | `Modifier`              | Layout and styling for the entire slider container (e.g., `height`, `fillMaxWidth`, `padding`).                                                                     |
| `threshold`                | `Float`                 | Fraction of the full slide distance required to trigger confirmation. Range: `0.0f..1.0f`. Example: `0.95f` means the user must slide to 95% of the track.         |
| `sliderContainerWidth`     | `Dp`                    | Visual width of the sliding knob container along the track.                                                                                                         |
| `sliderIcon`               | `Int`                   | Drawable resource ID used as the icon inside the slider knob.                                                                                                       |
| `sliderSize`               | `Dp`                    | Size of the icon inside the knob.                                                                                                                                   |
| `sliderTintColor`          | `Color`                 | Tint applied to the slider icon.                                                                                                                                    |
| `sliderBackgroundColor`    | `Color`                 | Background color of the slider knob itself.                                                                                                                         |
| `defaultBackgroundColor`   | `Color`                 | Background color of the track before and while sliding (unconfirmed state).                                                                                        |
| `swipedBackgroundColor`    | `Color`                 | Background color of the portion of the track that has been swiped / confirmed area.                                                                                |
| `borderRadius`             | `Dp`                    | Corner radius of both the track and knob background.                                                                                                                |
| `borderWidth`              | `Dp`                    | Stroke width of the track border.                                                                                                                                   |
| `borderColor`              | `Color`                 | Color of the track border.                                                                                                                                          |
| `engageText`               | `String`                | Text shown when the slider is in its default, unconfirmed state (e.g., “Slide to confirm”).                                                                        |
| `engageTextStyle`          | `TextStyle`             | Typography used for `engageText` (color, size, font, etc.).                                                                                                         |
| `confirmedText`            | `String`                | Text shown once the slider has fully confirmed (e.g., “Confirmed!”).                                                                                               |
| `confirmedTextStyle`       | `TextStyle`             | Typography used for `confirmedText`.                                                                                                                                |
| `vibrationMilliseconds`    | `Long`                  | Duration of vibration when confirmation succeeds. Set to `0L` to disable vibration.                                                                                |
| `defaultStatus`            | `SlideToConfirmStatus`  | Initial slider status: `SlideToConfirmStatus.INIT` – starts unconfirmed (normal use case); `SlideToConfirmStatus.CONFIRMED` – starts already confirmed.            |
| `onConfirmed`              | `() -> Unit`            | Callback invoked once the slider has passed the threshold and the confirmation animation completes. Place your action logic here (API calls, navigation, etc.).    |


## Installation

This library is published as an Android AAR.

### 1. Add JitPack

If you are consuming this from GitHub via JitPack, add the repository in your root `settings.gradle.kts` or `build.gradle.kts`:


- gradle.kts
```kotlin
allprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

- gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add the dependency

In your app module’s `build.gradle.kts`:
```
    dependencies {
        implementation("com.github.chinalwb:compose-slidetoconfirm:1.0.0")
    }
```

### 3. Usage

In your Compose UI:
```kotlin
SlideToConfirm(
    modifier = Modifier
        .height(60.dp)
        .background(color = Color.Transparent),
    sliderIcon = com.chinalwb.compose.slidetoconfirm.R.drawable.arrow,
    sliderBackgroundColor = Color(0xFF484EAA),
    sliderTintColor = Color(0xFFD81B60),
    sliderSize = 24.dp,
    sliderContainerWidth = 80.dp,
    borderColor = Color(0xFF484EAA),
    borderWidth = 2.dp,
    borderRadius = 30.dp,
    defaultBackgroundColor = Color.White,
    swipedBackgroundColor = Color(0xFFD81B60),
    engageText = "Slide to stop billing",
    engageTextStyle = TextStyle(fontSize = 17.sp, color = Color(0xFFD81B60)),
    defaultStatus = SlideToConfirmStatus.INIT
) {
    Log.d("xx", "SlideToConfirmTest: confirmed!")
}
```

And you will see:

<img src="https://raw.githubusercontent.com/chinalwb/slidetoconfirm-compose/refs/heads/main/assets/usage_screenshot.png" width="360" />


## License
MIT.
