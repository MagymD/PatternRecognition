package magym.patternrecognitionvegetables.util

import android.app.Application
import org.koin.android.ext.android.startKoin

class VegetablesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(retrofitModule, photoModule))
    }

}