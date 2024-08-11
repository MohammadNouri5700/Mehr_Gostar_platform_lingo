package com.android.platform.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.platform.data.local.entity.Course
import com.android.platform.data.repository.CourseRepository
import com.android.platform.utils.Result_
import com.android.platform.utils.UiState

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CourseViewModel @Inject constructor(private val repository: CourseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Course>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Course>>> = _uiState

    init {
        getAllCourses()
    }

    private fun getAllCourses() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllCourses().collect { result ->
                _uiState.value = when (result) {
                    is Result_.Success -> UiState.Success(result.data)
                    is Result_.Error -> UiState.Error(result.exception.message ?: "Unknown Error")
                    else -> UiState.Loading
                }
            }
        }
    }

    fun insert(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            when (val result = repository.insert(course)) {
                is Result_.Success -> getAllCourses() // Refresh data after insertion
                is Result_.Error -> _uiState.value = UiState.Error(result.exception.message ?: "Insertion Failed")
                Result_.Loading -> TODO()
            }
        }
    }
}