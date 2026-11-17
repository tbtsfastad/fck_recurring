# fck_recurring

## 中文

### 引入依赖

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

```kotlin
dependencies {
    implementation("com.github.tbtsfastad:fck_recurring:1.0.0")
}
```

### 调用

```kotlin
FckRecurring.initialize(applicationContext)
FckRecurring.instance.enqueue("sync", 15, SyncWorker())
```

`interval` 的单位是分钟。

## English

### Dependency

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

```kotlin
dependencies {
    implementation("com.github.tbtsfastad:fck_recurring:1.0.0")
}
```

### Usage

```kotlin
FckRecurring.initialize(applicationContext)
FckRecurring.instance.enqueue("sync", 15, SyncWorker())
```

`interval` is measured in minutes.
