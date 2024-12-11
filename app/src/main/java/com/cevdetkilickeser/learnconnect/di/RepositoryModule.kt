package com.cevdetkilickeser.learnconnect.di

import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import com.cevdetkilickeser.learnconnect.data.repository.DownloadRepository
import com.cevdetkilickeser.learnconnect.data.repository.EnrollmentRepository
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import com.cevdetkilickeser.learnconnect.domain.repository.CourseRepositoryImpl
import com.cevdetkilickeser.learnconnect.domain.repository.DownloadRepositoryImpl
import com.cevdetkilickeser.learnconnect.domain.repository.EnrollmentRepositoryImpl
import com.cevdetkilickeser.learnconnect.domain.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCourseRepository(courseRepositoryImpl: CourseRepositoryImpl): CourseRepository

    @Binds
    abstract fun bindDownloadRepository(downloadRepositoryImpl: DownloadRepositoryImpl): DownloadRepository

    @Binds
    abstract fun bindEnrollmentRepository(enrollmentRepositoryImpl: EnrollmentRepositoryImpl): EnrollmentRepository

    @Binds
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}