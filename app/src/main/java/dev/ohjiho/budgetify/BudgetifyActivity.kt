package dev.ohjiho.budgetify

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.ui.theme.BudgetifyTheme

@AndroidEntryPoint
class BudgetifyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetifyTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                        TransactionFab()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TransactionFab() {
    var isFabExpanded by rememberSaveable { mutableStateOf(false) }
    val animatedRotation by animateFloatAsState(targetValue = if (isFabExpanded) 585f else 0f, label = "rotation")

    ConstraintLayout {
        val (transferLabel, transferFab, incomeLabel, incomeFab, expenseLabel, expenseFab, transactionFab) = createRefs()

        val fabMenuLabelMarginEnd = 6.dp
        val fabMenuItemMarginBetween = 12.dp

        fun createModifiers(
            labelReference: ConstrainedLayoutReference,
            fabReference: ConstrainedLayoutReference,
            nextFabReference: ConstrainedLayoutReference,
        ) = Pair(
            Modifier.constrainAs(labelReference) {
                end.linkTo(fabReference.start, margin = fabMenuLabelMarginEnd)
                top.linkTo(fabReference.top)
                bottom.linkTo(fabReference.bottom)
            }, Modifier.constrainAs(fabReference) {
                bottom.linkTo(nextFabReference.top, margin = fabMenuItemMarginBetween)
                start.linkTo(transactionFab.start)
                end.linkTo(transactionFab.end)
            })

        val firstAnimationDelay = 0
        val secondAnimationDelay = 100
        val thirdAnimationDelay = 200

        // List of FABs to be shown when transaction menu is expanded
        // Add transfer transaction FAB
        val (transferLabelModifier, transferFabModifier) = createModifiers(transferLabel, transferFab, incomeFab)
        TransactionFABMenuItem(
            isFabExpanded,
            transferLabelModifier,
            transferFabModifier,
            "transfer",
            R.drawable.ic_transfer,
            thirdAnimationDelay,
            firstAnimationDelay
        ) {}

        // Add income transaction FAB
        val (incomeLabelModifier, incomeFabModifier) = createModifiers(incomeLabel, incomeFab, expenseFab)
        TransactionFABMenuItem(
            isFabExpanded,
            incomeLabelModifier,
            incomeFabModifier,
            "income",
            R.drawable.ic_income,
            secondAnimationDelay,
            secondAnimationDelay
        ) {}

        // Add expense transaction FAB
        val (expenseLabelModifier, expenseFabModifier) = createModifiers(expenseLabel, expenseFab, transactionFab)
        TransactionFABMenuItem(
            isFabExpanded,
            expenseLabelModifier,
            expenseFabModifier,
            "expense",
            R.drawable.ic_expense,
            firstAnimationDelay,
            thirdAnimationDelay
        ) {}

        // Add transaction FAB
        LargeFloatingActionButton(
            modifier = Modifier
                .constrainAs(transactionFab) { bottom.linkTo(parent.bottom) },
            shape = CircleShape,
            onClick = {
                isFabExpanded = !isFabExpanded
            }
        ) {
            Icon(
                Icons.Filled.Add, "Add new transaction", modifier = Modifier
                    .size(56.dp)
                    .rotate(animatedRotation)
            )
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun TransactionFABMenuItem(
    isFabExpanded: Boolean,
    labelModifier: Modifier,
    fabModifier: Modifier,
    label: String,
    iconId: Int,
    enterDelay: Int,
    exitDelay: Int,
    onClick: () -> Unit
) {
    val animationDurationMillis = 150
    val labelEnterDelay = enterDelay + 200
    val fabExitDelay = exitDelay + 200

    AnimatedVisibility(
        modifier = labelModifier,
        visible = isFabExpanded,
        enter = slideInHorizontally(tween(animationDurationMillis, labelEnterDelay)) { it / 2 } + fadeIn(tween(animationDurationMillis - 50, labelEnterDelay)),
        exit = slideOutHorizontally(tween(animationDurationMillis, exitDelay)) { it / 2 } + fadeOut(tween(animationDurationMillis - 50, exitDelay))
    ) {
        Surface(shape = RoundedCornerShape(4.dp)) {
            Text(modifier = Modifier.padding(12.dp, 4.dp), text = "Add $label", fontSize = 12.sp, maxLines = 1)
        }
    }

    AnimatedVisibility(
        modifier = fabModifier,
        visible = isFabExpanded,
        enter = scaleIn(tween(animationDurationMillis, enterDelay)),
        exit = scaleOut(tween(animationDurationMillis, fabExitDelay))
    ) {
        FloatingActionButton(shape = CircleShape, onClick = onClick) {
            Icon(painter = painterResource(id = iconId), contentDescription = label)
        }
    }
}
