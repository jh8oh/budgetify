package dev.ohjiho.budgetify.utils.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.navigateTo(containerId: Int, fragment: Fragment, isInstant: Boolean = false) {
    if (isFragmentAlreadyShown(findFragmentById(containerId), fragment)) return

    beginTransaction().replace(containerId, fragment).commit()
    if (isInstant) {
        // executePendingTransactions() required so that commit() is not done asynchronously and is instead done instantly to
        // matched with the start of another animation
        executePendingTransactions()
    }
}

private fun isFragmentAlreadyShown(currentFragment: Fragment?, newFragment: Fragment): Boolean {
    return (currentFragment != null && currentFragment.javaClass == newFragment.javaClass)
}