package com.cevdetkilickeser.learnconnect.ui.presentation.watchcourse


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.cevdetkilickeser.learnconnect.data.repository.DownloadRepository
import com.cevdetkilickeser.learnconnect.data.repository.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchCourseViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val _isFullScreen = MutableStateFlow(false)
    val isFullScreen: StateFlow<Boolean> = _isFullScreen

    private val _lessons = MutableStateFlow<List<LessonStatus>>(emptyList())
    val lessons: StateFlow<List<LessonStatus>> = _lessons

    private val _isBuffering = MutableStateFlow(true)
    val isBuffering: StateFlow<Boolean> = _isBuffering

    private val _currentPlayingLessonNumber = MutableStateFlow<Int?>(null)
    val currentPlayingLessonNumber: StateFlow<Int?> = _currentPlayingLessonNumber

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private var _currentPosition: Long? = null

    suspend fun getLessonsByUserIdAndCourseId(userId: Int, courseId: Int) {
        val lessons = lessonRepository.getLessonsByUserIdAndCourseId(userId, courseId)
        _lessons.value = lessons
    }

    private fun play(
        exoPlayer: ExoPlayer, userId: Int, courseId: Int, lessonNumber: Int, isPlaying: Boolean
    ) {
        viewModelScope.launch {
            _currentPlayingLessonNumber.value = lessonNumber
            val lastPosition = lessonRepository.getLastPosition(userId, courseId, lessonNumber)
            val currentIndex = lessonNumber - 1
            exoPlayer.seekTo(currentIndex, lastPosition)
            lessonRepository.updateLessonIsWatchedStatus(userId, courseId, lessonNumber)
            lessonRepository.updateIsLastWatched(userId, courseId, lessonNumber)
            if (isPlaying) exoPlayer.play()
        }
    }

    fun startPlayerListener(exoPlayer: ExoPlayer, userId: Int, courseId: Int) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                _isBuffering.value = when (playbackState) {
                    Player.STATE_BUFFERING -> true
                    Player.STATE_READY -> false
                    else -> false
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    _isPlaying.value = true
                    saveCurrentPositionToRoom(exoPlayer, userId, courseId)
                    updateDuration(exoPlayer)
                    viewModelScope.launch {
                        val lessonNumber = exoPlayer.currentMediaItemIndex + 1
                        lessonRepository.updateLessonIsWatchedStatus(userId, courseId, lessonNumber)
                        lessonRepository.updateIsLastWatched(userId, courseId, lessonNumber)
                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO || reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK) {
                    val lessonNumber = exoPlayer.currentMediaItemIndex + 1
                    val isPlaying = exoPlayer.isPlaying
                    play(exoPlayer, userId, courseId, lessonNumber, isPlaying)
                }
            }
        })
    }

    private suspend fun setStartIndexAndPosition(exoPlayer: ExoPlayer, userId: Int, courseId: Int) {
        val lastWatchedLessonNumber = lessonRepository.getLastWatchedLessonNumber(userId, courseId)
        _currentPlayingLessonNumber.value = lastWatchedLessonNumber
        val lastPosition = lessonRepository.getLastPosition(
            userId, courseId, lastWatchedLessonNumber
        )
        val startIndex = (lastWatchedLessonNumber - 1)
        exoPlayer.seekTo(startIndex, lastPosition)
    }

    fun addPlaylistToExoPlayer(
        exoPlayer: ExoPlayer, userId: Int, courseId: Int
    ) {
        viewModelScope.launch {
            exoPlayer.clearMediaItems()
            _lessons.value.forEach { lesson ->
                val url =
                    if (lesson.lesson.filePath.isNullOrEmpty()) lesson.lesson.lessonUrl else lesson.lesson.filePath
                val mediaItem = MediaItem.fromUri(url)
                exoPlayer.addMediaItem(mediaItem)
            }
            setStartIndexAndPosition(exoPlayer, userId, courseId)
            exoPlayer.prepare()
            if (_isPlaying.value) {
                exoPlayer.playWhenReady = true
            }
        }
    }

    private fun saveCurrentPositionToRoom(exoPlayer: ExoPlayer, userId: Int, courseId: Int) {
        viewModelScope.launch {
            while (isActive) {
                if (exoPlayer.isPlaying) {
                    val currentPosition = exoPlayer.currentPosition
                    val lessonNumber = exoPlayer.currentMediaItemIndex + 1
                    lessonRepository.saveCurrentPositionToRoom(
                        userId, courseId, lessonNumber, currentPosition
                    )
                }
                delay(5000)
            }
        }
    }

    private fun updateDuration(exoPlayer: ExoPlayer) {
        viewModelScope.launch {
            while (exoPlayer.isPlaying) {
                _currentPosition = exoPlayer.currentPosition
                delay(100)
            }
        }
    }

    fun onClickLessonItem(exoPlayer: ExoPlayer, userId: Int, courseId: Int, lessonNumber: Int) {
        if (_currentPlayingLessonNumber.value != lessonNumber) {
            viewModelScope.launch {
                val isPlaying = exoPlayer.isPlaying
                play(exoPlayer, userId, courseId, lessonNumber, isPlaying)
            }
        }
    }

    fun downloadLesson(userId: Int, courseId: Int, lessonNumber: Int, url: String, title: String) {
        viewModelScope.launch {
            val downloadedUrl = downloadRepository.downloadVideo(url, title)
            lessonRepository.saveDownloadedFilePath(userId, courseId, lessonNumber, downloadedUrl)
            getLessonsByUserIdAndCourseId(userId, courseId)
        }
    }

    fun changeFullScreenStatus(isPlaying: Boolean) {
        _isFullScreen.value = !_isFullScreen.value
        _isPlaying.value = isPlaying
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}