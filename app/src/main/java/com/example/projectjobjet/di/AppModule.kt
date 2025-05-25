package com.example.projectjobjet.di //

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Cung cấp dependencies cho toàn bộ ứng dụng
object AppModule {

    @Singleton // Đảm bảo chỉ có một instance duy nhất
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    // Ví dụ cung cấp ApiService (bạn cần tạo class/interface ApiService trước)
    /*
    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("YOUR_BASE_URL")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    */

    // Ví dụ cung cấp JobRepository (bạn cần tạo class JobRepository và interface của nó nếu có)
    /*
    @Singleton
    @Provides
    fun provideJobRepository(apiService: ApiService): JobRepository {
        return JobRepositoryImpl(apiService) // Giả sử có JobRepositoryImpl
    }
    */
}