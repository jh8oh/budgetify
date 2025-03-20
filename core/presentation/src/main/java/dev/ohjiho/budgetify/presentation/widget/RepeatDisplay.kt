package dev.ohjiho.budgetify.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import dev.ohjiho.budgetify.domain.model.Interval
import dev.ohjiho.budgetify.domain.model.Reoccurrence
import dev.ohjiho.budgetify.domain.model.Reoccurrence.Companion.NEVER_REPEATED_DISPLAY_TEXT
import dev.ohjiho.budgetify.presentation.R
import dev.ohjiho.budgetify.presentation.databinding.DialogRepeatBinding
import dev.ohjiho.budgetify.presentation.databinding.WidgetRepeatDisplayBinding
import dev.ohjiho.budgetify.utils.ui.runAfterTextChange

class RepeatDisplay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: WidgetRepeatDisplayBinding = WidgetRepeatDisplayBinding.inflate(LayoutInflater.from(context), this, true)
    private var listener: Listener? = null

    var reoccurrence: Reoccurrence? = null
        set(value) {
            field = value
            updateView()
        }

    var allowNeverRepeat = true

    init {
        updateView()
        setUpDisplay()
    }

    private fun setUpDisplay() {
        binding.root.setOnClickListener {
            listener?.showDialog(Dialog().apply {
                repeatDisplay = this@RepeatDisplay
            })
        }
    }

    private fun updateView() {
        binding.repeatText.text = reoccurrence?.toString() ?: NEVER_REPEATED_DISPLAY_TEXT
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun showDialog(dialog: Dialog)
    }

    class Dialog : DialogFragment() {
        private lateinit var binding: DialogRepeatBinding

        var repeatDisplay: RepeatDisplay? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
            binding = DialogRepeatBinding.inflate(layoutInflater)
            return AlertDialog.Builder(requireContext()).apply {
                setView(setUpView(savedInstanceState))
                setPositiveButton(R.string.dialog_repeat_positive, null)
                setNegativeButton(R.string.dialog_repeat_negative) { dialog, _ ->
                    dialog.cancel()
                }
            }.create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        if (onPositiveButtonPress()) {
                            dismiss()
                        } else {
                            Toast.makeText(context, NON_NULL_REOCCURRENCE_NEEDED, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun setUpView(savedInstanceState: Bundle?): View {
            return binding.run {
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    savedInstanceState?.getParcelable(TAG, Reoccurrence::class.java)
                } else {
                    savedInstanceState?.getParcelable(TAG)
                } ?: repeatDisplay?.reoccurrence)?.let { reoccurrence ->
                    fun checkWeeklyContainer() {
                        reoccurrence.indexOfRepetition.map {
                            when (it) {
                                1 -> monday.id
                                2 -> tuesday.id
                                3 -> wednesday.id
                                4 -> thursday.id
                                5 -> friday.id
                                6 -> saturday.id
                                else -> sunday.id
                            }
                        }.forEach {
                            weeklyContainer.check(it)
                        }
                    }

                    when (reoccurrence.interval) {
                        Interval.WEEKLY -> {
                            intervalSelection.check(weekly.id)
                            weeklyContainer.visibility = View.VISIBLE
                            checkWeeklyContainer()
                        }

                        Interval.BIWEEKLY -> {
                            intervalSelection.check(biweekly.id)
                            weeklyContainer.visibility = View.VISIBLE
                            checkWeeklyContainer()
                        }

                        Interval.MONTHLY -> {
                            intervalSelection.check(monthly.id)
                            monthlyContainer.visibility = View.VISIBLE
                            dayOfMonth.setText(reoccurrence.indexOfRepetition.first().toString())
                        }
                    }
                }

                intervalSelection.addOnButtonCheckedListener { group, id, isChecked ->
                    if (isChecked) {
                        when (id) {
                            weekly.id, biweekly.id -> {
                                weeklyContainer.visibility = VISIBLE
                                monthlyContainer.visibility = GONE

                                dayOfMonth.text = null
                            }

                            monthly.id -> {
                                weeklyContainer.visibility = GONE
                                monthlyContainer.visibility = VISIBLE

                                weeklyContainer.clearCheck()
                            }
                        }
                    } else if (group.checkedButtonId == NO_ID) {
                        weeklyContainer.visibility = GONE
                        monthlyContainer.visibility = GONE

                        weeklyContainer.clearCheck()
                        dayOfMonth.text = null
                    }
                }

                dayOfMonth.apply {
                    transformationMethod = null
                    runAfterTextChange {
                        try {
                            val parsedText = dayOfMonth.text.toString().toInt()
                            if (parsedText < 1) {
                                dayOfMonth.setText("1")
                            } else if (parsedText > 31) {
                                dayOfMonth.setText("31")
                            }
                        } catch (e: NumberFormatException) {
                            Log.e(TAG, "dayOfMonth's text is not a number")
                        }
                    }
                }

                root
            }
        }

        private fun onPositiveButtonPress(): Boolean {
            return try {
                val interval = getRepetitionInterval()
                val repetitionIndexes = getRepetitionIndexes()

                if (interval != null && repetitionIndexes.isNotEmpty()) {
                    repeatDisplay?.reoccurrence = Reoccurrence.with(interval, repetitionIndexes)
                    true
                } else {
                    if (repeatDisplay?.allowNeverRepeat == true) {
                        repeatDisplay?.reoccurrence = null
                        true
                    } else {
                        false
                    }
                }
            } catch (e: Exception) {
                e.message?.let { message -> Log.e(TAG, message) }
                false
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            try {
                val interval = getRepetitionInterval()
                val repetitionIndexes = getRepetitionIndexes()

                if (interval != null && repetitionIndexes.isNotEmpty()) {
                    outState.putParcelable(TAG, Reoccurrence.with(interval, repetitionIndexes))
                }
            } catch (e: Exception) {
                e.message?.let { message -> Log.e(TAG, message) }
                outState.putParcelable(TAG, null)
            }
            super.onSaveInstanceState(outState)
        }

        private fun getRepetitionInterval(): Interval? {
            with(binding) {
                return when (intervalSelection.checkedButtonId) {
                    weekly.id -> Interval.WEEKLY
                    biweekly.id -> Interval.BIWEEKLY
                    monthly.id -> Interval.MONTHLY
                    else -> null
                }
            }
        }

        private fun getRepetitionIndexes(): Set<Int> {
            with(binding) {
                return when (getRepetitionInterval()) {
                    Interval.WEEKLY, Interval.BIWEEKLY -> {
                        weeklyContainer.checkedChipIds.map {
                            when (it) {
                                monday.id -> 1
                                tuesday.id -> 2
                                wednesday.id -> 3
                                thursday.id -> 4
                                friday.id -> 5
                                saturday.id -> 6
                                else -> 7
                            }
                        }.toSet()
                    }

                    Interval.MONTHLY -> {
                        if (dayOfMonth.text.toString().isBlank()) {
                            emptySet()
                        } else {
                            setOf(dayOfMonth.text.toString().toInt())
                        }
                    }

                    null -> emptySet()
                }
            }
        }
    }

    companion object {
        const val TAG = "RepeatDisplayDialog"

        const val NON_NULL_REOCCURRENCE_NEEDED = "Please select an interval to repeat"
    }
}