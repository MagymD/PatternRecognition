package magym.patternrecognitionvegetables

import com.google.gson.GsonBuilder
import magym.patternrecognitionvegetables.data.datasourse.NetworkPhotoDataSource
import magym.patternrecognitionvegetables.data.service.PhotoService
import magym.patternrecognitionvegetables.domain.interactor.PhotoInteractor
import magym.patternrecognitionvegetables.domain.repository.PhotoRepository
import magym.patternrecognitionvegetables.presentation.screen.photo.PhotoViewModel
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {
    single {
        OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
    }

    single {
        GsonBuilder()
                .setLenient()
                .create()
    }

    single { GsonConverterFactory.create(get()) }
    single { RxJava2CallAdapterFactory.create() }

    single {
        Retrofit.Builder()
                .client(get())
                .addConverterFactory(get<GsonConverterFactory>())
                .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
                .baseUrl("https://fruitrecognition.pythonanywhere.com/")
                .build()
    }
}

val photoModule = module {
    single { get<Retrofit>().create(PhotoService::class.java) }
    single<PhotoRepository> { NetworkPhotoDataSource(get()) }
    single { PhotoInteractor(get()) }
    single { PhotoViewModel(get()) }
}