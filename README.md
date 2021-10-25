### GitLab Structure

> flappyBird_local - local version of flappy bird
>
> flappyBird_online - flappy bird with online features
>
> flappyBird_database - flappy bird with database features
>
> flappyBird_pages - flappy bird with pages
>
> flappyBird_sensor - flappy bird with multiple sensors



### Environment Info.

#### Current runnable version for flappy bird:

```javascript
compileSdk 31
minSdk 21
targetSdk 31
versionCode 1
versionName "1.0"
sourceCompatibility JavaVersion.VERSION_1_8
targetCompatibility JavaVersion.VERSION_1_8
```

#### Java version:

```
>java -version
openjdk version "11.0.8" 2020-07-14
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.8+10)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.8+10, mixed mode)
```

#### Compile Options:

```
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```



### Implementation Question and Solution

#### No SDK installed

(1) Navigate from file -> settings -> System Settings -> Android SDK -> SDK Platform -> Apply

or 

(2) Navigate from file -> settings -> Appearance & Behavior -> System Settings -> HTTP Proxy -> Change to Auto-detect proxy setting -> add directory

#### How to add library to Android Studio

https://stackoverflow.com/questions/25660166/how-to-add-a-jar-in-external-libraries-in-android-studio

#### How to create virtual device on Android Studio

click on AVD manager -> Create new virtual device -> choose a version -> download and finish

#### How to add pixel graphs for flappy bird

go to project -> app -> src -> main -> res -> drawable

#### Stucked when run APP and prompt “waiting for all target devices to come online”

 Tools -> AVD manager -> more -> cold boot now
