package com.android.platform.di.factory

data class PriorityTask(
    val task: suspend () -> Unit,
    val priority: Int
) : Comparable<PriorityTask> {
    override fun compareTo(other: PriorityTask): Int {
        return other.priority - this.priority
    }
}