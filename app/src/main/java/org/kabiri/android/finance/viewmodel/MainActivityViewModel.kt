package org.kabiri.android.finance.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private val _inProgress = MutableLiveData<Boolean>()
    val inProgress: LiveData<Boolean>?
        get() = _inProgress

    fun showProgress() = _inProgress.postValue(true)

    fun hideProgress() = _inProgress.postValue(false)
}