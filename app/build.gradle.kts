plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.passager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.passager"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    packaging {
        resources.excludes.add("META-INF/INDEX.LIST")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/NOTICE.md")
        //resources.excludes.add("META-INF/NOTICE.md")
    }

    configurations {
        all {
            resolutionStrategy {
                //force("com.sun.activation:jakarta.activation:1.2.2")
                exclude("com.sun.activation", "jakarta.activation")
                exclude("javax.xml.bind", "jaxb-api")
                exclude("com.android.support", "support-v4")

            }
        }
    }



}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    //implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    //implementation ("de.slackspace:openkeepass:0.8.2")
    implementation ("androidx.multidex:multidex:2.0.1")
    implementation("org.linguafranca.pwdb:KeePassJava2:2.2.1")
    //implementation ("com.android.tools.build:gradle:8.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.fragment:fragment-testing:1.4.1")


    androidTestImplementation("androidx.test:core:1.4.0")
    implementation ("javax.xml.stream:stax-api:1.0")
    implementation ("androidx.biometric:biometric:1.1.0")







}