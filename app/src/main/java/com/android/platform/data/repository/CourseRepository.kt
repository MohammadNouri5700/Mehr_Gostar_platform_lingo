package com.android.platform.data.repository


import com.android.platform.data.local.dao.CourseDao
import com.android.platform.data.local.entity.Course
import com.android.platform.utils.Result_
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepository @Inject constructor(private val courseDao: CourseDao) {

    fun getAllCourses(): Flow<Result_<List<Course>>> = flow {
        emit(Result_.Loading)
        try {
            val courses = courseDao.getAllCourses().first()
            emit(Result_.Success(courses))
        } catch (e: Exception) {
            emit(Result_.Error(e))
        }
    }

   fun insert(course: Course): Result_<Unit> {
        return try {
            courseDao.insert(course)
            Result_.Success(Unit)
        } catch (e: Exception) {
            Result_.Error(e)
        }
    }
}