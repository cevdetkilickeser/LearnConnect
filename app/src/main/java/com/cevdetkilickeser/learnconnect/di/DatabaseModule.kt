package com.cevdetkilickeser.learnconnect.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.cevdetkilickeser.learnconnect.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .createFromAsset("prepopulated_database.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideCourseDao(database: AppDatabase) = database.courseDao()

    @Provides
    @Singleton
    fun provideLessonDao(database: AppDatabase) = database.lessonDao()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }
}