package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpBudgetsBinding

internal class SetUpBudgetsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSetUpBudgetsBinding.inflate(inflater).root
    }
}