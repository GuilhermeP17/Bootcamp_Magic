package com.bootcamp.bootcampmagic.viewmodels

sealed class SetsViewModelState {
    data class Error(val message: Int): SetsViewModelState()
    object CacheLoaded: SetsViewModelState()
    object loadinContent: SetsViewModelState()
}