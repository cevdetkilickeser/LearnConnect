package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Dao
import androidx.room.Query
import com.cevdetkilickeser.learnconnect.data.entity.course.Lesson

@Dao
interface LessonDao {

    @Query("SELECT * FROM lessons WHERE courseId = :courseId")
    suspend fun getLessonsByCourseId(courseId: Int): List<Lesson>

}