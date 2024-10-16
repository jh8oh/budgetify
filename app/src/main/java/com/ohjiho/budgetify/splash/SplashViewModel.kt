package com.ohjiho.budgetify.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.repository.SetUpRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(setUpRepository: SetUpRepository) : ViewModel() {
    val isSetUp = setUpRepository.isSetUp()
}