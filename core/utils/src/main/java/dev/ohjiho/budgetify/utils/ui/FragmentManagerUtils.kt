package dev.ohjiho.budgetify.utils.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.navigateTo(containerId: Int, fragment: Fragment, isInstant: Boolean = false) {
    val existingFragment = findFragmentById(containerId)
    if (existingFragment != null && existingFragment.javaClass == fragment.javaClass) return

    beginTransaction().replace(containerId, fragment).commit()
    if (isInstant) {
        // executePendingTransactions() required so that commit() is not done asynchronously and is instead done instantly to
        // matched with the start of another animation
        executePendingTransactions()
    }
}